package cloud.mmda.core.entities;

import cloud.mmda.core.Tenancy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import cloud.mmda.core.utils.NameValue;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
//import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 抽象实体基类
 *
 * 所有模型应继承此类
 * 若分租户的实体需继承多租户实体{@link TenancyEntity}
 * @param <K> 主键类型
 * @remarks 计算函数移到 Computable 接口
 */
public abstract class AbstractEntity<K> implements Entity<K>, Serializable {
    /**
     * 行号，从数据库中取出时的顺序
     */
    @Getter @Setter
    private long rowNum;


    /**
     * 实体状态，指在应用中的临时修改状态，用于控制更新数据库时的行为
     */
    @Getter @Setter
    private int entityState;

    /**
     * 组装状态，关联对象、子表、引用属性和枚举属性是否已经组装。
     * 服务层可能组装业务相关的关联对象和子表，控制器组装引用属性和枚举属性文本用于显示。
     * 若服务层已经组装，则控制器层调用组装函数不会重复读取数据，提升性能。
     */
    @Getter @Setter
    private int assemblyState;

    public boolean isAssembled(int state){
        return (assemblyState & state) > 0;
    }
    public void setAssembled(int state){
        assemblyState |= state;
    }

    public void setCreated(){
        setEntityState(EntityState.CREATED);
    }
    public void setModified(){
        setEntityState(entityState|EntityState.MODIFIED);
    }
    public void setDeleted(){
        setEntityState(EntityState.DELETED);
    }
    public void resetEntityState(){ entityState = EntityState.DEFAULT;}
    /**
     * 自定义属性
     *
     * 可用于自定义字段，引用字段的文本存储{@link #getRefProperty(String)}
     */
    @Getter @Setter
    protected Map<String,Object> customProperties;

    @JsonIgnore
    public boolean noCustomProperties(){
        return customProperties==null || customProperties.isEmpty();
    }

    public Object getCustomProperty(final String propName){
        Objects.requireNonNull(propName);
        if(customProperties==null) return null;
        return customProperties.get(propName);
    }

    public void setCustomProperty(final String propName, final Object value){
        Objects.requireNonNull(propName);
        if(customProperties==null) customProperties = new HashMap<>();
        customProperties.put(propName, value);
    }
    public void setCustomProperty(final NameValue<String,Object> property){
        setCustomProperty(property.getName(),property.getValue());
    }
    /**
     * 获取引用字段的显示文本
     *
     * 引用字段例如partnerID有REF(partnerID,partnerName)，
     * 传给客户端需显示partnerName，字典里$partnerID=>客户名称
     * @param colName 字段名称，如partnerID
     * @return 返回partName
     */
    public String getRefProperty(String colName){
        return (String) getCustomProperty(getRefPropName(colName));
    }
    public void setRefProperty(String colName, String value){
        setCustomProperty(getRefPropName(colName),value);
    }
    public void setRefProperty(NameValue<String,String> property){
        setRefProperty(property.getName(),property.getValue());
    }
    protected final String getRefPropName(String propName){
        return "$"+propName;
    }
    protected final boolean isRefPropName(String propName){
        return propName.charAt(0)=='$';
    }

}
