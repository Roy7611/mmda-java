package cloud.mmda.core.security;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.enums.UserStatus;
import cloud.mmda.core.security.exceptions.InvalidUserAccountException;
import cloud.mmda.core.security.utils.InternationalMobile;
import cloud.mmda.core.security.models.OpenIdentity;
import cloud.mmda.core.security.models.User;
import cloud.mmda.core.security.models.UserAccount;
import cloud.mmda.core.security.tokens.OtpTokenStore;
import cloud.mmda.core.security.utils.UsernameParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * DAO of {@link UserAccount}
 *
 * @author Roy Luo
 */
@Component("serviceUserAccountService")
public class UserAccountService implements UserDetailsService {

    private StringRedisTemplate stringRedisTemplate;
    private HashOperations<String, String,String> stringRedisHashOps;

    private UserRepository _userRepository;
    private OtpTokenStore _otpTokenStore;

    /**
     * Construct a user account DAO service
     * @param userRepository Jpa repository for load user account from database
     * @param otpTokenStore One-time password token store
     */
    public UserAccountService(@Qualifier("serviceUserRepository") UserRepository userRepository, OtpTokenStore otpTokenStore, RedisConnectionFactory redisConnectionFactory) {
        _userRepository = userRepository;
        _otpTokenStore = otpTokenStore;
        stringRedisTemplate= new StringRedisTemplate(redisConnectionFactory);
        stringRedisHashOps = stringRedisTemplate.opsForHash();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.empty();
        boolean canbeOneTimePwd = false;
        String oneTimePwdUsername = null;
        if(UsernameParser.isEmail(username)){
            user = _userRepository.findByEmail(username);
            canbeOneTimePwd = true;
            oneTimePwdUsername = username;
        }
        else{
            Optional<InternationalMobile> mobile = UsernameParser.parseMobile(username);
            if(mobile.isPresent()){
                var m = mobile.get();
                if(StringUtils.hasText(m.getCountryAreaCode()))
                    user = _userRepository.findByMobileAndTelPrefix(m.getPhoneNumber(), m.getCountryAreaCode());
                else
                    user = _userRepository.findByMobile(m.getPhoneNumber());
                canbeOneTimePwd = true;
                oneTimePwdUsername = m.getCallablePhoneNumber();
            }
            else{
                Optional<OpenIdentity> oid = UsernameParser.parseOpenId(username);
                if(oid.isPresent()){
                    var o = oid.get();
                    user = _userRepository.findByOpenIdentity(o.getOpenIDType(),o.getOpenID());
                }
                else{
                    user = _userRepository.findByUsername(username);
                }
            }
        }

        if(user.isEmpty()){
            throw new InvalidUserAccountException("User not found or password invalid");
        }

        String oneTimePassword = null;
        if(canbeOneTimePwd){
            oneTimePassword = _otpTokenStore.retrieve(oneTimePwdUsername);
        }
        return new UserAccount(user.get(), oneTimePassword);
    }

    public User loadUserByUserIdWithDevices(long userId){
        var user = _userRepository.findByUserID(userId);
        if(user.isEmpty()){
            throw new InvalidUserAccountException("User not found");
        }
        var u = user.get();
        u.getDevices();
        return u;
    }


    public List<User> getUsersByModulePermission(int tenantID,String moduleCode) {
        long maxID=Tenancy.buildEntityID(tenantID,  Tenancy.MAX_REAL_ID);
        long minID=Tenancy.buildEntityID(tenantID,  0);
        var users = _userRepository.findUsersByModulePermission(UserStatus.ACTIVATED_VAL,minID,maxID,moduleCode);
        users.forEach(u -> {
            u.setRoles(null);
            u.setDevices(null);
            u.setOpenIdentities(null);
            assembleRefProperties(u);
        });
        return users;
    }


    public List<User> getUsersByModuleActionPermission(int tenantID,String moduleCode,List<String> actionNames){
//        String names = ;
        long maxID=Tenancy.buildEntityID(tenantID,  Tenancy.MAX_REAL_ID);
        long minID=Tenancy.buildEntityID(tenantID,  0);
        var users=  _userRepository.findUsersByModuleActionPermission(UserStatus.ACTIVATED_VAL,minID,maxID,moduleCode,String.join("|",actionNames));
        users.forEach(u -> {
            u.setRoles(null);
            u.setDevices(null);
            u.setOpenIdentities(null);

            assembleRefProperties(u);
        });
        return users;
    }

    private static final String DEPTID_COLNAME="deptID";
    private User assembleRefProperties(User t){
        try {
            String refValue=t.getDeptID()+"";
            String cacheKeyOfRef=String.join(":", String.valueOf(t.getTenantID()),"REF_Department");
            if(existsRefMap(cacheKeyOfRef)){
                String refActionText =getRefText(cacheKeyOfRef,refValue);
                if(refActionText == null) {
                    Map<String,String> refActionMap = loadRefDepartMentMap(t.getTenantID());
                    refActionText=refActionMap.getOrDefault(refValue,"-");
                    setRefEnumText(cacheKeyOfRef,refValue,refActionText);
                }
                t.setRefProperty(DEPTID_COLNAME,refActionText);
            }else{
                Map<String,String> refActionMap = loadRefDepartMentMap(t.getTenantID());
                t.setRefProperty(DEPTID_COLNAME, refActionMap.getOrDefault(refValue,"-"));
            }

        }catch (Exception ex){
//            String msg = String.format("组装%1$s.%2$s引用属性值异常。", "User",DEPTID_COLNAME);
//            logger.error(msg, ex);
        }
        return t;
    }

    private Map<String, String> loadRefDepartMentMap(int tenantID) {
        long maxID=Tenancy.buildEntityID(tenantID,  Tenancy.MAX_REAL_ID);
        long minID=Tenancy.buildEntityID(tenantID,  0);
        List<Object[]> results = _userRepository.findDepartments(minID,maxID);
        return results.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(), // deptID
                        row -> row[1].toString()  // deptName
                ));
    }

    public String getRefText(String refSet, String refKey){
        return stringRedisHashOps.get(refSet,refKey);
    }
    public Boolean existsRefMap(String refSet){
        return stringRedisTemplate.hasKey(refSet);
    }
    public void setRefEnumText(String refSet, String refKey, String refText){
        stringRedisHashOps.put(refSet,refKey,refText);
    }
}
