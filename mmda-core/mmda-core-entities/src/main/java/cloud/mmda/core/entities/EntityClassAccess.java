package cloud.mmda.core.entities;

import cloud.mmda.core.LazySingletonSupplier;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.metadata.MetaObjectAccess;
import cloud.mmda.core.metadata.MetaRelation;
import cloud.mmda.core.sql.SqlCmd;
import cloud.mmda.core.sql.SqlQueryable;
import cloud.mmda.core.sql.expressions.SqlCriteriaExp;
import cloud.mmda.core.sql.expressions.SqlCriteriaSupplier;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 实体类访问器提供根据实体类型{@code T}访问元对象{@link #getMetaObject()}、
 * 对象实例属性读取{@link #getProperty(Entity, MetaCol)}、写入{@link #setProperty(Entity, MetaCol, Object)}、
 * SQL查询条件{@link SqlCriteriaExp}、可执行查询{@link SqlQueryable}、命令{@link SqlCmd}缓存
 * @param <T> 实体类型
 * @param <K> 实体主键类型
 */
public final class EntityClassAccess<T extends Entity, K> {
    private final LazySingletonSupplier<MetaObjectAccess<T, K>> objectAccessSupplier;//对象访问器
    private final LazySingletonSupplier<RowMapper<T>> rowMapperSupplier;//行映射器
    private final LazySingletonSupplier<Sort> keySortSupplier;//主键排序

    private final ConcurrentHashMap<String, SqlCmd> sqlCommands;
    private final ConcurrentHashMap<String, SqlQueryable> sqlQueryables;
    private final ConcurrentHashMap<String, SqlCriteriaExp> sqlCriteria;

    /**
     * 构造实体类访问器，用来访问实体对象的属性、关联元数据和行映射器，为实体工厂{@link EntityFactory}提供服务
     * @param metaObject 元对象
     * @param entityClass 实体类
     * @param keyClass 实体主键类
     * @param rowMapperSupplier 行映射器提供函数
     */
    public EntityClassAccess(@NonNull final MetaObject metaObject,
                             @NonNull final Class<T> entityClass,
                             @NonNull final Class<K> keyClass,
                             Supplier<RowMapper<T>> rowMapperSupplier) {
        this.objectAccessSupplier = new LazySingletonSupplier<>(() -> new MetaObjectAccess<>(metaObject, entityClass, keyClass));
        this.keySortSupplier = new LazySingletonSupplier<>(()->Sort.byKey(getObjectAccess().getMetaObject()));
        this.sqlCommands = new ConcurrentHashMap<>();
        this.sqlQueryables = new ConcurrentHashMap<>();
        this.sqlCriteria = new ConcurrentHashMap<>();
        this.rowMapperSupplier = rowMapperSupplier != null
            ? new LazySingletonSupplier<>(rowMapperSupplier)
            : new LazySingletonSupplier<>(()->createDefaultRowMapper());
    }

    /**
     * 构造实体类访问器，用来访问实体对象的属性、关联元数据和行映射器，为实体工厂{@link EntityFactory}提供服务。
     * 此函数使用内部默认的行映射器，建议你使用{@code EntityClassAccess(MetaObject, Class, Class, Supplier)}提供自己的更高性能的行映射器
     * @param metaObject 元对象
     * @param entityClass 实体类
     * @param keyClass 实体主键类
     */
    public EntityClassAccess(@NonNull final MetaObject metaObject,
                             @NonNull final Class<T> entityClass,
                             @NonNull final Class<K> keyClass) {
        this(metaObject,entityClass,keyClass, null);
    }
    /**
     * 返回单表行映射器，用于从{@link ResultSet}中解构数据为实体{@code T}
     * @return
     */
    public RowMapper<T> getRowMapper() {
        return rowMapperSupplier.get();
    }

    private RowMapper<T> createDefaultRowMapper() {
        final var objAccess = getObjectAccess();
        final var metaObj = objAccess.getMetaObject();
        return (rs, rowNum) -> {
            var t = objAccess.newInstance();
            t.setRowNum(rowNum);
            for(MetaCol col : metaObj.getCols()) {
                objAccess.setProperty(t, col, col.getResult(rs));
            }
            return t;
        };
    }

    /**
     * 返回实体对象对象访问器，它提供{@code getProperty}和{@code setProperty}函数读写对象属性
     * @return
     */
    public MetaObjectAccess<T, K> getObjectAccess() {
        return objectAccessSupplier.get();
    }

    /**
     * 返回实体类
     */
    public Class<T> getEntityClass() {
        return getObjectAccess().getObjClass();
    }

    /**
     * 返回实体主键类
     */
    public Class<K> getKeyClass() {
        return getObjectAccess().getKeyClass();
    }

    /**
     * 返回实体对应的元对象
     */
    public MetaObject getMetaObject(){
        return getObjectAccess().getMetaObject();
    }

    //region 属性访问
    public Object getProperty(final T entity, final String field) throws IllegalAccessException {
        return getObjectAccess().getProperty(entity, field);
    }
    public Object getProperty(final T entity, final MetaCol col) {
        return getObjectAccess().getProperty(entity, col);
    }
    public Object getProperty(final T entity, final MetaRelation rel) {
        return getObjectAccess().getProperty(entity, rel);
    }

    /**
     * 获取实体{@code entity}的主键值
     * @param entity 实体对象
     * @return 主键值
     */
    public K getKey(final T entity) {
        return getObjectAccess().getKey(entity);
    }

    public void setProperty(final T entity, final String field, final Object value) throws IllegalAccessException {
        getObjectAccess().setProperty(entity, field, value);
    }
    public void setProperty(final T entity, final MetaCol col, final Object value) {
        getObjectAccess().setProperty(entity, col, value);
    }
    public void setProperty(final T entity, final MetaRelation rel, final Object value) {
        getObjectAccess().setProperty(entity, rel, value);
    }

    /**
     * 设置实体{@code entity}的主键值为{@code key}
     * @param entity 实体对象
     * @param key 主键值
     */
    public void setKey(final T entity, final K key) {
        getObjectAccess().setKey(entity, key);
    }

    //endregion 属性访问

    /**
     * 采用{@code sort}排序，如果它为空则使用主键排序
     * @param sort 排序规则
     * @return 非空的排序规则
     */
    public Sort sortByOrDefault(@Nullable final Sort sort){
        return sort != null ? sort : sortByKey();
    }

    /**
     * 获取按主键排序的规则
     * @return 排序规则
     */
    public Sort sortByKey(){
        return keySortSupplier.get();
    }

    /**
     * 根据名称获取可执行SQL查询，如果缓存未命中，采用第二个参数创建并放入缓存
     * @param name 查询名称
     * @param queryable 可执行SQL查询提供函数
     * @return 可执行SQL查询
     */
    public SqlQueryable queryable(final String name, final Supplier<SqlQueryable> queryable) {
        return sqlQueryables.computeIfAbsent(name, n -> queryable.get());
    }

    /**
     * 根据名称获取可执行SQL命令，如果缓存未命中，采用第二个参数创建并放入缓存
     * @param name 命令名称
     * @param cmd 可执行SQL命令提供者
     * @return 可执行SQL命令
     */
    public SqlCmd command(final String name, final Supplier<SqlCmd> cmd) {
        return sqlCommands.computeIfAbsent(name, n -> cmd.get());
    }

    /**
     * 根据名称获取SQL查询条件表达式，如果缓存未命中，采用第二个参数创建并放入缓存
     * @param name 条件名称
     * @param exp Sql查询条件表达式提供者
     * @return SQL查询条件表达式
     */
    public SqlCriteriaExp namedCondition(final String name, final Supplier<SqlCriteriaExp> exp) {
        return sqlCriteria.computeIfAbsent(name, n -> exp.get());
    }

    public SqlCriteriaExp namedCondition(final SqlCriteriaSupplier condition) {
        return sqlCriteria.computeIfAbsent(condition.name(), n -> condition.delegate().get());
    }
}
