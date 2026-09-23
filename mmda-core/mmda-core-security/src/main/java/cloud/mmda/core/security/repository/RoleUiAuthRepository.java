package cloud.mmda.core.security.repository;

import cloud.mmda.core.security.models.RoleUiAuth;
import org.springframework.data.repository.Repository;

import java.util.List;

@org.springframework.stereotype.Repository("securityRoleUiAuthRepository")
public interface RoleUiAuthRepository extends Repository<RoleUiAuth,RoleUiAuth.Key> {
    List<RoleUiAuth> findAllByRoleID(long roleID);
}
