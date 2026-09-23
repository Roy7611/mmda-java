package cloud.mmda.core.security;

import cloud.mmda.core.security.models.User;
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
@org.springframework.stereotype.Repository("serviceUserRepository")
public interface UserRepository extends Repository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByMobile(String mobile);
    Optional<User> findByMobileAndTelPrefix(String mobile, String telPrefix);

    @Query(value = "select * from User u where u.userID = (select userID from UserOpenIdentity where openIDType=?1 and openID=?2) ", nativeQuery = true)
    Optional<User> findByOpenIdentity(String openIdType, String openId);

    Optional<User> findByUserID(long userId);

    @Query(value = "SELECT u.* FROM mmda_base.User u WHERE u.`status`>=?1 AND (u.userID between ?2 AND ?3 ) AND u.userID IN (SELECT r.userID FROM mmda_base.RoleModuleAuth auth INNER JOIN mmda_base.UserRole r ON auth.roleID=r.roleID WHERE auth.moduleCode=?4 AND auth.allowRead=TRUE ) ORDER BY u.deptID ASC ,u.userName ASC ", nativeQuery = true)
    List<User> findUsersByModulePermission(short status,long minID,long maxID, String moduleCode);

    @Query(value = "SELECT *  FROM mmda_base.User u WHERE u.`status` >= ?1  AND (u.userID between ?2 AND ?3 )  AND u.userID IN ( " +
            "    SELECT r.userID    FROM mmda_base.RoleModuleAuth auth   INNER JOIN mmda_base.UserRole r  ON auth.roleID = r.roleID  " +
            "    WHERE auth.moduleCode = ?4   AND auth.authActions REGEXP ?5   )   ORDER BY u.deptID ASC ,u.userName ASC ", nativeQuery = true)
    List<User> findUsersByModuleActionPermission(short status,long minID,long maxID,String moduleCode, String actionNames);

    @Query(value = "SELECT d.deptID, d.deptName FROM mmda_base.Department d WHERE  d.deptID between ?1 AND ?2 ", nativeQuery = true)
    List<Object[]> findDepartments(long minID,long maxID);

}
