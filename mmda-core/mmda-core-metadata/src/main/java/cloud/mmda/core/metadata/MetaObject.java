package cloud.mmda.core.metadata;


import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.enums.ExtensionType;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.utils.BaseUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 元对象
 *
 * @author roshion 2020.3.26
 * @remarks
 * 数据库中的表、视图定义。相比数据库原生的scheme info扩展了支持面向对象的继承关系的属性，
 * 以及多租户、数据分片机制的属性。多租户数据库分片存储，根据租户ID哈希计算是哪个数据库在
 * 哪个节点上。同个节点中的不同租户数据采用表分区优化数据存储。
 * <p>
 * 要求在同一个节点（数据库服务器）上的不同数据库中数据表名唯一，例如：db1和db2中不能同时存在表t1
 * 完全可以使用跨数据库查询，无需这种冗余，否则会带来数据一致性维护工作的麻烦
 */
public class MetaObject extends MetaEntity implements MetaContext {
    //region 枚举
    //对象类型：T表，V视图，P存储过程，F函数
    public static final String TABLE = "T";
    public static final String VIEW = "V";
    public static final String PROC = "P";
    public static final String FUNC = "F";
    //endregion

    //region 属性
    /**
     * 数据库名称
     */
    @NotBlank
    @Size(max = 30)
    @Getter @Setter
    private String dbSchema;

    /**
     * 对象名称
     */
    @NotBlank
    @Size(max = 30)
    @Getter @Setter
    private String objName;

    /**
     * 命名空间
     */
    @Size(max = 255)
    @Getter @Setter
    private String nameSpace;

    /**
     * 默认显示标签
     */
    @Size(max = 30)
    @Getter @Setter
    private String displayLabel;

    /**
     * 对象类型：T表，V视图，P存储过程，F函数
     * TA 支持附件
     */
    @NotBlank
    @Size(max = 3)
    @Getter @Setter
    private String objType;

    /**
     * 固定的过滤器，如statusFilter
     */
    @Size(max = 30)
    @Getter @Setter
    private String fixedFilter;

    /**
     * 租户内唯一索引字段，可生成findByXxx函数，查重等
     */
    @Size(max = 30)
    @Getter @Setter
    private String uniqueKey;

    /**
     * 分片/表分区主键，通常为表的BIGINT类型的主键ID
     */
    @Size(max = 30)
    @Getter @Setter
    private String partitionKey;

    /**
     * 是否使用表分区
     *
     * 如果使用，则查询语句会指定某个分区
     */
    @NotNull
    @Getter @Setter
    private boolean partitioned;


    /**
     * 名称字段
     */
    @Size(max = 30)
    @Getter @Setter
    private String nameCol;


    /**
     * 父字段，父子结构的实体指定parentID字段名
     */
    @Size(max = 30)
    @Getter @Setter
    private String parentIdCol;

    /**
     * 分组字段
     */
    @Size(max = 30)
    @Getter @Setter
    private String groupByCol;


    /**
     * 缩略图字段
     */
    @Size(max = 30)
    @Getter @Setter
    private String thumbnailCol;
    /**
     * 最小ID
     */
    @Min(0)
    @Getter @Setter
    private Long minID;

    /**
     * 最大ID
     */
    @Min(0)
    @Getter @Setter
    private Long maxID;

    /**
     * 扩展类型：0无，1继承，2扩展（添加了属性字段）
     */
    @NotNull
    @Min(0)
    @Getter @Setter
    private ExtensionType extendType;

    /**
     * 父类名称，指继承哪个父类
     */
    @Size(max = 30)
    @Getter @Setter
    private String superName;

    /**
     * 继承后的区别条件，用于SQL查询。
     * 例如Product继承自Material，区别条件定义为{@code materialType='PROD'}
     */
    @Size(max = 255)
    @Getter @Setter
    private String discrimination;

    /**
     * 描述
     */
    @Size(max = 255)
    @Getter @Setter
    private String description;

    /**
     * 时间
     */
    @Getter @Setter
    private Timestamp createDate;

    /**
     * 最后修改时间
     */
    @Getter @Setter
    private Timestamp lastModified;

    //endregion


    //////////////////////////////////////////////////////////////////////////
    // Columns
    //////////////////////////////////////////////////////////////////////////

    //region 字段

    /**
     * 字段列表
     */
    @Getter
    private List<MetaCol> cols;

