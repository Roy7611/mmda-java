package cloud.mmda.core.security.repository;

import cloud.mmda.core.security.models.RoleDataAuth;
import org.springframework.data.repository.Repository;

import java.util.List;

@org.springframework.stereotype.Repository("securityRoleDataAuthRepository")
public interface RoleDataAuthRepository extends Repository<RoleDataAuth,RoleDataAuth.Key> {
    List<RoleDataAuth> findAllByRoleID(long roleID);
}
