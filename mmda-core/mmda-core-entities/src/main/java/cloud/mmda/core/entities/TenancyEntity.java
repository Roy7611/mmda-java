package cloud.mmda.core.entities;

import cloud.mmda.core.Tenancy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 多租户实体
 * <p>
 * 分片策略（Sharding）：每个租户根据其唯一编码tenantCode自动哈希至数据库服务器节点，tenantCode具有不变性。
 * 分区策略（Partition）：同一个数据库中不同租户的tenantID唯一，采用表分区隔离和优化。
 * </p>
 * <p>
 * 多租户实体必有一个long型ID字段，作为租户数据的分区键（partitionKey）。
 * <p>
 * 64位中：<p>
 * 前28bit为int型tenantID值，因此单个数据库中可最多支持0x7FF_FFFF(134,217,727, 约1.3亿)个租户使用。
 * 后36bit为long型实际实体id，单租户最大数据量可达68,719,476,735(687亿)
 * </p>
 * 如何从long中解析出int型的租户ID见{@link Tenancy#parseTenantID(long)}
 */
public abstract class TenancyEntity<K> extends AbstractEntity<K> implements Tenancy {

    @JsonIgnore
    public abstract long getPartitionID();
    /**
     * 设置主键值
     * @param partitionID 主键
     */
    public abstract void setPartitionID(long partitionID);

    public void setTenantID(int tenantID){
        long partitionID = Tenancy.buildEntityID(tenantID,getPartitionID());
        setPartitionID(partitionID);
    }
    /**
     * 设置实体为新建
     *
     * 子类可在此函数设置创建日期等缺省值
     * @param tenantID
     */
    public void setCreated(int tenantID){
        super.setCreated();
        setTenantID(tenantID);
    }


    /**
     * 自定义Json，实现多租户自定义字段功能
     */
    @Getter
    @JsonIgnore
    private String customJson;

    /**
     * 设置自定义Json字段值
     * @param json
     */
    public void setCustomJson(String json){
        this.customJson = json;
        //空值不改变自定义属性map
        if(BaseUtil.isNullOrWhitesapce(json)) return;

        //解析为自定义属性map（仅用于前端展示）
        Map<String,Object> customMap = JsonUtil.parseJsonAsMap(json);
        if(customMap!=null){
            if(customProperties == null)
                this.customProperties = customMap;
            else
                this.customProperties.putAll(customMap);
        }
    }


    /**
     * 将自定义属性字典customProperties序列化为customJson，便于保存至数据库
     * @param excludeRefProperty 是否排除引用属性
     */
    public void serializeCustomJson(boolean excludeRefProperty){
        if(noCustomProperties()){
            this.customJson = null;
        }
        else{
            Map<String,Object> result = excludeRefProperty
                    //排除$引用属性
                    ? customProperties.entrySet().stream()
                        .filter(entry->!isRefPropName(entry.getKey()))
                        .collect(Collectors.toMap(en->en.getKey(),en->en.getValue()))
                    //全部自定义属性
                    : customProperties;
            this.customJson = JsonUtil.toJson(result);
        }
    }

    @Getter @Setter
    private List<EntityAction> actions;

    /**
     * 添加操作
     * @param a 实体操作
     */
    public void addAction(EntityAction a){
        if(actions == null) actions = new ArrayList<>();
        actions.add(a);
    }

    //region unique key

    /**
     * 设置默认的唯一键值，通常是单据号
     */
    public void setDefaultUniqueKey(){}

    //endregion
}
