package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.DisplayShape;
import cloud.mmda.core.enums.FetchMode;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.utils.NameValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 元关系
 * @author roshion
 *
 * 定义类之间的关系，可与数据库存储层不一样，但不违反。
 * 2023.10.04 增加defaultFilter,defaultPageSize,defaultSort，定义默认载入数据条数和默认的排序，对于像设备日志这样的子表有用
 * 2025.09.11 REF_ONE 表示一次性引用，用于无缓存字典也无需HAS_ONE的情况
 */
public class MetaRelation extends MetaEntity {

    public static final String INNER_JOIN = " INNER JOIN ";
    public static final String LEFT_JOIN = " LEFT JOIN ";
    public static final String RIGHT_JOIN = " RIGHT JOIN ";
    public static final String FULL_JOIN = " FULL JOIN ";
    public static final String CROSS_JOIN = " CROSS JOIN ";

    //region 属性

    /**
     * 数据库名称
     */
    @NotBlank
    @Size(min = 1, max = 30)
    @Getter @Setter
    private String dbSchema;

    /**
     * 元对象名称
     */
    @NotBlank
    @Size(min = 1, max = 30)
    @Getter @Setter
    private String objName;

    /**
     * 关系名称，与类的导航属性一致
     */
    @NotBlank
    @Size(min = 1, max = 30)
    @Getter @Setter
    private String relationName;

    /**
     * 关系类型：NONE,HAS_ONE,HAS_MANY,REF
     */
    @NotNull
    @Min(0)
    @Getter @Setter
    private MetaRelationType relationType;

    /**
     * 关系顺序号
     */
    @Getter @Setter
    private Integer relationIdx;
    /**
     * 显示标签
     */
    @Size(max = 30)
    @Getter @Setter
    private String displayLabel;
    /**
     * 关联元对象数据库模式
     */
    @NotNull
    @Size(min = 1, max = 30)
    @Getter @Setter
    private String relativeDbSchema;
    /**
     * 关联元对象名称
     */
    @NotBlank
    @Size(min = 1, max = 30)
    @Getter @Setter
    private String relativeObjName;
	/**
	 * 显示形状：0;LIST;列表|1;TREE;树形|2;HIERARCHY;层次
	 */
	@NotNull
	@Getter @Setter
	private DisplayShape displayShape;
	/**
	 * 形状键，树形的父亲字段如parentID，层次的编码字段例如taskCode
	 */
	@NotBlank
	@Size(min=1,max=30)
	@Getter @Setter
	private String shapeKey;
    /**
     * 只读否
     */
    @NotNull
    @Getter @Setter
    private boolean readOnly;

    /**
     * 连接条件，形式为messageID=@messageID
     * has one 可以生成join 语句后一并取出
     */
    @Size(max = 255)
    @Getter @Setter
    private String joinOn;

    /**
     * 可能拥有此关系的条件
     */
    @Size(max = 255)
    @Getter @Setter
    private String canHave;

    /**
     * 要求子表必须有至少一个元素
     */
    @Getter @Setter
    private boolean requiredAny;

    /**
     * 子表项次字段，自动+1
     */
	@Size(max=30)
    @Getter @Setter
    private String sequenceKey;
	/**
	 * 次要组
	 */
	@Getter @Setter
	private Boolean secondary;
	/**
	 * 加载模式：0;EAGER;急加载|0;LAZY;懒加载
	 */
	@NotNull
	@Getter @Setter
	private FetchMode fetchMode;
	@Size(max=255)
    @Getter @Setter
    private String defaultFilter;
    @Size(max=100)
    @Getter @Setter
    private String defaultGroupBy;

    @Getter @Setter
    private Integer defaultPageSize;

	@Size(max=100)
    @Getter @Setter
    private String defaultSort;
	/**
	 * 聚合设置。例如SUM(fld1),COUNT(fld2)
	 */
	@Size(max=255)
	@Getter @Setter
	private String aggregates;
    //endregion

    /**
     * inner / left / right join
     */
    @Getter @Setter
    private String joinType;
    @Getter @Setter
    private String valueColName;
    @Getter @Setter
    private String labelColName;
    @Getter @Setter
    private String labelColExtra;//额外的显示字段，与labelColName空格拼接
    @Getter @Setter
    private String groupColName;

    /**
     * 缓存查询语句
     */
    @JsonIgnore
    @Getter @Setter
    private String sql;

    /**
     * has one 时候解析enumSet得到,
     * has many 时使用joinOn
     */
    @Getter @Setter
    private String where;

    /**
     * 全称
     * @return
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getFullName(){
        return String.join(".", dbSchema,objName,relationName);
    }

    /**
     * 显示标签字段sql表达式
     * @return
     */
    @JsonIgnore
    public String getLabelColExp(){
        return labelColExtra == null
                ? labelColName
                : "CONCAT_WS(' ',"+labelColName+","+labelColExtra+") AS " + labelColName;//解决 labelColExtra 多参数 拼接失败问题
                //: "CONCAT("+labelColName+",' ',"+labelColExtra+") AS " + labelColName;
    }
    @JsonIgnore
    public String getFullRelativeObjName(){
        if(relativeDbSchema ==null || relativeObjName.equals(dbSchema)) return relativeObjName;
        return relativeDbSchema +"."+relativeObjName;
    }
    //////////////////////////////////////////////////////////////////////////
    // Hash code and equals
    //////////////////////////////////////////////////////////////////////////

