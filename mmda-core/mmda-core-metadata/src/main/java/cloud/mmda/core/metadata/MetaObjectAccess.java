package cloud.mmda.core.metadata;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.enums.ExtensionType;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.NamingUtil;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 对象访问类基于元对象{@link MetaObject}提供实体对象的属性读写访问函数和参数集合构建。
 * 我们基于<a href="https://github.com/EsotericSoftware/reflectasm">ReflectAsm</a>实现了
 * 基于字节码技术的反射性能优化。
 *
 * @see <a href="https://www.jianshu.com/p/6bc74577bfa6">Java反射性能优化方案</a>
 */
public final class MetaObjectAccess<T,K> {
    @Getter
    private final MetaObject metaObject;
    @Getter
    private final Class<T> objClass;
    @Getter
    private final Class<K> keyClass;
    private final MethodAccess objAccess, compositeKeyAccess;
    private final ConcurrentHashMap<String, MetaPropAccess> compositeKeyColsAccess;

    private final ConstructorAccess<T> constructorAccess;
    private final ConstructorAccess<K> compositeKeyConstructorAccess;
    public MetaObjectAccess(MetaObject metaObject, Class<T> objClass, Class<K> keyClass){
        this.metaObject = metaObject;
        this.objClass = objClass;
        this.keyClass = keyClass;
        this.objAccess = MethodAccess.get(objClass);
        this.constructorAccess = ConstructorAccess.get(objClass);

        var methodNames = objAccess.getMethodNames();
        var returnTypes = objAccess.getReturnTypes();
        for(int i = 0; i < methodNames.length; i++){
            var methodName = methodNames[i];
            if(methodName.startsWith("get")){
                var fieldName = NamingUtil.firstLetterLower(methodName.substring(3));
                metaObject.setupGetter(fieldName, methodName, i, returnTypes[i]);
            }
            else if(methodName.startsWith("is")){
                var fieldName = NamingUtil.firstLetterLower(methodName.substring(2));
                metaObject.setupGetter(fieldName, methodName, i, returnTypes[i]);
            }
            else if(methodName.startsWith("set")){
                var fieldName = NamingUtil.firstLetterLower(methodName.substring(3));
                metaObject.setupSetter(fieldName, methodName, i);
            }
        }

        /** 内嵌循环效率低下，objAccess.getIndex遍历了所有方法名称，找索引，改为上面的代码（待验证）
        var objName = metaObject.getObjName();
        for(MetaCol col : metaObject.getCols()){
            // 基类属性在基类中构建访问器
            if(!col.getObjName().equalsIgnoreCase(objName) && col.getExtendType() == ExtensionType.NONE) continue;

            // 属性访问器
            String propName = NamingUtil.firstLetterUpper(col.getColName());
            String getterName = (col.getDataType() == DataType.BOOL && !col.isNullable())
                    ? "is" + propName
                    : "get" + propName;
            String setterName = "set" + propName;

            int getterIndex = objAccess.getIndex(getterName);
            int setterIndex = objAccess.getIndex(setterName, 1);
            MetaPropAccess propAccess = new MetaPropAccess(getterName, getterIndex, setterName, setterIndex);
            col.setAccess(propAccess);

            //枚举访问器
            if(col.isEnumType()){
            }
        }

        // 关联对象属性访问器构建，包含 HAS_ONE, HAS_MANY
        for(MetaRelation relation : metaObject.getRelations()){
            // 继承得到的忽略，在基类中已经有了
            if(!relation.getObjName().equalsIgnoreCase(objName)) continue;
            // 忽略引用和枚举
            if(relation.getRelationType().compareTo(MetaRelationType.HAS_MANY)>0) continue;

            String propName = NamingUtil.firstLetterUpper(relation.getRelationName());
            String getterName = "get" + propName;
            String setterName = "set" + propName;
            int getterIndex = objAccess.getIndex(getterName);
            int setterIndex = objAccess.getIndex(setterName, 1);
            MetaPropAccess propAccess = new MetaPropAccess(getterName, getterIndex,setterName,setterIndex);
            relation.setAccess(propAccess);
        }
        */

        // 组合式主键访问
        if(metaObject.hasCompositeKeys()){ // 组合键
            this.compositeKeyAccess = MethodAccess.get(keyClass);
            this.compositeKeyColsAccess = new ConcurrentHashMap<>();
            for(MetaCol col : metaObject.getKeyCols()){
                String propName = NamingUtil.firstLetterUpper(col.getColName());
                String getterName = (col.getDataType() == DataType.BOOL && !col.isNullable())
                        ? "is" + propName
                        : "get" + propName;
                String setterName = "set" + propName;

                MetaPropAccess colAccess = new MetaPropAccess(getterName, compositeKeyAccess.getIndex(getterName), setterName,
                        compositeKeyAccess.getIndex(setterName, 1));
                compositeKeyColsAccess.put(col.getColName(), colAccess);
            }
            this.compositeKeyConstructorAccess = ConstructorAccess.get(keyClass);
        }
        else{ // 单主键
            this.compositeKeyAccess = null;
            this.compositeKeyColsAccess =null;
            this.compositeKeyConstructorAccess = null;
        }
    }

