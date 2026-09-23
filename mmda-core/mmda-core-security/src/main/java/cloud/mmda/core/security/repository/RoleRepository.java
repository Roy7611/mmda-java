package cloud.mmda.core.security.repository;

import cloud.mmda.core.security.models.Role;
import org.springframework.data.repository.Repository;

/**
 * 角色 Repository用于访问base.Role表中的角色数据
 */
@org.springframework.stereotype.Repository("securityRoleRepository")
public interface RoleRepository extends Repository<Role,Long> {
}
