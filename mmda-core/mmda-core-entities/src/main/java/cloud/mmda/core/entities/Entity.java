package cloud.mmda.core.entities;

import cloud.mmda.core.Tenancy;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * 实体接口
 *
 * 指数据库表中的一行，有行号、实体状态及根据数据控制增删改权限
 * @param <K> 主键类型
 */
public interface Entity<K> extends SequencedRow {
    //组装类型
    int ASSEMBLE_NONE = 0;
    int ASSEMBLE_ONE = 1; //一对一关联对象
    int ASSEMBLE_MANY = 2;//一对多子表
    int ASSEMBLE_REF = 4; //引用属性
    int ASSEMBLE_ENUM = 8;//枚举属性


    /**
     * 返回主键 id
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    K getId();

    /**
     * 返回主键 id 类，避免使用反射获得实体主键类
     */
    @JsonIgnore
    Class<K> getIdClass();

    /**
     * 是否允许编辑
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    default boolean isEditable(){ return true; };

    /**
     * 是否允许删除
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    default boolean isDeletable(){ return true; };

    /**
     * 是否可打印
     * @return
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    default boolean isPrintable(){ return true; };

    /**
     * 实体状态，参考{@link EntityState}
     */
    int getEntityState();
    void setEntityState(int state);

    @JsonIgnore
    default boolean isCreated(){
        return EntityState.isCreated(getEntityState());
    }
    @JsonIgnore
    default boolean isModified(){
        return EntityState.isModified(getEntityState());
    }
    @JsonIgnore
    default boolean isDeleted(){
        return EntityState.isDeleted(getEntityState());
    }

    String getRefProperty(String propName);
    void setRefProperty(String propName, String value);

    //关联对象和属性的组装状态
    boolean isAssembled(int state);
    void setAssembled(int state);
    //行号
    long getRowNum();
    void setRowNum(long rowNum);

    /**
     * 判断集合是否不为空
     * @param tCollection 集合
     * @return 至少有一个元素返回true，否则返回false
     * @param <T> 元素类型
     */
    default <T> boolean hasAny(Collection<T> tCollection){
        return tCollection!=null && !tCollection.isEmpty();
    }

    //region 单据号
    SimpleDateFormat _df = new SimpleDateFormat("yyyyMMdd");

    /**
     * 生成前缀(prefix)+日期(yyyyMMdd)+序列号(000n)的租户内唯一单据号，仅在租户未设置编码规则默认使用
     * @param prefix 前缀，例如 "SO"
     * @param date 日期，例如 "2026-05-16"
     * @param id 实体 id, 例如 8388609
     * @return 单据号，SO202605168609
     * @see #generateNo(String, long)
     */
    default String generateNo(String prefix, Date date, long id){
        String serialNo = toSequenceNo(id, 4);
        return String.join("",prefix, _df.format(date), serialNo);
    }

    /**
     * 生成前缀(prefix)+序列号(000n)的租户内唯一单据号
     *
     * @param prefix 前缀，如 "E"
     * @param id 实体对象唯一id
     * @return 单据号，E0001
     * @see #generateNo(String, Date, long) 
     */
    default String generateNo(String prefix, long id){
        return prefix + toSequenceNo(id, 4);
    }

    /**
     * 用 id 生成单据序列号，截取右边 len 位，不足的添加前导 0
     * @param id 主键 id
     * @param len 要生成几位数字
     * @return 序列号，例如 “0001”
     */
    static String toSequenceNo(long id, int len){
        var no = Strings.padStart(Long.toString(id), len, '0');
        return no.substring(Math.max(0, no.length() - len));//right(s, len)
    }
    //endregion of 单据号
}
