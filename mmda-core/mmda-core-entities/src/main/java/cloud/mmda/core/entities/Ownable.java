package cloud.mmda.core.entities;

/**
 * 有当前负责人{@link #getOwnerID()}、负责部门{@link #getOwnerDeptID()}属性的实体接口
 */
public interface Ownable {
    Long getOwnerID();
    void setOwnerID(Long ownerID);
    Long getOwnerDeptID();
    void setOwnerDeptID(Long ownerDeptID);

    default void ownedBy(Long ownerID, Long ownerDeptID){
        setOwnerID(ownerID);
        setOwnerDeptID(ownerDeptID);
    }
    default void ownedByIf(Long ownerID, Long ownerDeptID){
        if(ownerID!=null) setOwnerID(ownerID);
        if(ownerDeptID!=null) setOwnerDeptID(ownerDeptID);
    }
}
