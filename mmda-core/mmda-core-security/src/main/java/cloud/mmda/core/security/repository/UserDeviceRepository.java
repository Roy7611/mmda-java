package cloud.mmda.core.security.repository;

import cloud.mmda.core.security.models.UserDevice;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户设备 Repository 用于增删改查一个User的终端设备信息。
 */
@Repository("securityUserDeviceRepository")
public interface UserDeviceRepository extends ListCrudRepository<UserDevice,String> {
    List<UserDevice> findAllByUserID(long userID);
}
