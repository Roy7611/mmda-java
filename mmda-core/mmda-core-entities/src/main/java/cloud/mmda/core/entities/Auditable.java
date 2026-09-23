package cloud.mmda.core.entities;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 可审计追踪的实体接口，包含创建人、创建部门、创建日期、修改人、修改日期以及createBy,modifyBy函数接口
 */
public interface Auditable {
    Long getCreatorID();
    void setCreatorID(Long creatorID);
    Long getDeptID();
    void setDeptID(Long deptID);
    Timestamp getCreateDate();
    void setCreateDate(Timestamp createDate);

    Long getLastModifierID();
    void setLastModifierID(Long lastModifierID);
    Timestamp getLastModified();
    void setLastModified(Timestamp lastModified);

    default void createBy(Long creatorID, Long deptID){
        setCreatorID(creatorID);
        setDeptID(deptID);
        setCreateDate(Timestamp.valueOf(LocalDateTime.now()));
    }
    default void modifyBy(Long modifierID){
        setLastModifierID(modifierID);
        setLastModified(Timestamp.valueOf(LocalDateTime.now()));
    }
}
