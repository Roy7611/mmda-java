package cloud.mmda.core.security.repository;

import cloud.mmda.core.security.models.RoleModuleAuth;
import cloud.mmda.core.security.models.User;
import cloud.mmda.core.security.UserAccountService;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户仓储可按用户名称（username）、邮箱（email）、手机号（mobile）和第三方开放标识（OpenID）查找用户。
 * <p>
 *     It is a Jpa repository used by {@link UserAccountService} in order to load a user.
 * </p>
 *
 * @author Roy Luo
 * @since 4.0
 * @remarks
 * 如果要全部增删改查、分页排序，请扩展{@link JpaRepository}或继承{@link SimpleJpaRepository}
 */
@org.springframework.stereotype.Repository("securityUserRepository")
public interface UserRepository extends Repository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByMobile(String mobile);
    Optional<User> findByMobileAndTelPrefix(String mobile, String telPrefix);

    @Query(value = "select * from mmda_base.User u where u.userID = (select userID from mmda_base.UserOpenIdentity where openIDType=?1 and openID=?2) ", nativeQuery = true)
    Optional<User> findByOpenIdentity(String openIdType, String openID);

    Optional<User> findByUserID(Long userID);
}
