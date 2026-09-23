package cloud.mmda.core.security.repository;

import cloud.mmda.core.security.models.RoleModuleAuth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

@org.springframework.stereotype.Repository("securityRoleModuleAuthRepository")
public interface RoleModuleAuthRepository extends Repository<RoleModuleAuth,RoleModuleAuth.Key> {
    List<RoleModuleAuth> findAllByRoleID(long roleID);

    @Query(value = "select * from mmda_base.RoleModuleAuth a where a.roleID in (select roleID from mmda_base.UserRole where userID=?1) order by a.moduleCode", nativeQuery = true)
    List<RoleModuleAuth> findAllByUserID(long userID);

    @Query(value = "select * from mmda_base.RoleModuleAuth a where a.allowRead=1 and a.moduleCode like ?2 and  a.roleID in (select roleID from mmda_base.UserRole where userID=?1) order by a.moduleCode", nativeQuery = true)
    List<RoleModuleAuth> findReadableModules(long userID, String moduleCode);
}
