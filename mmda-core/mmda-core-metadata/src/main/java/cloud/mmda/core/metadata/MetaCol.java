package cloud.mmda.core.metadata;

import cloud.mmda.core.data.conversion.DataTypeHandler;
import cloud.mmda.core.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import cloud.mmda.core.utils.NameValue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 元列
 *
 * @remarks
 * 数据库中字段的定义，对应类中的属性元数据。
 * 可用于存储层自动生成SQL语句。
 **/
public class MetaCol extends MetaEntity {
    /**
     * 数据库名称或者模式
     */
    @NotBlank
    @Size(min = 1, max = 30)
    @Getter @Setter
    private String dbSchema;

    /**
     * 对象名称
     */
    @NotBlank
    @Size(min = 1, max = 30)
    @Getter @Setter
    private String objName;

    /**
     * 字段名称
     */
    @NotBlank
    @Size(min = 1, max = 30)
    @Getter @Setter
    private String colName;

    /**
     * 是否主键
     */
    @NotNull
    @Getter @Setter
    private boolean isKey;

    /**
     * 是否生成，数据库层定义了自增
     */
    @NotNull
    @Getter @Setter
    private boolean isGenerated;

    /**
     * 在表或视图中定义的顺序
     */
    @NotNull
    @Min(0)
    @Getter @Setter
    private int colIdx;
    /**
     * 分组名称，如：p1.订单信息，s2.物流信息，s9.概要信息
     * p代表primary 放主要区域，s代表summary 放右边栏次要和辅助位置
     */
    @Size(max = 30)
    @Getter @Setter
    private String groupLabel;

    /**
     * 默认显示标签，将被特定租户或多语言版本覆盖
     */
    @Size(max = 30)
    @Getter @Setter
    private String displayLabel;
    /**
     * 合并标签，多个字段连接在一起显示
     */
    @Size(max = 30)
    @Getter @Setter
    private String mergeLabel;
    /**
     * 合并前缀，例如换行、空格
     */
    @Size(max = 30)
    @Getter @Setter
    private String mergePrefix;

    /**
     * 数据类型，参考{@link MetaDataType}
     */
    @NotNull
    @Min(0)
    @Getter @Setter
    private DataType dataType;

    /**
     * 是否无符号，用于整型
     */
    @NotNull
    @Getter @Setter
    private boolean isUnsigned;

    /**
     * 字符串最大长度，用于CHAR/VARCHAR
     */
    @Min(0)
    @Getter @Setter
    private Integer maxLength;

    /**
     * 数值精度
     */
    @Min(0)
    @Getter @Setter
    private Integer numericPrecision;

    /**
     * 小数位数
     */
    @Min(0)
    @Getter @Setter
    private Integer numericScale;

    /**
     * 是否可为空值
     */
    @NotNull
    @Getter @Setter
    private boolean nullable;

    /**
     * 是否计算字段，计算字段不能UPDATE
     */
    @NotNull
    @Getter @Setter
    private boolean computed;

    /**
     * 计算公式，仅当computed=true有效
     */
    @Size(max=255)
    @Getter @Setter
    private String formula;

    /**
     * 约束，例如greaterThan(0)
     */
    @Size(max=255)
    @Getter @Setter
    private String constraint;
    /**
     * 缺省值
     */
    @Size(max = 255)
    @Getter @Setter
    private String defaultVal;

    /**
     * 扩展类型，参见{@link MetaObject#getExtendType()}。
     * 表示这个字段是继承或扩展属性
     */
    @NotNull
    @Min(0)
    @Getter @Setter
    private ExtensionType extendType;

    /**
     * 枚举设置
     * 1.常量：0;NONE;无|1;APPROVED;已批准
     * 2.关系：HAS_ONE Partner(partnerID,partnerName)
     * 3.引用：REF Partner(partnerID,partnerName)
     */
    @Size(max = 255)
    @Getter @Setter
    private String enumSet;

    /**
     * 若为空字符串则设为null
     */
    @NotNull
    @Getter @Setter
    private boolean nullIfEmpty;

    /**
     * 是否可过滤，通常此字段是一个索引中的首个字段
     */
    @Getter @Setter
    private boolean filterable;
    /**
     * 域名称，参见 {@link MetaUiField#getFieldName()}
     */
    @Size(max = 30)
    @Getter @Setter
    private String fieldName;
    /**
     * 列出，桌面端列表显示
     */
    @NotNull
    @Getter @Setter
    private boolean listed;
    /**
     * 聚合设置：0;NONE;无|1;COUNT;计数|2;SUM;求和|4;AVG;平均|8;MIN;最小值|16;MAX;最大值
     */
    @NotNull
    @Getter @Setter
    @JsonSerialize(converter = EnumBitSet.ToIntConverter.class)
    @JsonDeserialize(converter = EnumBitSet.FromIntConverter.class)
    private EnumBitSet<FieldAggregation> aggregationSet;
    /**
     * 只读
     */
    @NotNull
    @Getter @Setter
    private boolean readOnly;
    /**
     * 隐藏
     */
    @NotNull
    @Getter @Setter
    private boolean hidden;
    /**
     * 描述
     */
    @Size(max = 255)
    @Getter @Setter
    private String description;

    //region 枚举和默认值解析
    /**
     * 外键关系类型
     *
     * 从enumSet解析获得
     */
    @Getter @Setter
    private MetaRelationType relationType = MetaRelationType.NONE;

    /**
     * 外键关系名称
     *
     * 从enumSet解析获得
     */
    @Getter @Setter
    private String relationName;

    /**
     * 解析后的类型化缺省值
     */
    @Getter @Setter
    private Object parsedDefaultVal;