    //region 新实例
    public T newInstance(){
        return constructorAccess.newInstance();
    }
    //endregion of 新实例

    //region 属性访问
    public Object getProperty(T entity, String field) throws IllegalAccessException {
        MetaCol col = this.metaObject.getCol(field);
        //if(colAccess==null) return null; //let it throws exception
        if(col != null) return objAccess.invoke(entity, col.getAccessGetter().index());

        var relation = this.metaObject.getRelation(field);
        if(relation != null) return objAccess.invoke(entity, relation.getAccessGetter().index());
        throw new IllegalAccessException(this.metaObject.getObjName() + " hasn't any property named " + field);
    }
    public Object getProperty(T entity, MetaCol col){
        return objAccess.invoke(entity, col.getAccessGetter().index());
    }
    public Object getProperty(T entity, MetaRelation relation){
        return objAccess.invoke(entity, relation.getAccessGetter().index());
    }
    public K getKey(T entity){
        Objects.requireNonNull(entity, "entity cannot be null");
        if(metaObject.isSingleKey()) {
            var keyCol = metaObject.getKeyCols().get(0);
            return (K) getProperty(entity, keyCol);
        }
        else{
            var k = compositeKeyConstructorAccess.newInstance();
            for(MetaCol col : metaObject.getKeyCols()){
                var value = getProperty(entity, col);
                MetaPropAccess colAccess = compositeKeyColsAccess.get(col.getColName());
                compositeKeyAccess.invoke(entity, colAccess.setterIndex(), value);
            }
            return k;
        }
    }
    public void setProperty(T entity, String field, Object value) throws IllegalAccessException {
        MetaCol col = this.metaObject.getCol(field);
        if(col != null) {
            objAccess.invoke(entity, col.getAccessSetter().index(), value);
            return;
        }
        var relation = this.metaObject.getRelation(field);
        if(relation != null) {
            objAccess.invoke(entity, relation.getAccessSetter().index(), value);
            return;
        }
        throw new IllegalAccessException(this.metaObject.getObjName() + " hasn't any property named " + field);
    }

    public void setProperty(T entity, MetaCol col, Object value){
        objAccess.invoke(entity, col.getAccessSetter().index(), value);
    }

    public void setProperty(T entity, MetaRelation relation, Object value){
        objAccess.invoke(entity,relation.getAccessSetter().index(), value);
    }
    public void setKey(T entity, K key){
        Objects.requireNonNull(entity, "entity cannot be null");
        Objects.requireNonNull(key, "key cannot be null");
        if(metaObject.isSingleKey()) {
            var keyCol = metaObject.getKeyCols().get(0);
            setProperty(entity, keyCol, key);
        }
        else{
            for(MetaCol col : metaObject.getKeyCols()){
                MetaPropAccess colAccess = compositeKeyColsAccess.get(col.getColName());
                var value = compositeKeyAccess.invoke(key, colAccess.getterIndex());
                setProperty(entity, col, value);
            }
        }
    }
    public Object getKeyProperty(Object keyOrEntity, String colName) throws IllegalAccessException {
        if(keyClass.isInstance(keyOrEntity)){
            if(compositeKeyAccess != null){
                MetaPropAccess colAccess = compositeKeyColsAccess.get(colName);
                return compositeKeyAccess.invoke(keyOrEntity, colAccess.getterIndex());
            }
            else{
                return keyOrEntity;
            }
        }
        else{
            //传入整个实体，获取复合主键某一列
            return getProperty((T)keyOrEntity, colName);
        }
    }

