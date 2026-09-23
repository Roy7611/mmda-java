package cloud.mmda.core.security;

import cloud.mmda.core.security.exceptions.InvalidUserAccountException;
import cloud.mmda.core.security.utils.InternationalMobile;
import cloud.mmda.core.security.models.OpenIdentity;
import cloud.mmda.core.security.models.User;
import cloud.mmda.core.security.models.UserAccount;
import cloud.mmda.core.security.repository.UserRepository;
import cloud.mmda.core.security.tokens.OtpTokenStore;
import cloud.mmda.core.security.utils.UsernameParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * DAO of {@link UserAccount}
 *
 * @author Roy Luo
 */
@Component("userAccountService")
public class UserAccountService implements UserDetailsService {
    private UserRepository _userRepository;
    private OtpTokenStore _otpTokenStore;

    /**
     * Construct a user account DAO service
     * @param userRepository Jpa repository for load user account from database
     * @param otpTokenStore One-time password token store
     */
    public UserAccountService(@Qualifier("securityUserRepository") UserRepository userRepository, OtpTokenStore otpTokenStore) {
        _userRepository = userRepository;
        _otpTokenStore = otpTokenStore;
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
}