    //以下属性都是临时计算
    private Map<String,MetaCol> namedCols;//命名字段字典，用于根据名称查找字段
    private List<MetaCol> keys;//主键集合
    private List<MetaCol> insertableCols;//可插入字段集合，排除自增和计算字段
    private List<MetaCol> updatableCols;//可更新字段集合，排除主键、自增和计算字段

    /**
     * 添加元列，在{@link MetaObject.Builder}中用于构建临时元对象
     * @param col
     */
    public void addCol(MetaCol col) {
        if(namedCols == null) namedCols = new LinkedHashMap<>();
        if(namedCols.containsKey(col.getColName())){
            throw new RuntimeException("Duplicate col name: " + col.getColName());
        }
        namedCols.put(col.getColName(), col);

        if(cols == null) cols = new ArrayList<>();
        cols.add(col);
        if(col.isKey()) keys = null;
        insertableCols = null;
        updatableCols = null;
    }

    /**
     * 设置元列集合，内部会重新创建命名列集合，反序列化后保证{@link #getCol(String)}可访问
     * @param cols 元列集合
     */
    public void setCols(List<MetaCol> cols) {
        this.cols = cols;
        this.namedCols = cols
                .stream()
                .collect(Collectors.toMap(MetaCol::getColName, Function.identity()));
    }

    /**
     * 设置命名列集合，名称可能是别名，构建元视图{@link MetaView}时使用
     * @param namedCols 命名列集合，此属性不参与序列化
     */
    public void setNamedCols(Map<String,MetaCol> namedCols){
        this.namedCols = namedCols;
        this.cols = namedCols.values().stream()
                .toList();
    }

    /**
     * 按名称获取本对象字段（不会搜索关联对象）
     * @param colName 字段名称
     * @return MetaCol 或者 null
     * @remarks 此函数只限定在元对象本身查找命名列，
     * 而{@link MetaContext#findCol(String)}会在上下文环境中搜索关联对象中的列集合中具有此名称的列，
     * 找到第一个即返回
     */
    public final MetaCol getCol(String colName){
        return namedCols.get(colName);
    }

    /**
     * 判断元对象本身是否有命名列（不包括关联对象的列）
     * @param colName 列名
     * @return 存在否
     */
    public final boolean hasCol(String colName){
        return namedCols.containsKey(colName);
    }

    /**
     * 判断元对象本身是否有计算列
     * @return 存在否
     */
    public final boolean hasComputedCols(){
        return cols.stream().anyMatch(col -> col.isComputed());
    }

    /**
     * 获取主键列表
     * @return 主键列表
     */
    @JsonIgnore
    public List<MetaCol> getKeyCols(){
        if(keys==null) {
            keys = cols
                    .stream()
                    .filter(c->c.isKey())
                    .sorted(Comparator.comparingInt(MetaCol::getColIdx))
                    .collect(Collectors.toList());
        }
        return keys;
    }
    @JsonIgnore
    public final boolean supportAttachments(){
        return this.objType.indexOf('A')>0;
    }
    @JsonIgnore
    public final boolean supportFlowTrails(){
        return this.objType.indexOf('F')>0;
    }

    /**
     * 判断是否有组合键，多个字段作为主键
     * @see #isSingleKey() 判断是否单主键
     */
    @JsonIgnore
    public final boolean hasCompositeKeys(){
        return getKeyCols().size()>1;
    }

    /**
     * 判断是否单字段主键
     * @see #hasCompositeKeys() 判断是否有组合键
     */
    @JsonIgnore
    public final boolean isSingleKey(){
        return getKeyCols().size() == 1;
    }

    /**
     * 获取可插入的列集合，排除了数据库生成的和计算列，用于生成{@code INSERT}语句
     * @see #getUpdatableCols() 获取可更新的列集合
     */
    @JsonIgnore
    public List<MetaCol> getInsertableCols(){
        if(insertableCols==null) {
            if(this.extendType == ExtensionType.EXTENDS) {
                this.insertableCols = cols
                        .stream()
                        .filter(c->c.getObjName().equals(this.objName) && !(c.isGenerated() || c.isComputed()))
                        .collect(Collectors.toList());
            }
            else{
                this.insertableCols = cols
                        .stream()
                        .filter(c->!(c.isGenerated() || c.isComputed()))
                        .collect(Collectors.toList());
            }
        }
        return insertableCols;
    }

