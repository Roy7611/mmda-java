package cloud.mmda.core.security.models;

import cloud.mmda.core.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Account of user who will sign in our application.
 *
 * @author Roy Luo
 * @since 4.0
 */
public class UserAccount implements UserDetails {
    @Getter
    private long userID;
    private String username;
    @Getter
    private String avatar;
    @Getter
    private String telPrefix;
    @Getter
    private String mobile;
    @Getter
    private String email;
    @Getter
    private Long personID;
    @Getter
    private boolean staff;
    @Getter
    private Long deptID;
    private String signInPwd;
    private OffsetDateTime signInPwdExpiredAt;
    @Getter @Setter
    private String oneTimePwd;

    private UserStatus status;

    @Getter
    private Set<String> bizScopes = new HashSet<>();
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * 临时账户
     * @param username
     * @param password
     * @param oneTimePassword
     * @param authorities
     */
    public UserAccount(String username, String password, boolean oneTimePassword, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        if(oneTimePassword) this.oneTimePwd = password;
        else this.signInPwd = password;
        this.authorities = authorities;
        this.status = UserStatus.NEW;
    }

    /**
     * 从数据库加载
     * @param user
     * @param oneTimePwd
     */
    public UserAccount(final User user, final String oneTimePwd){
        this.userID = user.getUserID();
        this.username = user.getUsername();
        this.avatar = user.getAvatar();
        this.telPrefix = user.getTelPrefix();
        this.mobile = user.getMobile();
        this.email = user.getEmail();
        this.personID = user.getPersonID();
        this.staff = user.isStaff();
        this.deptID = user.getDeptID();
        this.signInPwd = user.getSignInPwd();
        this.signInPwdExpiredAt = user.getSignInPwdExpiredAt();
        this.oneTimePwd = oneTimePwd;
        this.status = user.getStatus();
        if(!user.getRoles().isEmpty()){
            Set<String> roles = user.getRoles().stream()
                    .map(ur->ur.getRole().getRoleType())
                    .collect(Collectors.toSet());
            authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

            bizScopes = user.getRoles().stream()
                    .map(ur->ur.getRole().getBizScope())
                    .collect(Collectors.toSet());
        }
        else{
            if (this.staff) {
                authorities = AuthorityUtils.createAuthorityList(Roles.STAFF);
            }
            else{
                authorities = AuthorityUtils.createAuthorityList(Roles.USER);
            }
        }
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return StringUtils.hasText(oneTimePwd) ? oneTimePwd : signInPwd;
    }
    public String getOneTimePwd() {
        return oneTimePwd;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserStatus.DEACTIVATED != status;
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserStatus.LOCKED != status;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return signInPwdExpiredAt == null || signInPwdExpiredAt.isBefore(OffsetDateTime.now());
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ACTIVATED == status;
    }

    /**
     * 构建用户动态二维码字符串
     * @param splitter 分隔符
     * @return
     */
    public StringBuilder buildQrCode(String splitter){
        return new StringBuilder()
                .append(userID).append(splitter)
                .append(mobile).append(splitter)
                .append(status).append(splitter)
                .append(LocalDateTime.now().toString()
                );
    }

    /**
     * 根据二维码字符串解析出用户标识、手机和状态
     * @param splitter 分隔符
     * @param value
     * @return
     */
    public static Map<String,String> parseQrCode(String splitter, String value){
        String[] fields = value.split(splitter);
        HashMap<String,String> result = new HashMap<>();
        result.put("userID",fields[0]);
        result.put("mobile",fields[1]);
        result.put("status",fields[2]);
        result.put("timestamp",fields[3]);
        return result;
    }
}