    public void setKeyProperty(Object keyOrEntity, String colName, Object value) throws IllegalAccessException {
        if(keyClass.isInstance(keyOrEntity)){
            if(compositeKeyAccess != null){
                MetaPropAccess colAccess = compositeKeyColsAccess.get(colName);
                compositeKeyAccess.invoke(keyOrEntity, colAccess.setterIndex(), value);
            }
            else{
                keyOrEntity = value;
            }
        }
        else{
            setProperty((T)keyOrEntity, colName, value);
        }
    }
    //endregion of 属性访问

    //region 参数化 args

    public Stream<Object> argStreamOfEntityCols(T entity, List<MetaCol> cols) {
        return cols.stream()
                .map(col -> getProperty(entity, col));
    }
    public Object[] argArrayOfEntityCols(T entity, List<MetaCol> cols) {
        return argStreamOfEntityCols(entity, cols).toArray();
    }
    public Stream<Object> argStreamOfEntityKey(T entity) {
        return argStreamOfEntityCols(entity, metaObject.getKeyCols());
    }
    /**
     * 将实体转化为主键参数数组，用于执行JDBC指令
     * @param entity 实体
     * @return 主键值数组
     */
    public Object[] argArrayOfEntityKey(T entity) {
        return argArrayOfEntityCols(entity, metaObject.getKeyCols());
    }
    public Object[] argArrayOfSingleKey(K key){
        return new Object[] { key };
    }
    /**
     * 将组合键转化为参数数组，用于执行JDBC指令
     * @param compositeKey 组合键值
     * @return 数组
     */
    public Object[] argArrayOfCompositeKey(K compositeKey) {
        return metaObject.getKeyCols().stream()
                .map(col -> compositeKeyAccess.invoke(compositeKey, compositeKeyColsAccess.get(col.getColName()).getterIndex()))
                .toArray();
    }
    public Object[] argArrayOfKey(K key){
        return metaObject.isSingleKey()
                ? new Object[] { key }
                : metaObject.getKeyCols().stream()
                    .map(col -> compositeKeyAccess.invoke(key, compositeKeyColsAccess.get(col.getColName()).getterIndex()))
                    .toArray();
    }

    public Stream<Object> argStreamOfKey(K key){
        return metaObject.isSingleKey()
                ? Stream.of(key)
                : metaObject.getKeyCols().stream()
                .map(col -> compositeKeyAccess.invoke(key, compositeKeyColsAccess.get(col.getColName()).getterIndex()));
    }


    private static Object[] emptyArgArray = new Object[0];
    public Object[] argArrayOfMinMaxId(int tenantId) {
        if(BaseUtil.hasText(metaObject.getPartitionKey())){
            var minId = Tenancy.getMinEntityID(tenantId, metaObject);
            var maxId = Tenancy.getMaxEntityID(tenantId, metaObject);
            return new Object[] { minId, maxId };
        }
        return emptyArgArray;
    }
    /**
     * 将实体转化为插入参数数组，用于执行JDBC指令
     * @param entity 实体对象
     * @return 参数数组
     */
    public Object[] argArrayOfInsertable(T entity) {
        return argArrayOfEntityCols(entity,metaObject.getInsertableCols());
    }
    /**
     * 将实体转化为更新参数数组，用于执行JDBC指令
     * @param entity 实体对象
     * @return 参数数组
     */
    public Object[] argArrayOfUpdatable(T entity) {
        return argArrayOfEntityCols(entity,metaObject.getUpdatableCols());
    }
    public Stream<Object> argStreamOfUpdatable(T entity) {
        return argStreamOfEntityCols(entity,metaObject.getUpdatableCols());
    }

    public Map<Object, Object> argMapOfEntityCols(T entity, List<MetaCol> cols){
        return cols.stream()
                .map(col -> Map.entry(col,getProperty(entity, col)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public Map<Object, Object> argMapOfEntityKeyCols(T entity){
        return argMapOfEntityCols(entity, metaObject.getKeyCols());
    }

    public Map<Object, Object> argMapOfSingleKey(K key){
        return new HashMap<>(){
            { put(metaObject.getKeyCols().get(0),key);}
        };
    }
    public Map<Object, Object> argMapOfCompositeKey(K compositeKey){
        return metaObject.getKeyCols().stream()
                .map(col -> Map.entry(col, compositeKeyAccess.invoke(compositeKey, compositeKeyColsAccess.get(col.getColName()).getterIndex())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    //endregion of 参数化



}
