package cloud.mmda.core.entities;

/**
 * 多租户的组合键实体，一般是主实体{@link MasterTenancyEntity}的子表
 * @param <K> 主键类型
 */
public abstract class CompositeTenancyEntity<K extends CompositeTenancyKey> extends TenancyEntity<K> {
//    @Override
//    public long getPartitionID() {
//        return getId().getPartitionID();
//    }
}