    /**
     * 获取可更新的列集合，排除了主键、数据库生成的和计算列，用于生成{@code UPDATE}语句
     * @see #getInsertableCols() 获取可插入的列集合
     */
    @JsonIgnore
    public List<MetaCol> getUpdatableCols(){
        if(updatableCols==null) {
            if(this.extendType != ExtensionType.EXTENDS) {
                updatableCols = cols
                        .stream()
                        .filter(c->!(c.isKey() || c.isGenerated() || c.isComputed()))
                        .collect(Collectors.toList());
            }
            else{
                updatableCols = cols
                        .stream()
                        .filter(c->c.getObjName().equals(objName) && !(c.isKey() || c.isGenerated() || c.isComputed()))
                        .collect(Collectors.toList());
            }
        }
        return updatableCols;
    }

    public ConcurrentMap<MetaCol,MetaRelation> buildColRelationMap(MetaRelationType relationType){
        return cols.stream()
                .filter(c->c.getRelationType()==relationType)
                .collect(Collectors.toConcurrentMap(Function.identity(),c->getRelation(c.getRelationName())));
    }

    /**
     * 获取数据库生成的主键列集合，这些列不能插入、更新，在插入和更新后从数据库返回值
     * @see #getInsertableCols() 获取可插入的列集合
     * @see #getUpdatableCols() 获取可更新的列集合
     */
    @JsonIgnore
    public List<MetaCol> getGeneratedKeyCols(){
        return getKeyCols()
                .stream()
                .filter(k->k.isGenerated())
                .collect(Collectors.toList());
    }

    /**
     * 查找显示文本为 label 的元列，用于导入、导出时字段映射
     * @param label 标签文本
     * @return 元列
     */
    public Optional<MetaCol> findColByLabel(String label){
        return cols.stream().filter(c->label.equals(c.getDisplayLabel())).findFirst();
    }

    //endregion

    //////////////////////////////////////////////////////////////////////////
    // Relations
    //////////////////////////////////////////////////////////////////////////
    //region 关系

    /**
     * 元关系列表
     */
    @Getter @Setter
    private List<MetaRelation> relations;

    /**
     * 判断是否有关联关系
     * @return
     */
    @JsonIgnore
    public boolean hasRelations(){
        return relations!=null && !relations.isEmpty();
    }
    @JsonIgnore
    public boolean anyHasOneRelation(){
        if(!hasRelations()) return false;
        return relations.stream().anyMatch(r -> r.getRelationType() == MetaRelationType.HAS_ONE);
    }

    /**
     * 获取{@code HAS_ONE}类型的元关系集合
     * @see #anyHasOneRelation() 判断是否有一对一类型的关系
     */
    @JsonIgnore
    public List<MetaRelation> getHasOneRelations(){
        if(!hasRelations()) return List.of();
        return relations.stream()
                .filter(r -> r.getRelationType() == MetaRelationType.HAS_ONE)
                .collect(Collectors.toList());
    }

    /**
     * 获取命名元关系，内部遍历元关系集合实现，尽可能减少大批量重复调用
     * @param relationName 元关系名称
     * @return 存在即返回元关系，否则为 null
     */
    public MetaRelation getRelation(String relationName){
        if(!hasRelations()) return null;

        MetaRelation result = null;
        for(MetaRelation relation : relations){
            if(relation.getRelationName().equals(relationName)){
                result = relation;
                break;
            }
        }
        return result;
    }

    /**
     * 添加元关系，包括{@code HAS_ONE, HAS_MANY, REF, ENUM }
     * @param relation 元关系
     * @remarks 临时构建元对象和解析字段的关联关系时使用
     */
    public void addRelation(MetaRelation relation){
        if(relations==null) relations = new ArrayList<>();
        relations.add(relation);
    }
    //endregion of 关系

    //region 别名
    public static String getObjectAlias(final String objName, final MetaObject target, final Collection<MetaRelation> withRelations){
//        if(target.getObjName().equals(objName)) return "t";
        if(target.isSubObject() && target.getSuperName().equals(objName)) return "tb";
        if(BaseUtil.hasAny(withRelations)){
            for(var relation : withRelations){
                if(relation.getRelativeObjName().equals(objName)) return relation.getRelationName();
            }
        }
        return "t";
    }

    /**
     * 返回本身，实现元上下文{@link MetaContext}接口
     * @return
     */
    @JsonIgnore
    @Override
    public MetaObject get() {
        return this;
    }

