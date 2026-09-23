package cloud.mmda.core;

import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.utils.BaseUtil;
import com.google.common.base.Strings;

import java.util.Objects;

/**
 * 多租户实体接口
 * <p>
 * 多租户实体 {@code TenancyEntity} 实现此接口，若存在组合键，组合键也实现此接口
 */
public interface Tenancy {
    int NO_TENANT_ID = 0;
    int MIN_TENANT_ID = 1;
    int MAX_TENANT_ID = 0x7FF_FFFF;//高28位是租户id

    long MAX_REAL_ID = 0xF_FFFF_FFFFL;//低36位是实际id
    /**
     * 获取当前实体的租户id
     * @return
     */
    default int getTenantID(){ return parseTenantID(getPartitionID());}

//    /**
//     * 设置实体的租户id，会改变主键id的高28位
//     * 通常在Controller层中根据Request上下文设置。Controller可根据域名识别未登录用户所属租户，
//     * 或者根据已登录用户的id也可解析出租户id，参见{@link TenancyEntity#parseTenantID(long)}
//
//     * @param tenantID
//     */
//    default void setTenantID(int tenantID){ setPartitionID(buildEntityID(tenantID,getPartitionID()));}

    long getPartitionID();
//    void setPartitionID(long partitionID);
    /**
     * 从一个长整型分片id中解析出租户id
     * @param partitionID
     * @return 租户id
     */
    static int parseTenantID(long partitionID){
        return (int)(partitionID>>>36);
    }

    /**
     * 存货条目会使用
     * @param entry
     * @return
     */
    static int parseTenantID(String entry){
        String[] segments = entry.split("\\.");
        long partitionId = Long.parseLong(segments[0]);
        return Tenancy.parseTenantID(partitionId);
    }
    /**
     * 从未知的主键类型中解析出租户id
     * @param k 主键值
     * @param <K> 主键类型，可能是long / Tenancy
     * @return
     */
    static <K> int toTenantID(K k){
        Objects.requireNonNull(k);

        if(k instanceof Long){
            return Tenancy.parseTenantID((Long)k);
        }
        else if(k instanceof Tenancy){
            return ((Tenancy)k).getTenantID();
        }
        else if(k instanceof  String){
            return Tenancy.parseTenantID((String)k);
        }
        else {
            return (int)k;
        }
    }

    /**
     * 分布式唯一id生成后合成租户关联的实体id
     * @param tenantID 当前租户id
     * @param id 生成后48bits实际的id
     * @return 实体id
     */
    static long buildEntityID(int tenantID, long id){
        long entityID = tenantID & MAX_TENANT_ID;
        return (entityID<<36) + (id & MAX_REAL_ID);
    }

    /**
     * 获取实际的ID，排除租户ID
     * @param id
     * @return
     */
    static long getRealID(long id){
        return id & MAX_REAL_ID;
    }
    static long getMinID(long id){
        return id & (~MAX_REAL_ID);
    }
    static long getMaxID(long id){
        return id | MAX_REAL_ID;
    }

    static boolean isSameTenant(long a, long b){
        return getMinID(a) == getMinID(b);
    }


    /**
     * 获取某个租户当前实体最小id
     *
     * 可考虑缓存优化
     * @param tenantID
     * @return
     */
    static long getMinEntityID(int tenantID, MetaObject metaObj){
        return buildEntityID(tenantID, BaseUtil.getValueOrZero(metaObj.getMinID()));
    }

    /**
     * 获取某个租户当前实体最大id
     *
     * 可考虑缓存优化
     * @param tenantID
     * @return
     */
    static long getMaxEntityID(int tenantID, MetaObject metaObj){
        return buildEntityID(tenantID, BaseUtil.getValueOrDefault(metaObj.getMaxID(),Tenancy.MAX_REAL_ID));
    }
}