    /**
     * 枚举类名，载入后解析出来
     */
    @Getter @Setter
    private String enumClass;

    /**
     * 是否按位多选的枚举集（enumSet=ENUMS enumClass）
     */
    @Getter @Setter
    private boolean bitSet;

    @JsonIgnore
    @Getter @Setter
    private MetaEnum metaEnum;

    /**
     * 是否真正的枚举数据类型
     * @return
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean isEnumType(){
        return relationType == MetaRelationType.ENUM && metaEnum!=null;
    }

    /**
     * 解析后的枚举选项
     * 0->{NONE,无}
     */
    @JsonIgnore
    @Deprecated(since = "metaEnum.getParsedEnumMap即将去除")
    public Map<String, NameValue<String,String>> getEnumMap(){
        if(metaEnum==null) return null;
        return metaEnum.getParsedEnumMap();
    }
    //endregion of 枚举

    //region 实体属性访问器
//    /**
//     * 属性存取器
//     */
//    @JsonIgnore
//    @Getter @Setter
//    @Deprecated
//    private MetaPropAccess access;

    @JsonIgnore
    @Getter @Setter
    private MetaGetterAccess accessGetter;

    @JsonIgnore
    @Getter @Setter
    private MetaSetterAccess accessSetter;

    private DataTypeHandler<?> customDataTypeHandler;

    /**
     * 从数据库结果集中读取字段值，若是枚举自动转换
     * @param rs 结果集
     * @return 值对象
     * @throws SQLException
     */
    public Object getResult(ResultSet rs) throws SQLException {
        var result = dataType.getDefaultHandler().getResult(colName, rs, nullable);
        //枚举值转换
        if(isEnumType() && result != null){
            var enumType = accessGetter.type();
            if(isBitSet()){
                return EnumBitSet.valueOf(enumType, (int)result);
            }
            else{
                //枚举类型自动转换为 Enum
                return Enum.valueOf(enumType, metaEnum.nameOf((int)result));
            }
        }
        return result;
    }

    /**
     * 设置参数值到可执行语句
     * @param ps 执行语句
     * @param paramIndex 参数索引
     * @param value 参数值
     * @throws SQLException
     */
    public void setParameter(PreparedStatement ps, int paramIndex, Object value) throws SQLException {
        if(value == null){
            ps.setNull(paramIndex, dataType.getJdbcType());
        }
        else if(isEnumType()){//枚举值转换
            if(value instanceof EnumBitSet<?> enumBitSet){
                //如果是枚举集合
                var underlyingValue = enumBitSet.getValue();
                ps.setInt(paramIndex, underlyingValue);
            }
            else if(value instanceof EnumValue<?> enumValue){
                //枚举值类型转换为底层数值
                ps.setObject(paramIndex, enumValue.getValue(), dataType.getJdbcType());
            }
            else{
                //普通枚举通过元数据转换
                var underlyingVal = metaEnum.valueOf(value.toString());
                ps.setObject(paramIndex, underlyingVal, dataType.getJdbcType());
            }
        }
        else{
            ps.setObject(paramIndex, value, dataType.getJdbcType());
        }
    }
    //endregion of 实体属性访问器

    /**
     * 元列构建器，用来临时构建元视图{@link MetaView}对象或者计算列
     */
    public static class Builder{
        private final MetaObject.Builder objBuilder;
        private MetaCol metaCol;

        public MetaCol get(){
            return metaCol;
        }
        public Builder(MetaObject.Builder objBuilder){
            this.objBuilder = objBuilder;
            var metaObj = objBuilder.get();
            metaCol = new MetaCol();
            metaCol.setDbSchema(metaObj.getDbSchema());
            metaCol.setObjName(metaObj.getObjName());
            metaCol.setNullable(true);
        }

        public Builder withName(String colName){
            metaCol.setColName(colName);
            return this;
        }
        public Builder withName(String colName, String displayLabel){
            metaCol.setColName(colName);
            metaCol.setDisplayLabel(displayLabel);
            return this;
        }
        public Builder withKey(String colName){
            metaCol.setColName(colName);
            metaCol.setKey(true);
            return this;
        }
        public Builder withKey(String colName, String displayLabel){
            metaCol.setColName(colName);
            metaCol.setDisplayLabel(displayLabel);
            metaCol.setKey(true);
            return this;
        }
        public Builder withType(DataType dataType, Integer maxLength)
        {
            metaCol.dataType = dataType;
            metaCol.maxLength = maxLength;
            return this;
        }
        public Builder withType(DataType dataType, Integer maxLength, boolean nullable)
        {
            metaCol.dataType = dataType;
            metaCol.maxLength = maxLength;
            metaCol.nullable = metaCol.isKey ? false : nullable;
            return this;
        }
        public Builder withType(DataType dataType, Integer maxLength, Integer precision, Integer scale, boolean nullable)
        {
            metaCol.dataType = dataType;
            metaCol.maxLength = maxLength;
            metaCol.numericPrecision = precision;
            metaCol.numericScale = scale;
            metaCol.nullable = metaCol.isKey ? false : nullable;
            return this;
        }

        public Builder withEnum(String enumSet)
        {
            metaCol.enumSet = enumSet;
            return this;
        }
        public Builder withDefaultValue(String defVal)
        {
            metaCol.defaultVal = defVal;
            return this;
        }
        public Builder withComputed(String formula)
        {
            if(formula==null || formula.isEmpty()) return this;
            metaCol.formula = formula;
            metaCol.computed = true;
            return this;
        }

        public Builder withComment(String comment)
        {
            metaCol.description = comment;
            return this;
        }
        public MetaObject.Builder add()
        {
            objBuilder.addCol(metaCol);
            return objBuilder;
        }
    }

}
