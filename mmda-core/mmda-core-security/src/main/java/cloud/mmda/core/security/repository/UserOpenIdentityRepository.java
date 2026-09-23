package cloud.mmda.core.security.repository;

import cloud.mmda.core.security.models.UserOpenIdentity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("securityUserOpenIdentityRepository")
public interface UserOpenIdentityRepository extends ListCrudRepository<UserOpenIdentity,UserOpenIdentity.Key> {
    List<UserOpenIdentity> findAllByUserID(long userID);

}