    @JsonIgnore
    @Override
    public Map<MetaRelation, MetaObject> getRelatives() {
        return Map.of();//需要 MetadataProvider 才能提供关联对象
    }

    public String getObjectAlias(String metaObjName){
        Objects.requireNonNull(metaObjName, "metaObjName must not be null");
        return getObjectAlias(metaObjName,this,getRelations());
    }
    public String getObjectAlias(MetaCol col){
        return getObjectAlias(col.getObjName(),this,getRelations());
    }
    //endregion of 别名

    /**
     * 元对象主键，包含数据库模式和对象名称。
     * <p>
     *     不同数据库厂商对于数据库模式的定义不统一，
     *     例如不支持跨数据库查询的以{@code schema.Table}这种形式访问，
     *     而可以跨数据库访问的{@code db.schema.Table}或者{@code db.Table}形式访问，
     *     前者我们把{@code dbSchema = db.schema}，后者我们把{@code dbSchema = db}
     *     在SQL方言中自动生成数据库对象完整标识。
     * </p>
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key{
        /**
         * 数据库模式
         */
        @NotBlank
        @Getter @Setter
        private String dbSchema;

        /**
         * 对象名称
         */
        @NotBlank
        @Getter @Setter
        private String objName;
    }
    //////////////////////////////////////////////////////////////////////////
    // Hash code and equals
    //////////////////////////////////////////////////////////////////////////

    /**
     * 获取元对象全称
     * <p>
     *     在生成不同数据库版本请使用MetadataProvider#getFullObjName(MetaObject)
     * </p>
     * @return 返回"数据库模式.对象"，例如mmda_crm.Partner
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public final String getFullName(){
        return dbSchema+'.'+objName;
    }

    /**
     * 获取没有前缀的元对象全称
     * @return 返回"系统.对象"，例如crm.Partner
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public final String getFullNameWithoutPrefix(){
        var fullName = getFullName();
        int pos = fullName.indexOf('_');
        if(pos != -1) return fullName.substring(pos+1);
        return fullName;
    }

    @Override
    public int hashCode() {
        return getFullName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(super.equals(obj)) return true;
        if(obj instanceof MetaObject metaObj){
            return objName.equals(metaObj.getObjName()) && dbSchema.equals(metaObj.dbSchema);
        }
        return false;
    }

    //region 扩展

    @JsonIgnore
    public boolean isSubObject(){
        return this.extendType == ExtensionType.EXTENDS && BaseUtil.hasText(this.superName);
    }
    /**
     * 扩展一个基类元对象
     * @param superObj
     */
    public void extend(MetaObject superObj){
        Objects.requireNonNull(superObj, "superObj should not be null");

        //继承核扩展属性列
        var cols = new ArrayList<>(superObj.getCols());
        for(var col : this.getCols()){
            var superCol = superObj.getCol(col.getColName());
            if(superCol != null){
                //继承
                col.setExtendType(ExtensionType.INHERITS);
                cols.set(cols.indexOf(superCol),col);
            }
            else{
                //扩展
                col.setExtendType(ExtensionType.EXTENDS);
                cols.add(col);
            }
        }
        setCols(cols);

        //继承关系
        for(var relation : superObj.getRelations()){
            if(this.getRelation(relation.getRelationName()) == null) addRelation(relation);
        }

        //这些采用基类的配置
        this.uniqueKey = superObj.uniqueKey;
        this.partitionKey = superObj.partitionKey;
        this.nameCol = superObj.nameCol;
        this.parentIdCol = superObj.parentIdCol;
        this.groupByCol = superObj.groupByCol;
        this.thumbnailCol = superObj.thumbnailCol;
        this.minID = superObj.minID;
        this.maxID = superObj.maxID;
        this.fixedFilter = superObj.fixedFilter;
    }
    //endregion of 扩展

    //region 访问器构建

    /**
     * 安装属性 getter 访问器，{@link MetaObjectAccess}初始化调用此函数
     * @param fieldName 属性名称可能是字段或者关联属性名称
     * @param getterName 属性 getter 函数名
     * @param getterIndex 属性 getter 索引
     * @param returnType 属性 getter 返回类型
     * @return 成功否
     */
    public boolean setupGetter(final String fieldName, final String getterName, int getterIndex, final Class<?> returnType){
        var col = getCol(fieldName);
        if(col != null){
            col.setAccessGetter(new MetaGetterAccess(getterName,getterIndex,returnType));
            if(col.isEnumType()){

            }
            return true;
        }
        else{
            var rel = getRelation(fieldName);
            if(rel != null){
                rel.setAccessGetter(new MetaGetterAccess(getterName,getterIndex,returnType));
                return true;
            }
        }
        return false;
    }

    /**
     * 安装属性 setter 访问器，{@link MetaObjectAccess}初始化调用此函数
     * @param fieldName 属性名称可能是字段或者关联属性名称
     * @param setterName 属性 setter 函数名
     * @param setterIndex 属性 setter 索引
     * @return 成功否
     */
    public boolean setupSetter(final String fieldName, final String setterName, int setterIndex){
        var col = getCol(fieldName);
        if(col != null){
            col.setAccessSetter(new MetaSetterAccess(setterName, setterIndex));
            return true;
        }
        else{
            var rel = getRelation(fieldName);
            if(rel != null){
                rel.setAccessSetter(new MetaSetterAccess(setterName, setterIndex));
                return true;
            }
        }
        return false;
    }
    //endregion of Access

    //region 约束 & 索引

    /**
     * 外键
     */
    @JsonIgnore
    @Getter @Setter
    private Collection<MetaForeignKey> foreignKeys;

    /**
     * 索引
     */
    @JsonIgnore
    @Getter @Setter
    private Collection<MetaIndex> indexes;


    //endregion of 索引

    public static class Builder{
        private MetaObject metaObj;

        public MetaObject get(){
            return metaObj;
        }
        public Builder(String dbSchema, String objName){
            this.metaObj = new MetaObject();
            this.metaObj.setDbSchema(dbSchema);
            this.metaObj.setObjName(objName);
            this.metaObj.namedCols = new LinkedHashMap<>();
            this.metaObj.cols = new ArrayList<>();
        }
        public Builder(MetaObject metaObj){
            this.metaObj = metaObj;
        }

        public Builder withPartition(String partitionKey, boolean partitioned)
        {
            metaObj.partitionKey = partitionKey;
            metaObj.partitioned = partitioned;
            return this;
        }
        public MetaCol.Builder withBoolCol(String colName, boolean nullable){
            return new MetaCol.Builder(this)
                    .withName(colName)
                    .withType(DataType.BOOL, 4, nullable);
        }
        public MetaCol.Builder withTinyIntCol(String colName, boolean nullable){
            return new MetaCol.Builder(this)
                    .withName(colName)
                    .withType(DataType.INT8, 4, nullable);
        }
        public MetaCol.Builder withSmallIntCol(String colName, boolean nullable){
            return new MetaCol.Builder(this)
                    .withName(colName)
                    .withType(DataType.INT16, 6, nullable);
        }
        public MetaCol.Builder withIntCol(String colName, boolean nullable){
            return new MetaCol.Builder(this)
                    .withName(colName)
                    .withType(DataType.INT32, 11, nullable);
        }
        public MetaCol.Builder withBigIntCol(String colName, boolean nullable)
        {
            return new MetaCol.Builder(this)
                    .withName(colName)
                    .withType(DataType.INT64, 20, nullable);
        }
        public MetaCol.Builder withBigIdCol(String colName)
        {
            return new MetaCol.Builder(this)
                    .withKey(colName)
                    .withType(DataType.INT64, 20);
        }
        public MetaCol.Builder withVarcharCol(String colName, int maxLength, boolean nullable)
        {
            return new MetaCol.Builder(this)
                    .withName(colName)
                    .withType(DataType.VARCHAR, maxLength, nullable);
        }
        public MetaCol.Builder withDecimalCol(String colName, int precision, int scale, boolean nullable)
        {
            return new MetaCol.Builder(this)
                    .withName(colName)
                    .withType(DataType.DECIMAL, null, precision, scale, nullable);
        }
        public MetaCol.Builder withNoCol(String colName, int maxLength)
        {
            return new MetaCol.Builder(this)
                    .withName(colName)
                    .withType(DataType.VARCHAR, maxLength);
        }

        public MetaCol.Builder withRemarkCol()
        {
            return withVarcharCol("remark", 255, true)
                    .withComment("备注");
        }

        public void addCol(MetaCol metaCol) {
            metaObj.addCol(metaCol);
        }
        public void addRelation(MetaRelation metaRelation) {
            metaObj.addRelation(metaRelation);
        }
    }

}