    @Override
    public int hashCode() {
        return Objects.hash(dbSchema, objName, relationName);
    }

    @Override
    public boolean equals(Object obj) {
        if(super.equals(obj)) return true;
        if(obj instanceof MetaRelation metaRelation){
            return relationName.equals(metaRelation.getRelationName())
                    && objName.equals(metaRelation.getObjName())
                    && dbSchema.equals(metaRelation.getDbSchema());
        }
        return false;
    }


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

    /**
     * 一次性（组装不采用缓存）
     */
    @NotNull
    @Getter @Setter
    private boolean oneTime;

    @JsonIgnore
    public boolean hasOneOrRef(){
        return relationType.hasOneOrRef();
    }
    @JsonIgnore
    public boolean hasOneOrMany(){
        return relationType.hasOneOrMany();
    }

    public Map<String,String> buildJoinMap(){
        return parseJoinOnToMap(this.joinOn);
    }

    /**
     * 将表关系联接表达式 {@code partnerID=@partnerID and itemID=@itemID} 解析为
     * <pre>
     *     {@code
     *      {
     *          {"partnerID", "partnerID"}
     *          {"itemID", "itemID"}
     *      }
     *     }
     * </pre>
     * @param joinOn
     * @return
     */
    public static Map<String,String> parseJoinOnToMap(String joinOn){
        Objects.requireNonNull(joinOn);
        var joinMap = new LinkedHashMap<String, String>();
        if(joinOn.indexOf(" AND ") != -1 || joinOn.indexOf(" and ") != -1){
            var conditions = joinOn.split("\s+[AND|and]\s+");
            for(var condition : conditions){
                var fields = condition.replaceAll(" ","").split("=@");
                joinMap.put(fields[0],fields[1]);
            }
        }
        else if(joinOn.indexOf(" OR ") != -1 || joinOn.indexOf(" or ") != -1){
            var conditions = joinOn.split("\s+[OR|or]\s+");
            for(var condition : conditions){
                var fields = condition.replaceAll(" ","").split("=@");
                joinMap.put(fields[0],fields[1]);
            }
        }
        return joinMap;
    }

    public static class Builder{
        private MetaObject.Builder objBuilder;
        private MetaRelation metaRelation;
        public MetaRelation get(){
            return metaRelation;
        }
        public Builder(MetaObject.Builder objBuilder){
            this.objBuilder = objBuilder;
            var metaObj = objBuilder.get();
            metaRelation = new MetaRelation();
            metaRelation.setDbSchema(metaObj.getDbSchema());
            metaRelation.setObjName(metaObj.getObjName());
            metaRelation.setRelativeDbSchema(metaObj.getDbSchema());
        }

        public Builder withMany(String name){
            metaRelation.setRelationName(name);
            metaRelation.setRelationType(MetaRelationType.HAS_MANY);
            return this;
        }
        public Builder withOne(String name){
            metaRelation.setRelationName(name);
            metaRelation.setRelationType(MetaRelationType.HAS_ONE);
            return this;
        }
        public Builder join(String joinType, String relativeObjName, String joinOn){
            metaRelation.setRelativeObjName(relativeObjName);
            if(joinOn.indexOf('@')==-1 && joinOn.indexOf('=')==-1){
                metaRelation.setJoinOn(joinOn+"=@"+joinOn);
            }
            else{
                metaRelation.setJoinOn(joinOn);
            }
            metaRelation.setJoinType(joinType);
            return this;
        }
        public Builder join(String joinType, MetaObject relativeObj, String joinOn){
            metaRelation.setRelativeDbSchema(relativeObj.getDbSchema());
            metaRelation.setRelativeObjName(relativeObj.getObjName());
            if(joinOn.indexOf('@')==-1 && joinOn.indexOf('=')==-1){
                metaRelation.setJoinOn(joinOn+"=@"+joinOn);
            }
            else{
                metaRelation.setJoinOn(joinOn);
            }
            metaRelation.setJoinType(joinType);
            return this;
        }
        public Builder leftJoin(String relativeObjName, String joinOn){
            join(LEFT_JOIN, relativeObjName, joinOn);
            return this;
        }
        public Builder leftJoin(MetaObject relativeObj, String joinOn){
            join(LEFT_JOIN, relativeObj, joinOn);
            return this;
        }
        public Builder rightJoin(String relativeObjName, String joinOn){
            join(RIGHT_JOIN, relativeObjName, joinOn);
            return this;
        }
        public Builder rightJoin(MetaObject relativeObj, String joinOn){
            join(RIGHT_JOIN, relativeObj, joinOn);
            return this;
        }
        public Builder fullJoin(String relativeObjName, String joinOn){
            join(FULL_JOIN, relativeObjName, joinOn);
            return this;
        }
        public Builder fullJoin(MetaObject relativeObj, String joinOn){
            join(FULL_JOIN, relativeObj, joinOn);
            return this;
        }
        public Builder crossJoin(String relativeObjName, String joinOn){
            join(CROSS_JOIN, relativeObjName, joinOn);
            return this;
        }
        public Builder crossJoin(MetaObject relativeObj, String joinOn){
            join(CROSS_JOIN, relativeObj, joinOn);
            return this;
        }
        public MetaObject.Builder add(){
            objBuilder.addRelation(metaRelation);
            return objBuilder;
        }
    }
}
