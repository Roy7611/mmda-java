package cloud.mmda.core.entities;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.data.pagination.PagedList;
import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.lamda.*;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.sql.*;
import cloud.mmda.core.sql.dialects.SqlDialect;
import cloud.mmda.core.sql.expressions.SqlArithmeticExp;
import cloud.mmda.core.sql.expressions.SqlCriteriaExp;
import cloud.mmda.core.sql.expressions.SqlExp;
import cloud.mmda.core.sql.expressions.SqlCriteriaSupplier;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.CollectionUtil;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.lookup.DataSourceLookupFailureException;

import javax.sql.DataSource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static cloud.mmda.core.sql.SqlConsts.*;

/**
 * 实体工厂基于{@link SqlDialect SQL方言}提供面向实体类的SQL命令和查询生成、缓存，实体类的属性访问
 * <p>
 * MetaObject - EntityClass(KeyClass) - MetaObjectAccess - RowMapper
 * 实现元数据-实体类-对象访问-数据库读写，基于{@link SqlDialect}实现Lambda构建命令{@link SqlCmd}和查询{@link SqlQueryable}。
 *
 * <p>
 * 原来的Repository注入此工厂并注册RowMapper，叠加上实体类型，具备了元对象访问能力。
 * 原来的元数据缓存可取消，使用实体工厂中的元数据内存缓存
 *
 * <p>
 * 利用<a href="https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html">JDBC API</a>访问数据库所有对象。
 * 包括CRUD操作。你的Service层完全可以使用实体工厂替代原来的Repository模式
 */
public class EntityFactory  {
    private static final Logger log = LoggerFactory.getLogger(EntityFactory.class);
    private static final ConcurrentHashMap<Class<?>, EntityClassAccess<?,?>> entityRegistries = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, MetaCol> lambdaGetterColMap = new ConcurrentHashMap<>();//lambda => col
    private static final ConcurrentHashMap<Class<?>,Class<?>> entityKeyClassMap = new ConcurrentHashMap<>();
    //RowMapper 与 SqlDialect 相关，例如CLOB,BLOB,UDT，还有枚举类型的中间转换
    private static final ConcurrentHashMap<Class<?>, RowMapper<?>> rowMappers = new ConcurrentHashMap<>();

    @Getter
    private final SqlDialect dialect;//包含元数据提供、SQL生成
    @Getter
    private final MetadataProvider metadataProvider;
    private final JdbcTemplate jdbcTemplate;

    /**
     * 构造一个实体工厂，需要指定是一个什么样的底层数据库，包括数据源连接和数据库类型（讲什么方言）
     * @param dataSource 数据源
     * @param dialect SQL方言
     */
    public EntityFactory(final DataSource dataSource, final SqlDialect dialect) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.dialect = dialect;
        this.metadataProvider = dialect.getMetadataProvider();
    }

    /**
     * 声明工厂提供实体类服务，若不提供行映射器可使用{@link #supply(Class, Class)}
     * @param entityClass 实体类
     * @param keyClass 实体主键类
     * @param supplier 行映射器提供函数，用于从Jdbc驱动读取数据、解构出实体
     * @return 实体工厂本身
     * @param <T> 实体类型
     * @param <K> 实体主键类型
     */
    public <T extends Entity<K>,K> EntityFactory supply(Class<T> entityClass, Class<K> keyClass, Supplier<RowMapper<T>> supplier) {
        var metaObj = metadataProvider.getMetaObject(entityClass);
        entityRegistries.putIfAbsent(entityClass, new EntityClassAccess<>(metaObj,entityClass,keyClass,supplier));
        return this;
    }

    /**
     * 声明工厂提供实体{@code T}服务
     * 
     * @see #supply(Class, Class, Supplier)  
     * 
     * @param entityClass 实体类
     * @param keyClass 实体主键类
     * @return 实体工厂本身
     * @param <T> 实体类型
     * @param <K> 实体主键类型
     */
    public <T extends Entity<K>,K> EntityFactory supply(Class<T> entityClass, Class<K> keyClass) {
        var metaObj = metadataProvider.getMetaObject(entityClass);
        entityRegistries.putIfAbsent(entityClass, new EntityClassAccess<>(metaObj,entityClass,keyClass));
        return this;
    }

    /**
     * 使用反射获得实体类的主键类型，我们建议使用{@link #supply(Class, Class)}手动注册实体，避免使用反射
     * @param entityClass 实体类
     * @return 主键类 或者 null
     * @param <T> 实体类型
     */
    private <T extends Entity<?>> Class<?> getKeyClass(Class<T> entityClass) {
        return entityKeyClassMap.computeIfAbsent(entityClass, eclass -> {
            Type[] genericInterfaces = entityClass.getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof Entity<?> entityType) {
                    Type keyClass = ((ParameterizedType) entityType).getActualTypeArguments()[0];
                    return (Class<?>) keyClass;
                }
            }
            return null;
        });
    }
    
    private <T extends Entity<K>,K>  RowMapper<T> createEagerRowMapper(
            @NonNull final Class<T> entityClass,
            @NonNull final Class<K> keyClass,
            @NonNull final List<MetaRelation> relations) {
        final EntityClassAccess<T,?> classAccess = access(entityClass,keyClass);
        final MetaObjectAccess<T,?> objAccess = classAccess.getObjectAccess();
        final MetaObject metaObj = objAccess.getMetaObject();
        if(relations.isEmpty()){
            relations.addAll(metaObj.getHasOneRelations());
        }
        return (rs, rowNum) -> {
            var t = objAccess.newInstance();
            for(MetaCol col : metaObj.getCols()) {
                objAccess.setProperty(t, col, col.getResult(rs));
            }

            for(var relation : relations){
                //若关联属性值为空则不组装
                var relativeProp = objAccess.getProperty(t, relation);
                if(relativeProp == null) continue;

                var relativeRowMapper = getRelativeRowMapper(relation);
                var r = relativeRowMapper.mapRow(rs, 1);
                objAccess.setProperty(t, relation, r);
            }
            t.setRowNum(rowNum);
            return t;
        };
    }
    private <T extends Entity<K>,K>  RowMapper<T> createEagerRowMapper(Class<T> entityClass, Class<K> keyClass){
        return createEagerRowMapper(entityClass, keyClass, List.of());
    }

    @SuppressWarnings("unchecked")
    private  <T extends Entity<K>, K> EntityClassAccess<T, K> access(final Class<T> entityClass, Class<K> keyClass) {
        return (EntityClassAccess<T, K>) entityRegistries.computeIfAbsent(entityClass, eclass->{
            var metaObj = metadataProvider.getMetaObject(entityClass);
            return new EntityClassAccess<>(metaObj, entityClass, keyClass);
        });
    }
    @SuppressWarnings("unchecked")
    private  <T extends Entity<K>,K> EntityClassAccess<T, K> access(final Class<T> entityClass, K key) {
        return access(entityClass, (Class<K>)key.getClass());
    }

    @SuppressWarnings("unchecked")
    private <T extends Entity<?>> EntityClassAccess<T, ?> access(final Class<T> entityClass) {
        return (EntityClassAccess<T, ?>)entityRegistries.computeIfAbsent(entityClass, eclass->{
            var metaObj = metadataProvider.getMetaObject(entityClass);
            return new EntityClassAccess<>(metaObj, entityClass, getKeyClass(entityClass));
        });
    }

    @SuppressWarnings("unchecked")
    private <T extends Entity<?>> EntityClassAccess<T, ?> access(T t) {
        final Class<T> entityClass = (Class<T>)t.getClass();
        return (EntityClassAccess<T, ?>)entityRegistries.computeIfAbsent(entityClass, eclass->{
            var metaObj = metadataProvider.getMetaObject(entityClass);
            return new EntityClassAccess<>(metaObj, entityClass, t.getIdClass());
        });
    }


    /**
     * 根据实体类获取元对象，前提是初始化实体工厂时提供了实体
     * <pre>
     *     {@code
     *      @Autowired
     *     private final entityFactory; //注入实体工厂
     *     //注入前声明提供哪些产品（实体类和行对象映射）
     *     entityFactory.supply(Partner.class, Long.class, () -> new PartnerRowMapper())
     *          .supply(...);
     *
     *     //现在可以访问此函数
     *     var metaObj = EntityFactory.getMetaObject(Partner.class);
     *     }
     * </pre>
     * @param entityClass 实体类
     * @return 元对象
     * @param <T> 实体类型
     */
    public <T extends Entity<K>, K> MetaObject getMetaObject(
            @NonNull final Class<T> entityClass,
            @NonNull final Class<K> keyClass) {
        return access(entityClass,keyClass).getMetaObject();
    }
    /**
     * 根据Lambda表达式获取元列字段，例如
     * <pre>
     *     {@code EntityFactory.getMetaCol(User::getUserId);}
     * </pre>
     * @param getter
     * @return
     * @param <T> 实体类型，？属性类型不关心
     */
    public static <T> MetaCol getMetaCol(@NonNull final LambdaGetter<T,?> getter) {
        return lambdaGetterColMap.computeIfAbsent(getter.getClass(), aClass -> {
            Class<?> entityClass = LambdaUtil.getImplClass(getter);
            var classAccess = entityRegistries.get(entityClass);
            String propertyName = LambdaUtil.getFieldName(getter);
            return classAccess.getMetaObject().getCol(propertyName);
        });
    }
    /**
     * 获取实体{@code entity}对象中名称为{@code field}的属性值
     * @param entity 实体
     * @param field 属性名称，可以是字段或者关系名称
     * @return 属性值
     * @param <T> 实体类型
     * @throws IllegalAccessException {@code field}属性在实体中不存在
     */
    public <T extends Entity<?>> Object getProperty(@NonNull final T entity, @NonNull final String field) throws IllegalAccessException {
        return access(entity).getProperty(entity, field);
    }


    public <T extends Entity<?>> void setProperty(@NonNull final T entity, @NonNull final String field, Object value) throws IllegalAccessException {
        access(entity).setProperty(entity, field, value);
    }
    public <T extends Entity<?>> Object getColValue(@NonNull final T entity, @NonNull final MetaCol col) {
        return access(entity).getObjectAccess().getProperty(entity, col);
    }
    public <T extends Entity<?>> void setColValue(@NonNull final T entity, @NonNull final MetaCol col, Object value) {
        access(entity).getObjectAccess().setProperty(entity, col, value);
    }

    public int executeCommand(@NonNull final SqlCmd cmd, int tenantId, Object[] args) {
        return this.jdbcTemplate.update(cmd.sql(), cmd.statementSetter(tenantId,args));
    }



    static void ensureNotEmpty(final List<?> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("list cannot be null or empty");
        }
    }
    static void requireTenantId(@NonNull final MetaObject metaObj, int tenantId) {
        if(BaseUtil.hasText(metaObj.getPartitionKey()) && tenantId <= 0){
            throw new IllegalArgumentException("tenantId must be greater than 0");
        }
    }


    //region where 条件

    /**
     * 按主键{@code id = ?}的查询条件，用于{@link #delete(Class, Object)}和{@link #update(Entity)}等
     * @param objAccess
     * @param key
     * @return Sql条件表达式
     * @param <T> 实体类型
     * @param <K> 主键类型
     */
    private static <T extends Entity<K>,K> SqlCriteriaExp keyCriteria(
            @NonNull final MetaObjectAccess<T,K> objAccess, K key) {
        var metaObj = objAccess.getMetaObject();
        var keyColArgMap = metaObj.isSingleKey()
                ? objAccess.argMapOfSingleKey(key)
                : objAccess.argMapOfCompositeKey(key);
        return SqlExp.allEqualsAnd(keyColArgMap);
    }
    private static <T extends Entity<?>> SqlCriteriaExp entityKeyCriteria(
            @NonNull final MetaObjectAccess<T,?> objAccess,
            @NonNull final T entity) {
        var keyColArgMap = objAccess.argMapOfEntityKeyCols(entity);
        return SqlExp.allEqualsAnd(keyColArgMap);
    }

    private static final String BY_ID = "ById";

    /**
     * 构建并缓存SQL条件表达式，使用命名的缓存条件表达式避免每次都创建和编译，可优化参数化查询性能。
     * <pre>
     *     {@code
     *      var cond = entityFactory.where(
     *          Partner.class, // 实体类
     *          "partnerNo contains", //命名条件表达式 partnerNo LIKE '%?%'
     *          () -> SqlExp.criteria(Partner::getPartnerNo).contains("ABC") //条件表达式构建函数
     *      );
     *     }
     * </pre>
     * 第二次调用此函数会命中缓存，避免了耗费资源的表达式编译过程。
     * @param entityClass 实体类
     * @param conditionName 条件名称作为缓存键值
     * @param condition 条件表达式构建函数
     * @return SQL条件表达式
     * @param <T> 实体类型
     */
    public  <T extends Entity<?>> SqlCriteriaExp where(Class<T> entityClass, final String conditionName, final Supplier<SqlCriteriaExp> condition){
        return access(entityClass).namedCondition(conditionName, condition);
    }

    /**
     * 内部使用{@code classAccess} 构建并缓存SQL条件表达式
     * @param classAccess 类访问器
     * @param conditionName 条件名称
     * @param condition 条件表达式函数
     * @return SQL条件表达式
     * @param <T> 实体类型
     */
    protected <T extends Entity<?>> SqlCriteriaExp where(EntityClassAccess<T,?> classAccess, final String conditionName, final Supplier<SqlCriteriaExp> condition){
        return classAccess.namedCondition(conditionName, condition);
    }

    /**
     * 构建主键查询条件表达式
     * @param entityClass 实体类
     * @return SQL条件表达式 (id = ?)
     * @param <T> 实体类型
     * @see #whereById(EntityClassAccess)
     */
    public <T extends Entity<?>> SqlCriteriaExp whereById(Class<T> entityClass) {
        return where(entityClass, BY_ID, () -> SqlExp.allEqualsAnd(access(entityClass).getMetaObject().getKeyCols()));
    }
    protected <T extends Entity<?>> SqlCriteriaExp whereById(EntityClassAccess<T,?> classAccess) {
        return where(classAccess, BY_ID, () -> SqlExp.allEqualsAnd(classAccess.getMetaObject().getKeyCols()));
    }

    /**
     * 构建实体对象{@code t}的主键查询表达式
     * @param t 实体对象
     * @return SQL条件表达式 (id = ?)
     * @param <T> 实体类型
     *
     * @see #whereById(Class)
     */
    public <T extends Entity<?>> SqlCriteriaExp whereById(T t) {
        return where(t.getClass(), BY_ID, () -> entityKeyCriteria(access(t).getObjectAccess(), t));
    }

    /**
     * 构建命名的查询条件表达式函数
     * @param name 条件名称
     * @param condition 条件表达式函数
     */
    protected static SqlCriteriaSupplier namedCondition(final String name, final Supplier<SqlCriteriaExp> condition){
        return new SqlCriteriaSupplier(name, condition);
    }
    //endregion of 条件

    //region command 常用命令

    /**
     * 提供给服务层构建缓存优化的SQL命令，
     * <pre>
     *     {@code
     *     var cmd = entityFactory.cmd(
     *          Partner.class,
     *          "UPDATE BY ID",
     *          () -> update(Partner.class, whereById(Partner.class, id))
     *     );
     *     }
     * </pre>
     * 查询和命令分离
     *
     * @see #createQuery()
     * @see #createQueryFrom(EntityClassAccess)
     *
     * @param entityClass 实体类
     * @param cmdName 命令唯一名称，用于缓存键值
     * @param cmdSupplier 命令构建函数
     * @return SQL 命令
     * @param <T> 实体类型
     */
    public <T extends Entity<?>> SqlCmd cmd(Class<T> entityClass, final String cmdName, final Supplier<SqlCmd> cmdSupplier) {
        return access(entityClass).command(cmdName, cmdSupplier);
    }

    public <T extends Entity<?>> SqlCmd cmdInsert(Class<T> entityClass) {
        return cmd(entityClass, INSERT, ()-> dialect.insert(access(entityClass).getMetaObject(), false));
    }

    public <T extends Entity<?>> SqlCmd cmdDeleteById(Class<T> entityClass) {
        var classAccess = access(entityClass);
        return cmd(entityClass, DELETE + BY_ID, () -> dialect.delete(
                classAccess.getMetaObject(),
                whereById(classAccess)
        ));
    }
    public <T extends Entity<?>> SqlCmd cmdDeleteByCondition(Class<T> entityClass, final String conditionName, Supplier<SqlCriteriaExp> condition) {
        var classAccess = access(entityClass);
        return cmd(entityClass, DELETE_BY + conditionName,
                () -> dialect.delete(
                        classAccess.getMetaObject(),
                        where(classAccess, conditionName, condition)
                )
        );
    }
    public <T extends Entity<?>> IDeleteFrom<T> cmdDelete(Class<T> entityClass){
        return new DeleteCommandBuilder<T>(this).delete(entityClass);
    }

    /**
     * 构建按主键更新整个实体的SQL命令
     *
     * @param entityClass 实体类
     * @return 可执行的SQL命令
     * @param <T> 实体类型
     * @see #update(Entity) 更新单个实体
     * @see #batchUpdate(List) 批量更新实体列表
     */
    public <T extends Entity<?>> SqlCmd cmdUpdateById(Class<T> entityClass) {
        var classAccess = access(entityClass);
        return cmd(entityClass, UPDATE + BY_ID, () -> dialect.update(
                classAccess.getMetaObject(),
                whereById(classAccess),
                false
        ));
    }
    /**
     * 构建复杂的更新命令
     * @param entityClass 实体类
     * @return 返回更新命令的SETTER
     * @param <T> 实体类型
     */
    public <T extends Entity<?>> IUpdateSetter<T> cmdUpdate(Class<T> entityClass){
        return new UpdateCommandBuilder<T>(this).update(entityClass);
    }
    //endregion of cmd

    //region insert 插入

    /**
     * 插入一个实体{@code t}
     * @param t 实体
     * @return 影响的记录数
     * @param <T> 实体类型
     * @throws DataAccessException
     */
    public <T extends Entity<?>> int insert(@NonNull final T t) throws DataAccessException {
        var cmd = cmdInsert(t.getClass());
        return executeCommand(cmd,getTenantId(t),access(t).getObjectAccess().argArrayOfInsertable(t));
    }

    /**
     * 批量插入实体列表{@code list}
     * @param list 实体列表
     * @return
     * @param <T> 实体类型
     * @throws DataAccessException
     */
    public <T extends Entity<?>> int[] batchInsert(@NonNull final List<T> list) throws DataAccessException {
        ensureNotEmpty(list);

        var t = list.get(0);
        var cmd = cmdInsert(t.getClass());
        var objAccess = access(t).getObjectAccess();
        return jdbcTemplate.batchUpdate(cmd.sql(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var it = list.get(i);
                cmd.setArgValues(ps, getTenantId(it), objAccess.argArrayOfInsertable(it));
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }
    //endregion of insert


    //region delete 删除

    /**
     * 根据{@code id}删除一个实体
     * @param entityClass 实体类
     * @param id 实体主键id
     * @return 影响记录数，成功返回1，实体不存在返回0
     * @param <T> 实体类型
     * @param <K> 实体主键类型
     * @throws DataAccessException
     *
     * @see #delete(Entity)
     */
    public <T extends Entity<K>, K> int delete(Class<T> entityClass, @NonNull final K id) throws DataAccessException {
        Objects.requireNonNull(id, "key cannot be null");
        var cmd = cmdDeleteById(entityClass);
        return executeCommand(cmd, getTenantId(id), access(entityClass,id).getObjectAccess().argArrayOfKey(id));
    }

    /**
     * 删除实体{@code t}，如果你只有实体的{@code id}，使用{@link #delete(Class, Object)}（此函数实际调用它）。
     * @param t 实体对象
     * @return 影响记录数，成功返回1，实体不存在返回0
     * @param <T> 实体类型
     * @throws DataAccessException
     *
     * @see #delete(Class, Object)
     */
    public <T extends Entity<?>> int delete(@NonNull final T t) throws DataAccessException {
        return delete(t.getClass(), t.getId());
    }

    /**
     * 批量删除列表中的实体，如果你只有主键 id 列表，使用{@link #batchDelete(Class, List)}
     * @param list 实体列表
     * @return 整形数组，其中 1 表示成功，0 未成功
     * @param <T> 实体类型
     * @throws DataAccessException 数据访问错误
     *
     */
    public <T extends Entity<?>> int[] batchDelete(@NonNull final List<T> list) throws DataAccessException {
        ensureNotEmpty(list);

        var t = list.getFirst();
        var cmd = cmdDeleteById(t.getClass());
        return jdbcTemplate.batchUpdate(cmd.sql(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var it = list.get(i);
                cmd.setArgValues(ps, getTenantId(it), it.getId());
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    /**
     * 批量删除主键 id 在{@code idList}中的实体
     * @param entityClass 实体类
     * @param idList 实体 id 列表
     * @return 整形数组，其中 1 表示成功，0 未成功
     * @param <T> 实体类型
     * @param <K> 实体主键类型
     * @throws DataAccessException 数据访问错误
     *
     * @see #batchUpdate(List) 删除一个实体 T 列表
     */
    public <T extends Entity<K>, K> int[] batchDelete(Class<T> entityClass, @NonNull final List<K> idList) throws DataAccessException {
        ensureNotEmpty(idList);
        var cmd = cmdDeleteById(entityClass);
        return jdbcTemplate.batchUpdate(cmd.sql(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var id = idList.get(i);
                cmd.setArgValues(ps, getTenantId(id), id);
            }

            @Override
            public int getBatchSize() {
                return idList.size();
            }
        });
    }

    private static final String DELETE_BY = DELETE + "BY ";

    /**
     * 条件删除实体
     * @param entityClass 实体类
     * @param condition 删除条件提供者，要求唯一命名{@link SqlCriteriaSupplier#name()}
     * @param tenantId 租户ID
     * @param args 参数值
     * @return 影响记录数
     * @param <T> 实体类型
     * @throws DataAccessException
     */
    public <T extends Entity<?>> int deleteBy(Class<T> entityClass, SqlCriteriaSupplier condition,
                                           int tenantId, Object...args) throws DataAccessException {
        Objects.requireNonNull(condition, "condition cannot be null");

        var classAccess = access(entityClass);
        var metaObj = classAccess.getMetaObject();

        //确保租户隔离
        requireTenantId(metaObj, tenantId);

        var cmd = cmdDeleteByCondition(entityClass, condition.name(), condition.delegate());
        return executeCommand(cmd, tenantId, args);
        //jdbcTemplate.update(cmd.sql(), cmd.statementSetter(tenantId, args));
    }


    //endregion of delete

    //region update 更新
    protected static int getTenantId(@NonNull final Object obj){
        if(obj instanceof Long id){
            return Tenancy.parseTenantID(id);
        }
        else if(obj instanceof Tenancy t){
            return t.getTenantID();
        }
        return 0;
    }
    /**
     * 更新一个实体{@code t}的所有可更新属性，不修改主键值
     * @param t 实体对象
     * @return 影响记录数
     * @param <T> 实体类型
     * @throws DataAccessException
     */
    public <T extends Entity<?>> int update(@NonNull final T t) throws DataAccessException {
        var classAccess = access(t);
        var objAccess = classAccess.getObjectAccess();
        var cmd = classAccess.command(UPDATE + BY_ID,
                () -> dialect.update(classAccess.getMetaObject(),
                        classAccess.namedCondition(BY_ID, () -> entityKeyCriteria(objAccess, t)),
                        false)
        );
        var argArray = Stream.concat(objAccess.argStreamOfUpdatable(t), objAccess.argStreamOfEntityKey(t)).toArray();
        return executeCommand(cmd, getTenantId(t), argArray);
    }

    /**
     * 更新实体{@code t}的部分属性{@code properties}
     * @param t 实体对象
     * @param properties 部分属性构建函数，采用{@link Partial#of(Object)}函数构建
     * @return 更新的记录数
     * @param <T> 实体类型
     * @throws DataAccessException
     */
    public <T extends Entity<?>> int updatePartial(T t, Partial<T> properties) throws DataAccessException {
        var classAccess = access(t);
        return updatePartialBy(
                classAccess.getEntityClass(),
                properties,
                namedCondition(BY_ID, () -> entityKeyCriteria(classAccess.getObjectAccess(), t)),
                getTenantId(t),
                t.getId() //byId
        );
    }

    /**
     * 更新单个实体的一个属性值。
     * 将主键为{@code key}的实体类{@code entityClass}的字段名称为{@code propertyName}属性值更新为{@code value}
     * @param entityClass 实体类
     * @param id 主键值，例如实体类的id属性
     * @param propertyName 属性名称
     * @param value 要更新的值
     * @return 影响的记录数，通常为1
     * @param <T> 实体类型
     * @param <K> 主键类型
     * @throws DataAccessException
     */
    public <T extends Entity<K>, K> int updateScalar(Class<T> entityClass, K id, String propertyName, Object value) throws DataAccessException {
        return updatePartialBy(
                entityClass,
                Map.of(propertyName, value),
                new SqlCriteriaSupplier(BY_ID, () -> whereById(entityClass)),
                getTenantId(id),
                id //byId
        );
    }

    /**
     * 更新单个实体的一个属性值。例如：
     * <pre>
     *     {@code
     *     //将 partnerID 为 1 的 partnerCode 更新为 ‘SY’
     *     var r = updateProperty(Partner.class, 1L, Partner::setPartnerCode, "SY");
     *     }
     * </pre>
     * 实际调用了{@link #updateScalar(Class, Object, String, Object)}，将Lambda表达式转化为字段名称。
     * @param entityClass 实体类
     * @param id 主键值，例如实体类的id属性
     * @param setter 实体的属性设置函数，传入<code>getter</code>也行
     * @param value 要更新的值
     * @return 影响的记录数，通常为1
     * @param <T> 实体类型
     * @param <K> 主键类型
     * @param <U> 属性类型
     * @throws DataAccessException
     */
    public <T extends Entity<K>, K, U> int updateScalar(Class<T> entityClass, K id, LambdaSetter<T,U> setter, U value) throws DataAccessException {
        return updateScalar(entityClass, id, LambdaUtil.getFieldName(setter), value);
    }

    /**
     * 批量更新实体，底层采用{@link BatchPreparedStatementSetter}一次性发送SQL语句，相比每个实体循环一次具有性能优势。
     * @param list 实体列表集合
     * @return 影响记录数组，1代表成功，0代表未实际更新
     * @param <T> 实体类型
     * @throws DataAccessException
     */
    public <T extends Entity<?>> int[] batchUpdate(@NonNull final List<T> list) throws DataAccessException {
        ensureNotEmpty(list);

        var t = list.getFirst();
        var classAccess = access(t);

        var metaObj = classAccess.getMetaObject();
        var objAccess = classAccess.getObjectAccess();
        var cmd = classAccess.command(UPDATE + BY_ID,
                () -> dialect.update(metaObj,
                        classAccess.namedCondition(BY_ID, () -> entityKeyCriteria(objAccess, t)),
                        false));

        return jdbcTemplate.batchUpdate(cmd.sql(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var it = list.get(i);
                var argArray = Stream.concat(objAccess.argStreamOfUpdatable(it), objAccess.argStreamOfEntityKey(it)).toArray();
                cmd.setArgValues(ps, getTenantId(it), argArray);
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    /**
     * 批量更新实体列表{@code list}的部分属性，属性由{@code getters}指定
     *
     * <p>
     *     你指定的{@code getters}必须是数据库中可更新的字段，且不能是主键。可更新字段指非计算字段、非自增 id
     * </p>
     * @param list 实体列表
     * @param getters 多个实体属性 getter
     * @return 整形数组，其中成功为 1，否则 0
     * @param <T> 实体类型
     * @throws DataAccessException 数据访问错误，例如你指定的 getters 非法
     */
    public <T extends Entity<?>> int[] batchUpdatePartial(@NonNull final List<T> list, LambdaGetter<T,?>...getters) throws DataAccessException {
        ensureNotEmpty(list);
        var t = list.getFirst();
        var classAccess = access(t);

        var metaObj = classAccess.getMetaObject();
        var objAccess = classAccess.getObjectAccess();
        var propertyList = Arrays.stream(getters).sequential()
                .map(getter -> LambdaUtil.getFieldName(getter))
                .collect(Collectors.joining(","));
        var cmd = classAccess.command(UPDATE + propertyList,
                () -> dialect.update(metaObj,
                        classAccess.namedCondition(BY_ID, () -> entityKeyCriteria(objAccess, t)),
                        false));

        return jdbcTemplate.batchUpdate(cmd.sql(), new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                var it = list.get(i);
                var argStream = Arrays.stream(getters).map(getter -> getter.get(it));
                var argArray = Stream.concat(argStream, objAccess.argStreamOfEntityKey(it)).toArray();
                cmd.setArgValues(ps, getTenantId(it), argArray);
            }

            @Override
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    /**
     * 更新所有符合条件{@code condition}的实体类{@code entityClass}的属性{@code properties}
     * <p>
     *     {@code properties}采用字符串为键值，键值必须是可更新的字段名，不要使用字符串字面量，例如：
     *     <pre>
     *         {@code
     *         Map.of("partnerName", partnerName); //Bad！ 字段名一修改，编译不会检查
     *         Map.of(_partnerName, partnerName);  //Good！ 使用 Partner.Meta._partnerName
     *         //多个字段建议使用 Partial<T> 构建
     *         Partial.of(partner)
     *          .with(Partner::getPartnerName)
     *          .with(Partner::getStatus)
     *          .with(Partner::getPartnerRoles)
     *          .result(); //build() 返回 Partial，而 result()直接返回结果 Map
     *         }
     *     </pre>
     * </p>
     * @param entityClass 实体类
     * @param properties 字段属性值集合(字典)
     * @param condition 条件表达式提供函数。注意这里提供的参数是形式参数，仅仅用于第一次构建和编译表达式
     * @param tenantId 租户id，若不区分多租户的可传入0
     * @param args 条件表达式中的参数值，注意这里是实际参数值
     * @return 影响记录数
     * @param <T> 实体类型
     * @throws DataAccessException 数据访问异常，例如属性集合键值是不可更新的字段
     *
     * @see #updatePartialBy(Class, Partial, SqlCriteriaSupplier, int, Object...)
     */
    public <T extends Entity<?>> int updatePartialBy(@NonNull final Class<T> entityClass,
                                                     @NonNull final Map<String, Object> properties,
                                                     @NonNull final SqlCriteriaSupplier condition,
                                                     int tenantId, Object...args) throws DataAccessException {
        Objects.requireNonNull(properties, "attributes cannot be null");
        if(properties.isEmpty()) throw new IllegalArgumentException("attributes cannot be empty");
        var classAccess = access(entityClass);
        var metaObj = classAccess.getMetaObject();

        //确保租户隔离
        requireTenantId(metaObj, tenantId);

        var cmdName = UPDATE + properties.keySet().stream().collect(Collectors.joining(",")) + WHERE + condition.name();
        var assignments = new LinkedHashMap<MetaCol, Object>();
        for (var attr : properties.entrySet()) {
            var col = metaObj.getCol(attr.getKey());
            if(col == null) throw new DataSourceLookupFailureException(attr.getKey() + " is not a column");
            if(col.isGenerated() || col.isComputed()) throw new DataAccessResourceFailureException(attr.getKey() + " is not an updatable column");
            assignments.put(col, attr.getValue());
        }
        var cmd = classAccess.command(cmdName,
            () -> dialect.update(metaObj, assignments,
                    classAccess.namedCondition(condition.name(), condition.delegate()),
                    false)
        );
        var argValues = args.length > 0
                //SET ...properties WHERE ...args
                ? Stream.concat(Stream.of(properties.values()), Stream.of(args)).toArray()
                : properties.values().toArray();
        return executeCommand(cmd, getTenantId(tenantId), argValues);
    }

    /**
     * 更新所有符合条件{@code condition}的实体类{@code entityClass}的一个或者多个属性{@code properties}。
     * 实际调用{@link #updatePartialBy(Class, Map, SqlCriteriaSupplier, int, Object...)}，
     * {@code Partial<T> properties}参数是为了然你可以使用Lambda表达式链式构建属性集合
     *
     * <pre>
     *     {@code
     *     // UPDATE Partner SET partnerRoles=?,status=? WHERE partnerID BETWEEN ? AND ? AND partnerCode=?
     *     var r = factory.updatePartialBy(Partner.class,
     *          Partial.of(partner)
     *              .with(Partner::getPartnerRoles) //更新 partnerRoles
     *              .with(Partner::getStatus) //更新 status
     *              .build(),
     *          namedCondition("byPartnerCode", ()->SqlExp.criteria(_partnerCode).eq("ABC")),// where partnerCode=?
     *          tenantId, // 多租户分区条件 partnerID BETWEEN minId AND maxId
     *          "ABC" //每次调用实际参数值可能变化，你可能会传入变量
     *      );
     *     }
     * </pre>
     * @param entityClass 实体类
     * @param properties 部分属性值抽取函数
     * @param condition 命名条件表达式提供函数，注意这里提供的参数是形式参数，仅仅用于第一次构建和编译表达式
     * @param tenantId 租户 id
     * @param args 调用时实际参数列表
     * @return 更新的记录数
     * @param <T> 实体类型
     * @throws DataAccessException 数据访问异常
     *
     */
    public <T extends Entity<?>> int updatePartialBy(@NonNull final Class<T> entityClass,
                                                     @NonNull final Partial<T> properties,
                                                     @NonNull final SqlCriteriaSupplier condition,
                                                     int tenantId, Object...args) throws DataAccessException {
        return updatePartialBy(entityClass, properties.get(), condition, tenantId, args);
    }
    //endregion of update

    //region find 查询

    private static final String FIND_BY_ID = SELECT + BY_ID;

    /**
     * 根据主键 {@code id}查找实体，如果不想抛出对象未找到异常，请使用{@link #findOptionalById(Class, Object)}
     * @param entityClass 实体类
     * @param id 主键 id
     * @return 返回实体对象或者抛出异常
     * @param <T> 实体类型
     * @param <K> 主键类型
     * @throws DataAccessException 未找到抛出异常 EmptyResultDataAccessException
     * 
     * @see #findOptionalById(Class, Object) 
     */
    public <T extends Entity<K>, K> T findById(Class<T> entityClass, @NonNull final K id) throws DataAccessException {
        var classAccess =  access(entityClass, (Class<K>) id.getClass());
        var objAccess = classAccess.getObjectAccess();
        var queryable = classAccess.queryable(FIND_BY_ID,
            () -> SqlQuery.create(dialect.getMetadataProvider())
                .from(classAccess.getMetaObject())
                .where(classAccess.namedCondition(BY_ID, () -> keyCriteria(objAccess,id)))
                .selectAll()
                .compile(dialect)
        );
        var argValues = queryable.getArgValues(getTenantId(id), objAccess.argArrayOfKey(id));
        return jdbcTemplate.queryForObject(queryable.sql(),classAccess.getRowMapper(),argValues);
    }

    /**
     * 根据主键 {@code id}查找实体，若不存在返回空而不像{@link #findById(Class, Object)}抛出异常
     * @param entityClass 实体类
     * @param id 主键 id
     * @return {@code  Optional<T>}，未找到返回{@link Optional#empty()}
     * @param <T> 实体类型
     * @param <K> 主键类型
     * @throws DataAccessException
     */
    public <T extends Entity<K>, K> Optional<T> findOptionalById(Class<T> entityClass, @NonNull final K id) throws DataAccessException {
        try{
            var t = findById(entityClass, id);
            return Optional.of(t);
        }
        catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
        catch(DataAccessException e){
            throw e;
        }
    }

    /**
     * 相当于 {@code Optional<findFirst()>}
     *
     * @see #findFirst(Class, SqlCriteriaSupplier, Sort, int, Object...)
     */
    public <T extends Entity<K>, K> Optional<T> findOptionalBy(Class<T> entityClass,
                                                               @NonNull final SqlCriteriaSupplier condition,
                                                               @Nullable final Sort sort,
                                                               int tenantId, Object...args) throws DataAccessException {
        try{
            var t = findFirst(entityClass, condition, sort, tenantId, args);
            return Optional.of(t);
        }
        catch(EmptyResultDataAccessException e){
            return Optional.empty();
        }
        catch(DataAccessException e){
            throw e;
        }
    }

    /**
     * 按{@code sort}规则排序，查找首个满足条件{@code condition}的实体对象
     * @param entityClass 实体类
     * @param condition 命名的查询条件表达式提供函数
     * @param sort 排序规则
     * @param tenantId 租户 id
     * @param args 多个条件查询实际参数值
     * @return 实体对象
     * @param <T> 实体类型
     * @throws DataAccessException 未找到或者访问异常
     *
     * @see #findFirst(Class, Sort, int)
     * @see #findFirst(Class, int)
     */
    public <T extends Entity<?>> T findFirst(Class<T> entityClass,
                                             @NonNull final SqlCriteriaSupplier condition,
                                             @Nullable final Sort sort,
                                             int tenantId, Object...args) throws DataAccessException {
        Objects.requireNonNull(condition, "condition cannot be null");
        var classAccess =  access(entityClass);

        // 确保租户隔离
        var metaObj = classAccess.getMetaObject();
        requireTenantId(metaObj, tenantId);

        var queryName = SELECT + ASTERISK + WHERE + condition.name();
        var q = classAccess.queryable(queryName,
                () -> createQueryFrom(classAccess)
                        .where(condition)
                        .orderBy(sort)
                        .selectAll().compile()
        ).orderBy(dialect,metaObj, classAccess.sortByOrDefault(sort));

        return jdbcTemplate.queryForObject(q.first(dialect),classAccess.getRowMapper(),q.getArgValues(tenantId, args));
    }

    /**
     * 按{@code sort}规则排序，查找首个实体对象
     * @param entityClass 实体类
     * @param sort 排序规则
     * @param tenantId 租户 id
     * @return 实体对象
     * @param <T> 实体类型
     * @throws DataAccessException 未找到或者访问异常
     *
     * @see #findFirst(Class, SqlCriteriaSupplier, Sort, int, Object...)
     * @see #findFirst(Class, int)
     */
    public <T extends Entity<?>> T findFirst(Class<T> entityClass, @Nullable final Sort sort, int tenantId) throws DataAccessException {
        return findFirst(entityClass, SqlCriteriaSupplier.EMPTY, sort, tenantId);
    }

    /**
     * 查找首个实体对象
     * @param entityClass 实体类
     * @param tenantId 租户 id
     * @return 实体对象
     * @param <T> 实体类型
     * @throws DataAccessException 未找到或者访问异常
     */
    public <T extends Entity<?>> T findFirst(Class<T> entityClass, int tenantId) throws DataAccessException {
        return findFirst(entityClass, SqlCriteriaSupplier.EMPTY, null, tenantId);
    }

    /**
     * 查找主键为 id 的实体的多个{@code getters}对应属性值
     *
     * <p>
     *     你自行负责这些属性有对应的数据库字段
     * </p>
     * @param entityClass 实体类
     * @param properties 多个属性名称集合
     * @param id 主键 id
     * @return 部分属性值
     * @param <T> 实体类型
     * @param <K> 主键类型
     * @throws DataAccessException
     */
    public <T extends Entity<?>, K> Partial<T> findPartial(Class<T> entityClass, @NonNull final Set<String> properties, @Nullable final K id) throws DataAccessException{
        var queryName = SELECT + String.join(",", properties)  + WHERE + BY_ID;
        var classAccess =  access(entityClass);
        var queryable = classAccess.queryable(queryName,
                () -> createQueryFrom(classAccess)
                        .whereById()
                        .select(properties.toArray(String[]::new))
                        .compile()
                );
        var argValues = queryable.getArgValues(getTenantId(id), id);
        return () -> jdbcTemplate.queryForMap(queryable.sql(),argValues);
    }


    /**
     * 查找主键为 id 的实体的单个{@code getter}对应的属性值
     * @param entityClass 实体类
     * @param id 主键 id
     * @param getter 属性 getter 函数指示你要获取的属性
     * @return 属性值
     * @param <T> 实体类型
     * @param <K> 主键类型
     * @param <U> 属性值类型
     * @throws DataAccessException
     */
    public <T extends Entity<?>, K, U> U findScalar(Class<T> entityClass, LambdaGetter<T,U> getter, @Nullable final K id) throws DataAccessException{
        Objects.requireNonNull(id, "id cannot be null");
        var fieldName = LambdaUtil.getFieldName(getter);
        var queryName = SELECT + fieldName + WHERE + BY_ID;

        var classAccess =  access(entityClass);
        var col = classAccess.getMetaObject().getCol(fieldName);
        if(col == null) throw new DataSourceLookupFailureException(fieldName + " column does not exist");
        var q = classAccess.queryable(queryName,
                () -> createQuery().from(entityClass)
                        .whereById()
                        .select(fieldName)
                        .compile()
                );
        var colDataClass = (Class<U>)col.getDataType().getJavaClass();
        var argValues = q.getArgValues(getTenantId(id), id);
        return jdbcTemplate.queryForObject(q.sql(), colDataClass, argValues);
    }


    //endregion of find

    //region list 列表

    public <T extends Entity<?>> List<T> listBy(Class<T> entityClass,
                                              @NonNull final SqlCriteriaSupplier condition,
                                              @Nullable final Sort sort,
                                              int tenantId, Object...args) throws DataAccessException {
        Objects.requireNonNull(condition, "condition cannot be null");
        var classAccess =  access(entityClass);

        // 确保租户隔离
        requireTenantId(classAccess.getMetaObject(), tenantId);

        var queryName = SELECT + WHERE + condition.name();
        var queryable = classAccess.queryable(queryName,
            () -> SqlQuery.create(dialect.getMetadataProvider())
                .from(classAccess.getMetaObject())
                .where(classAccess.namedCondition(condition.name(), condition.delegate()))
                .orderBy(classAccess.sortByOrDefault(sort))
                .selectAll()
                .compile(dialect)
        ).orderBy(dialect, classAccess.getMetaObject(), classAccess.sortByOrDefault(sort));
        return jdbcTemplate.query(queryable.sql(),classAccess.getRowMapper(),queryable.getArgValues(tenantId, args));
    }
    public <T extends Entity<?>> List<T> list(Class<T> entityClass, final Sort sort, int tenantId) throws DataAccessException {
        return listBy(entityClass,SqlCriteriaSupplier.EMPTY,sort,tenantId);
    }

    public <T extends Entity<?>> List<T> list(Class<T> entityClass, int tenantId) throws DataAccessException {
        return listBy(entityClass,SqlCriteriaSupplier.EMPTY,null,tenantId);

    }

    public <T extends Entity<?>> List<Map<String,Object>> listPartialBy(Class<T> entityClass,
                                              @NonNull final Set<String> properties,
                                              @NonNull final SqlCriteriaSupplier condition,
                                              @Nullable final Sort sort,
                                              int tenantId, Object...args) throws DataAccessException {
        Objects.requireNonNull(condition, "condition cannot be null");
        var classAccess =  access(entityClass);
        var metaObject = classAccess.getMetaObject();
        requireTenantId(metaObject, tenantId);// 确保租户隔离
        if(properties.stream().anyMatch(prop -> !metaObject.hasCol(prop))){
            throw new DataSourceLookupFailureException("column does not exist");
        }

        var queryName = SELECT + String.join(",", properties) + WHERE + condition.name();
        var q = classAccess.queryable(queryName,
                () -> createQueryFrom(classAccess)
                        .where(condition)
                        .orderBy(sort)
                        .select(properties.toArray(String[]::new))
                        .compile()
                ).orderBy(dialect,metaObject,classAccess.sortByOrDefault(sort));
        return jdbcTemplate.queryForList(q.sql(),q.getArgValues(tenantId, args));

    }
    public <T extends Entity<?>> List<Map<String,Object>> listPartial(Class<T> entityClass,
                                                                      @NonNull final Set<String> properties,
                                                                      @Nullable final Sort sort,
                                                                      int tenantId, Object...args) throws DataAccessException {
        return listPartialBy(entityClass,properties,SqlCriteriaSupplier.EMPTY,sort,tenantId,args);
    }

    public <T extends Entity<?>, U> List<U> listScalarBy(Class<T> entityClass,
                                                         LambdaGetter<T,U> getter,
                                                         @NonNull final SqlCriteriaSupplier condition,
                                                         @Nullable final Sort sort,
                                                         int tenantId, Object...args) throws DataAccessException{
        Objects.requireNonNull(condition, "condition cannot be null");
        var classAccess =  access(entityClass);
        var metaObject = classAccess.getMetaObject();
        requireTenantId(metaObject, tenantId);// 确保租户隔离
        var fieldName = LambdaUtil.getFieldName(getter);
        var col = metaObject.getCol(fieldName);
        if(col == null){
            throw new DataSourceLookupFailureException(fieldName + "column does not exist");
        }

        var queryName = SELECT + fieldName + WHERE + condition.name();
        var q = classAccess.queryable(queryName,
                () -> createQueryFrom(classAccess)
                        .where(condition)
                        .orderBy(sort)
                        .select(fieldName)
                        .compile()
        ).orderBy(dialect,metaObject,classAccess.sortByOrDefault(sort));
        var fieldClass = (Class<U>) col.getDataType().getJavaClass();
        return jdbcTemplate.queryForList(q.sql(),fieldClass,q.getArgValues(tenantId, args));
    }

    public <T extends Entity<?>, U> List<U> listScalar(Class<T> entityClass,
                                                       LambdaGetter<T,U> getter,
                                                       @Nullable final Sort sort,
                                                       int tenantId, Object...args) throws DataAccessException{
        return listScalarBy(entityClass,getter,SqlCriteriaSupplier.EMPTY,sort,tenantId,args);
    }
    //endregion of list

    //region page 分页查询
    public <T extends Entity<?>> PagedList<T> pageBy(Class<T> entityClass,
                                                     final Paginator paginator,
                                                     final SqlCriteriaSupplier condition,
                                                     int tenantId, Object...args) throws DataAccessException {
        Objects.requireNonNull(condition, "condition cannot be null");
        var classAccess =  access(entityClass);

        // 确保租户隔离
        var metaObj = classAccess.getMetaObject();
        requireTenantId(metaObj, tenantId);

        var queryName = SELECT + WHERE + condition.name();
        var q = classAccess.queryable(queryName,
                () -> createQueryFrom(classAccess)
                        .where(condition)
                        .orderBy(paginator.getSort())
                        .selectAll().compile()
        ).orderBy(dialect, metaObj, classAccess.sortByOrDefault(paginator.getSort()));

        var argValues = q.getArgValues(tenantId, args);

        var cnt = jdbcTemplate.queryForObject(q.countSql(),Integer.class, argValues);
        paginator.setRecordCount(cnt);

        var data = jdbcTemplate.query(
                q.pageOf(dialect, metaObj, paginator).sql(),
                classAccess.getRowMapper(),
                argValues);
        return new PagedList<>(data, paginator);
    }
    public <T extends Entity<?>> PagedList<T> pageBy(Class<T> entityClass,
                                                     int pageSize, int pageNo,
                                                     final Sort sort,
                                                     final SqlCriteriaSupplier condition,
                                                     int tenantId, Object...args) throws DataAccessException {
        return pageBy(entityClass, new Paginator(pageSize,pageNo,sort),condition,tenantId,args);
    }
    public <T extends Entity<?>> PagedList<T> page(Class<T> entityClass,
                                                   final Paginator paginator,
                                                   int tenantId) throws DataAccessException {
        return pageBy(entityClass,paginator,SqlCriteriaSupplier.EMPTY,tenantId);
    }

    public <T extends Entity<?>> PagedList<Map<String,Object>> pagePartialBy(Class<T> entityClass,
                                                     final Set<String> properties,
                                                     final Paginator paginator,
                                                     final SqlCriteriaSupplier condition,
                                                     int tenantId, Object...args) throws DataAccessException {
        Objects.requireNonNull(condition, "condition cannot be null");
        var classAccess =  access(entityClass);

        // 确保租户隔离
        var metaObj = classAccess.getMetaObject();
        requireTenantId(metaObj, tenantId);

        var fieldNames = properties.toArray(String[]::new);
        var queryName = SELECT + String.join(",", fieldNames) + WHERE + condition.name();
        var q = classAccess.queryable(queryName,
                () -> createQueryFrom(classAccess)
                        .where(condition)
                        .orderBy(paginator.getSort())
                        .select(fieldNames).compile()
        ).orderBy(dialect, metaObj, classAccess.sortByOrDefault(paginator.getSort()));

        var argValues = q.getArgValues(tenantId, args);

        var cnt = jdbcTemplate.queryForObject(q.countSql(),Integer.class, argValues);
        paginator.setRecordCount(cnt);

        var data = jdbcTemplate.queryForList(
                q.pageOf(dialect, metaObj, paginator).sql(),
                argValues);
        return new PagedList<>(data, paginator);
    }

    public <T extends Entity<?>> PagedList<Map<String,Object>> pagePartial(Class<T> entityClass,
                                                                             final Set<String> properties,
                                                                             final Paginator paginator,
                                                                             int tenantId, Object...args) throws DataAccessException {
        return pagePartialBy(entityClass,properties,paginator,SqlCriteriaSupplier.EMPTY,tenantId,args);
    }

    //endregion of find

    //region load 饿加载关联对象

    private RowMapper<?> getRelativeRowMapper(MetaRelation relation){
        var relativeClass = relation.getAccessGetter().type();
        if(relation.getRelationType() == MetaRelationType.HAS_MANY){
            var relativeMetaObj = metadataProvider.getRelativeMetaObject(relation);
        }
        var relativeClassAccess =  entityRegistries.get(relativeClass);
        return relativeClassAccess.getRowMapper();
    }

    private static final String WITH = " WITH ";
    private static final String EAGER = " EAGER";

    public <T extends Entity<K>, K> T findByIdWith(Class<T> entityClass, K id, LambdaGetter<T,?> relativeGetter) throws DataAccessException {
        Objects.requireNonNull(entityClass, "entityClass cannot be null");
        Objects.requireNonNull(id, "id cannot be null");

        var classAccess =  access(entityClass, id);
        var objAccess = classAccess.getObjectAccess();
        var relationName = LambdaUtil.getFieldName(relativeGetter);
        var relation = objAccess.getMetaObject().getRelation(relationName);
        if(relation == null) throw new IllegalArgumentException(relationName + " relation not found");

        var queryable = classAccess.queryable(FIND_BY_ID + WITH + relationName,
                () -> SqlQuery.create(dialect.getMetadataProvider())
                        .from(classAccess.getMetaObject())
                        .with(relativeGetter)
                        .where(classAccess.namedCondition(BY_ID, () -> keyCriteria(objAccess,id)))
                        .selectAll()
                        .compile(dialect)
        );
        var argValues = queryable.getArgValues(getTenantId(id), objAccess.argArrayOfKey(id));
        RowMapper<T> rowMapper = (rs, i) -> {
            var t = classAccess.getRowMapper().mapRow(rs, i);
            var r = getRelativeRowMapper(relation).mapRow(rs, i);
            objAccess.setProperty(t, relation, r);
            return t;
        };
        return jdbcTemplate.queryForObject(queryable.sql(),rowMapper,argValues);
    }
    public <T extends Entity<K>, K> T findByIdWith(@NonNull Class<T> entityClass, @NonNull K id, @NonNull LambdaGetter<T,?>...relativeGetters) throws DataAccessException {
        Objects.requireNonNull(entityClass, "entityClass cannot be null");
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(relativeGetters, "relativeGetters cannot be null");

        var classAccess =  access(entityClass, id);
        var objAccess = classAccess.getObjectAccess();
        var metaObj = objAccess.getMetaObject();
        var relations = Arrays.stream(relativeGetters)
                .map(LambdaUtil::getFieldName)
                .sorted()
                .map(metaObj::getRelation)
                .filter(r -> r != null)
                .collect(Collectors.toList());
        if(relations.isEmpty()) throw new IllegalArgumentException("relations not found by relative getters");

        var with = relations.stream().map(r -> r.getRelationName()).collect(Collectors.joining(","));
        var queryable = classAccess.queryable(FIND_BY_ID + WITH + with,
                () -> createQueryFrom(classAccess)
                        .withAll(relations)
                        .whereById()
                        .selectAll()
                        .compile()
        );

        var argValues = queryable.getArgValues(getTenantId(id), objAccess.argArrayOfKey(id));
        var rowMapper = createEagerRowMapper(entityClass, (Class<K>) id.getClass(), relations);//TODO 每次都创建不行
        return jdbcTemplate.queryForObject(queryable.sql(),rowMapper,argValues);
    }
    /**
     * 根据{@code  id} 饿加载实体对象，包括所有 HAS_ONE 关联对象
     * @param entityClass 实体类
     * @param id 主键值
     * @return
     * @param <T> 实体类邢
     * @param <K> 主键类型
     * @throws DataAccessException
     */
    public <T extends Entity<K>, K> T findByIdEager(@NonNull Class<T> entityClass, @NonNull K id) throws DataAccessException {
        Objects.requireNonNull(entityClass, "entityClass cannot be null");
        Objects.requireNonNull(id, "id cannot be null");

        var classAccess =  access(entityClass, id);

        //如果没有关联关系，则无需饿加载
        var metaObj = classAccess.getMetaObject();
        if(!metaObj.anyHasOneRelation()) return findById(entityClass, id);

        var objAccess = classAccess.getObjectAccess();
        var queryable = classAccess.queryable(FIND_BY_ID + EAGER,
                () -> createQueryFrom(classAccess)
                        .withAll()
                        .whereById()
                        .selectAll().compile()
        );
        var argValues = queryable.getArgValues(getTenantId(id), objAccess.argArrayOfKey(id));
        RowMapper<T> rowMapper = (rs, i) -> {
            var t = classAccess.getRowMapper().mapRow(rs, i);
            for(var relation : metaObj.getHasOneRelations()){
                var relativeProp = objAccess.getProperty(t, relation);
                if(relativeProp == null) continue;

                var relativeRowMapper = getRelativeRowMapper(relation);
                var r = relativeRowMapper.mapRow(rs, i);
                objAccess.setProperty(t, relation, r);
            }
            return t;
        };
        return jdbcTemplate.queryForObject(queryable.sql(),rowMapper,argValues);
    }

    private <T extends Entity<K>, K>  RowMapper<T> createEagerRowMapper(EntityClassAccess<T,K> classAccess) {
        return (rs, rowNum) -> {
            var objAccess = classAccess.getObjectAccess();
            var metaObj = objAccess.getMetaObject();
            var t = objAccess.newInstance();
            for(MetaCol col : metaObj.getCols()) {
                classAccess.setProperty(t, col, col.getResult(rs));
            }
            for(MetaRelation relation : metaObj.getHasOneRelations()) {
                var relativeProp = objAccess.getProperty(t, relation);
                if(relativeProp == null) continue;

                var relativeRowMapper = getRelativeRowMapper(relation);
                var r = relativeRowMapper.mapRow(rs, rowNum);
                objAccess.setProperty(t, relation, r);
            }
            return t;
        };
    }
    /**
     * 加载关联对象，支持`HAS_ONE`, `HAS_MANY`
     * <pre>
     *     {@code
     *     var partner = factory.findById(1L);
     *     factory.loadRelative(partner, Partner::getContactors); //加载伙伴的联系人
     *     //这时partner.getContactors()有值了
     *     }
     * </pre>
     * @param t 实体对象
     * @param relativeGetter 关联属性的Lambda表达式
     * @return 是否真正加载了
     * @param <T> 实体类型
     * @throws IllegalArgumentException
     * @throws DataAccessException
     */
    public <T extends Entity<?>> boolean loadRelative(@NonNull T t, @NonNull LambdaGetter<T,?> relativeGetter)
            throws IllegalArgumentException, DataAccessException {
        Objects.requireNonNull(t, "Entity t cannot be null");
        Objects.requireNonNull(relativeGetter, "relativeGetter cannot be null");

        var classAccess =  access(t);
        var objAccess = classAccess.getObjectAccess();
        var relationName = LambdaUtil.getFieldName(relativeGetter);
        var relation = objAccess.getMetaObject().getRelation(relationName);
        if(relation == null) throw new IllegalArgumentException(classAccess.getMetaObject().getObjName() + "." + relationName + " relation not found.");

        //关联属性未空，无需加载
        var relativeProp = objAccess.getProperty(t, relation);
        if(relativeProp == null) return false;

        var relativeMetaObj = metadataProvider.getRelativeMetaObject(relation);
        var queryable = classAccess.queryable(WITH + relationName,
                () -> SqlQuery.create(metadataProvider)
                        .from(relativeMetaObj)
                        .where(SqlJoin.of(objAccess.getMetaObject(), relation, relativeMetaObj).getJoinOnExp())
                        .selectAll()
                        .compile(dialect)
        );
        var argValues = queryable.getArgValues(getTenantId(t), objAccess.argArrayOfEntityKey(t));
        var relativeRowMapper = getRelativeRowMapper(relation);

        if(relation.getRelationType() == MetaRelationType.HAS_MANY){
            var relatives = jdbcTemplate.query(queryable.sql(), relativeRowMapper,argValues);
            objAccess.setProperty(t, relation, relatives);
        }
        else{
            try{
                var relative = jdbcTemplate.queryForObject(queryable.sql(), relativeRowMapper,argValues);
                objAccess.setProperty(t, relation, relative);
            }
            catch (Exception e){
                log.error("Failed to load relative one", e);
                objAccess.setProperty(t, relation, null);
            }
        }
        return true;
    }
    //endregion of load

    //region query 多表关联复杂查询

    public <T extends Entity<?>, R extends Entity<?>> SqlQuery.IQueryWhere query(Class<T> entityClass, final LambdaGetter<R,?> withOne, final String name){
        var classAccess = access(entityClass);
        var metaObj = classAccess.getMetaObject();
        return new SqlQuery.Builder(metadataProvider)
                .from(metaObj)
                .with(withOne);
    }

//    public <T extends Entity<?>> SqlQueryable getOrCreateQuery(EntityClassAccess<T,?> access, final String queryName, final Supplier<SqlQueryable> queryableSupplier) {
//        return access.queryable(queryName, queryableSupplier);
//    }

    public IEntitySource createQuery(){
        return new EntityQueryBuilder(this);
    }
    public <T extends Entity<?>> IEntityFilter createQueryFrom(EntityClassAccess<T,?> classAccess){
        return new EntityQueryBuilder(this).from(classAccess);
    }

    //region entity query builder 实体查询构建器

    public interface IEntitySource {
        <T extends Entity<?>> IEntityFilter from(Class<T> entityClass);
        <T extends Entity<?>> IEntityFilter from(EntityClassAccess<T,?> classAccess);
    }
    public interface IEntityFilter extends IEntitySortable{
        <T extends Entity<?>> IEntityFilter with(LambdaGetter<T,?> relative);
        IEntityFilter with(MetaRelation relation);
        IEntityFilter with(String relationName);
        IEntityFilter withAll();
        IEntityFilter withAll(Collection<MetaRelation> relations);
        /**
         * 设置命名的条件表达式提供函数，避免每次重新编译
         * @param name 条件表达式名称
         * @param condition Java 语言实现不了实体的条件表达式翻译未 Sql条件表达式，这里仍然采用底层的
         * @return
         */
        IEntitySortable where(String name, Supplier<SqlCriteriaExp> condition);
        IEntitySortable where(SqlCriteriaSupplier namedCondition);
        IEntitySortable whereById();
    }

    public interface IEntitySortable extends IEntityProjectable{
        IEntityProjectable orderBy(Sort sort);
        <T extends Entity<?>> IEntityProjectable orderBy(LambdaGetter<T,?>...fields);
        <T extends Entity<?>> IEntityProjectable orderByDesc(LambdaGetter<T,?>...fields);
    }

    public interface IEntityProjectable {
        <T extends Entity<?>> IEntityQueryable select(LambdaGetter<T,?>... fields);
        IEntityQueryable select(String... fields);
        IEntityQueryable selectAll();
        IEntityQueryable count();
        IEntityQueryable countDistinct();
    }

    public interface IEntityQueryable{
        SqlQueryable compile();
    }

    /**
     * 实体查询构建器是基于{@link SqlQuery.Builder}实现的使用实体类、实体工厂和实体类访问来构建查询。
     * 它与{@code SqlQuery.Builder}的区别是，一个是基于元数据，一个是基于上层的实体类型。
     * <p>
     *     实体类型查询借助{@link EntityClassAccess}处理了查询条件表达式自动缓存、子查询自动缓存。
     *     并且使得在实体工厂中编写构建{@link SqlQuery}的代码更加简洁。
     * </p>
     * <pre>
     *     {@code
     *     //原来我们在实体工厂中要这么构建一个可执行查询（SqlQueryable）
     *     var classAccess = access(entityClass);
     *     var q = SqlQuery.create(dialect.getMetadataProvider())
     *           .from(classAccess.getMetaObject())
     *           .where(classAccess.criteria(BY_ID, () -> whereById(classAccess)))
     *           .select(colName)
     *           .compile(dialect);
     *     //借助实体查询
     *     var q = createQuery(this)    //自动传入 factory.dialect, metadataProvider
     *          .from(entityClass)      //选择元对象，避免了上面多次出现 classAccess
     *          .with(...)              //调用底层
     *          .whereById()            //自动缓存查询条件
     *          .select(colName)        //调用底层
     *          .compiled();            //调用底层
     *     }
     * </pre>
     */
    protected class EntityQueryBuilder implements IEntitySource, IEntityFilter, IEntityQueryable {
        private final EntityFactory factory;
        private EntityClassAccess<?,?> access;
        private SqlQuery.IQueryWhere queryWhere;
        private SqlQuery.IQuerySortable querySortable;
        private SqlQuery.IQuerySelectable querySelectable;
        private SqlQuery query;
        public EntityQueryBuilder(final EntityFactory factory) {
            this.factory = factory;
        }


        /**
         * From语句从一个实体类开始，而底层是调用{@link SqlQuery.Builder}的{@code from(MetaObject)}
         * @param entityClass 实体类
         * @return
         * @param <T>
         */
        @Override
        public <T extends Entity<?>> IEntityFilter from(Class<T> entityClass){
            this.access = factory.access(entityClass);
            queryWhere = SqlQuery.create(factory.metadataProvider)
                    .from(access.getMetaObject());
            return this;
        }

        @Override
        public <T extends Entity<?>> IEntityFilter from(EntityClassAccess<T,?> classAccess){
            this.access = classAccess;
            queryWhere = SqlQuery.create(factory.metadataProvider)
                    .from(classAccess.getMetaObject());
            return this;
        }


        @Override
        public <T extends Entity<?>> IEntityFilter with(LambdaGetter<T, ?> relative) {
            queryWhere.with(relative);
            return this;
        }

        @Override
        public IEntityFilter with(MetaRelation relation) {
            queryWhere.with(relation);
            return this;
        }

        @Override
        public IEntityFilter with(String relationName) {
            queryWhere.with(relationName);
            return this;
        }

        @Override
        public IEntityFilter withAll() {
            queryWhere.withAll();
            return this;
        }

        @Override
        public IEntityFilter withAll(Collection<MetaRelation> relations) {
            queryWhere.withAll(relations);
            return this;
        }

        /**
         * Where语句代理，相比{@link SqlQuery.Builder}直接构建条件表达式，
         * 这里作了缓存处理，调用了{@link EntityClassAccess#namedCondition(String, Supplier)}
         * @param name 条件名称
         * @param condition 条件表达式提供函数
         */
        @Override
        public IEntitySortable where(final String name, Supplier<SqlCriteriaExp> condition){
            //通过access缓存查找，避免重复编译相同的条件表达式
            querySortable = queryWhere.where(access.namedCondition(name, condition));
            return this;
        }

        @Override
        public IEntitySortable where(SqlCriteriaSupplier namedCondition) {
            querySortable = queryWhere.where(access.namedCondition(namedCondition));
            return this;
        }

        @Override
        public IEntitySortable whereById() {
            querySortable = queryWhere.where(access.namedCondition(BY_ID, () -> factory.whereById(access)));
            return this;
        }

        @Override
        public IEntityProjectable orderBy(Sort sort) {
            querySelectable = querySortable.orderBy(access.sortByOrDefault(sort));
            return this;
        }

        @Override
        public <T extends Entity<?>> IEntityProjectable orderBy(LambdaGetter<T, ?>... fields) {
            querySelectable = querySortable.orderBy(fields);
            return this;
        }

        @Override
        public <T extends Entity<?>> IEntityProjectable orderByDesc(LambdaGetter<T, ?>... fields) {
            querySelectable = querySortable.orderByDesc(fields);
            return this;
        }

        @Override
        public <T extends Entity<?>> IEntityQueryable select(LambdaGetter<T, ?>... fields) {
            query = querySelectable.select(fields);
            return this;
        }

        @Override
        public IEntityQueryable select(String... fields) {
            query = querySelectable.select(fields);
            return this;
        }

        @Override
        public IEntityQueryable selectAll() {
            query = querySelectable.selectAll();
            return this;
        }

        @Override
        public IEntityQueryable count() {
            query = querySelectable.count();
            return this;
        }

        @Override
        public IEntityQueryable countDistinct() {
            query = querySelectable.countDistinct();
            return this;
        }

        @Override
        public SqlQueryable compile() {
            return query.compile(factory.dialect);
        }
    }
    //endregion of query builder

    //endregion of query

    //region command builder 多表关联更新和删除

    public interface IUpdateTarget<T extends Entity<?>>{
        IUpdateSetter<T> update(Class<T> entityClass);
    }

    public interface IUpdateSetter<T extends Entity<?>>{
        <R extends Entity<?>> IUpdateSetter<T> from(Class<R> fromClass, String alias, SqlCriteriaExp joinOn);
        IUpdateSetter<T> from(LambdaGetter<T,?> relatedGetter);
        IUpdateSetter<T> set(String colName);
        IUpdateSetter<T> set(String colName, SqlArithmeticExp value);
        IUpdateSetter<T> set(LambdaGetter<T,?> setter);
        IUpdateSetter<T> set(LambdaGetter<T,?> setter, SqlArithmeticExp value);
        ICommandWhere then();
        ICommandWhere setAllUpdatable();
    }
    public interface ICommandWhere extends ICommandBuilder {
        ICommandBuilder where(SqlCriteriaSupplier condition);
        ICommandBuilder where(String conditionName, Supplier<SqlCriteriaExp> delegate);
    }
    public interface ICommandBuilder {
        SqlCmd build();
    }
    public abstract class CommandBuilder<T extends Entity<?>> implements ICommandWhere, SqlMetaContext {
        protected final EntityFactory factory;
        protected EntityClassAccess<T,?> classAccess;
        protected List<SqlJoin> joins = new ArrayList<>();
        protected LinkedHashMap<MetaRelation,MetaObject> relatives = new LinkedHashMap<>();
        protected MetaObject target;
        protected String name;
        protected SqlCriteriaSupplier condition;

        @Override
        public MetaObject get() {
            return target;
        }

        @Override
        public LinkedHashMap<MetaRelation, MetaObject> getRelatives() {
            return relatives;
        }

        @Override
        public List<SqlJoin> getJoins() {
            return joins;
        }

        public CommandBuilder(EntityFactory factory) {
            this.factory = factory;
        }
        public ICommandBuilder where(SqlCriteriaSupplier condition){
            this.condition = condition;
            return this;
        }
        public ICommandBuilder where(String conditionName, Supplier<SqlCriteriaExp> delegate){
            this.condition = new SqlCriteriaSupplier(conditionName, delegate);
            return this;
        }
        public abstract SqlCmd build();
    }
    /**
     * 更新命令构建器用来构建复杂的SQL UPDATE命令，例如多表关联更新
     * <pre>
     *     {@code
     *     // 使用了Lambda表达式构建SQL命令，推荐使用 Partner.Meta._partnerCode 替代 Partner::getPartnerCode
     *     var cmd = entityFactory.cmdUpdate(Partner.class)
     *                 .from(PartnerCat.class, "c", criteria(PartnerCat::getCategoryID).eq(Partner::getCategoryID))
     *                 .set(Partner::getPartnerCode, exp(PartnerCat::getCategoryCode).plus(_partnerCode))
     *                 .then() //条件表达式 byCategoryID 会缓存
     *                 .where("byCategoryID", () -> criteria(Partner::getCategoryID).eq(PARAM_VALUE))
     *                 .build();
     *     //相当于如下SQL语句
     *     UPDATE t
     *          SET t.partnerCode = c.categoryCode + t.partnerCode
     *     FROM Partner AS t INNER JOIN PartnerCat AS c ON c.categoryID=t.categoryID
     *     WHERE t.categoryID = ?
     *     }
     * </pre>
     * @param <T> 实体类型
     */
    public class UpdateCommandBuilder<T extends Entity<?>> extends CommandBuilder<T>
            implements IUpdateTarget<T>, IUpdateSetter<T> {
        private LinkedHashMap<MetaCol,Object> assignments;//赋值语句

        public UpdateCommandBuilder(EntityFactory factory) {
            super(factory);
            this.assignments = new LinkedHashMap<>();
        }

        public IUpdateSetter<T> update(Class<T> entityClass){
            this.classAccess = factory.access(entityClass);
            this.target = classAccess.getMetaObject();
            return this;
        }

        public IUpdateSetter<T> from(LambdaGetter<T,?> relatedGetter){
            var relationName = LambdaUtil.getFieldName(relatedGetter);
            var relation = target.getRelation(relationName);
            if(relation == null) throw new IllegalArgumentException(relationName + " relation not found " + target.getObjName());
            joins.add(SqlJoin.of(target,relation,metadataProvider.getRelativeMetaObject(relation)));
            return this;
        }
        public <R extends Entity<?>> IUpdateSetter<T> from(Class<R> fromClass, String alias, SqlCriteriaExp joinOn){
            var relAccess = access(fromClass);
            var relMetaObj = relAccess.getMetaObject();
            var join = SqlJoin.hasOne(target,alias,relMetaObj,joinOn);
            joins.add(join);
            return this;
        }

//        public IUpdateSetter<T> from(SqlQuery subQuery, String alias, SqlCriteriaExp joinOn){
//
//        }

        public IUpdateSetter<T> set(String colName){
            var col = target.getCol(colName);
            assignments.put(col, SqlExp.PARAM_VALUE);
            return this;
        }
        public IUpdateSetter<T> set(String colName, SqlArithmeticExp value){
            var col = target.getCol(colName);
            assignments.put(col, value);
            return this;
        }
        public IUpdateSetter<T> set(LambdaGetter<T,?> setter){
            var fieldName = LambdaUtil.getFieldName(setter);
            var col = target.getCol(fieldName);
            assignments.put(col, SqlExp.PARAM_VALUE);
            return this;
        }
        public IUpdateSetter<T> set(LambdaGetter<T,?> setter, SqlArithmeticExp value){
            var fieldName = LambdaUtil.getFieldName(setter);
            var col = target.getCol(fieldName);
            assignments.put(col, value);
            return this;
        }

        public ICommandWhere setAllUpdatable(){
            target.getUpdatableCols().stream().forEach(col -> this.assignments.putIfAbsent(col, SqlExp.PARAM_VALUE));
            return this;
        }
        public ICommandWhere then(){
            return this;
        }
        @Override
        public ICommandBuilder where(SqlCriteriaSupplier condition){
            this.condition = condition;
            return this;
        }
        @Override
        public ICommandBuilder where(String conditionName, Supplier<SqlCriteriaExp> delegate){
            this.condition = new SqlCriteriaSupplier(conditionName, delegate);
            return this;
        }

        @Override
        public SqlCmd build(){
            //多表关联更新
            if(CollectionUtil.hasAny(joins)){
                return classAccess.command(name,
                        () -> factory.dialect.update(this, assignments,
                                classAccess.namedCondition(condition.name(), condition.delegate()),
                                false
                        )
                );
            }
            //单表更新
            return classAccess.command(name,
                    () -> factory.dialect.update(target, assignments,
                            classAccess.namedCondition(condition.name(), condition.delegate()),
                            false
                    )
            );
        }
    }


    public interface IDeleteTarget<T extends Entity<?>>{
        IDeleteFrom<T> delete(Class<T> entityClass);
    }
    public interface IDeleteFrom<T extends Entity<?>> extends ICommandWhere {
        <R extends Entity<?>> IDeleteFrom<T> from(Class<R> fromClass, String alias, SqlCriteriaExp joinOn);
        IDeleteFrom<T> from(LambdaGetter<T,?> relatedGetter);
    }
    /**
     * 删除命令构建器用来构建复杂的SQL DELETE命令
     * <pre>
     *     {@code
     *      var cmdDeleteFrom = entityFactory.cmdDelete(Partner.class)
     *                 .from(Region.class, "r", criteria(Region::getRegionCode).eq(Partner::getRegionCode))
     *                 .from(Partner::getCategory) //元关系有关联类别HAS_ONE定义
     *                 .where("byCategoryCodeAndStatusEq",
     *                         ()->criteria(PartnerCat::getCategoryCode).eq("ABC")
     *                                 .and(criteria(Partner::getStatus).eq(0))
     *                 )
     *                 .build();
     *
     *     //相当于如下SQL语句
     *     DELETE t
     *     FROM Partner AS t INNER JOIN Region r ON r.regionCode=t.regionCode
     *          INNER JOIN PartnerCat AS category ON categoryID=t.categoryID
     *     WHERE (category.categoryCode='ABC') AND (t.status=0)
     *     }
     * </pre>
     * @param <T> 实体类型
     */
    public class DeleteCommandBuilder<T extends Entity<?>> extends CommandBuilder<T>
        implements IDeleteTarget<T>,IDeleteFrom<T> {

        public DeleteCommandBuilder(EntityFactory factory) {
            super(factory);
        }
        public IDeleteFrom<T> delete(Class<T> entityClass){
            this.classAccess = factory.access(entityClass);
            this.target = classAccess.getMetaObject();
            return this;
        }

        @Override
        public <R extends Entity<?>> IDeleteFrom<T> from(Class<R> fromClass, String alias, SqlCriteriaExp joinOn) {
            var relAccess = access(fromClass);
            var relMetaObj = relAccess.getMetaObject();
            var join = SqlJoin.hasOne(target,alias,relMetaObj,joinOn);
            joins.add(join);
            return this;
        }

        @Override
        public IDeleteFrom<T> from(LambdaGetter<T,?> relatedGetter) {
            var relationName = LambdaUtil.getFieldName(relatedGetter);
            var relation = target.getRelation(relationName);
            if(relation == null) throw new IllegalArgumentException(relationName + " relation not found " + target.getObjName());
            joins.add(SqlJoin.of(target,relation,metadataProvider.getRelativeMetaObject(relation)));
            return this;
        }


        @Override
        public SqlCmd build() {
            //多表关联删除
            if(CollectionUtil.hasAny(joins)){
                return classAccess.command(name,
                        () -> factory.dialect.delete(this,
                                classAccess.namedCondition(condition.name(), condition.delegate())
                        )
                );
            }
            //单表删除
            return classAccess.command(name,
                    () -> factory.dialect.delete(target,
                            classAccess.namedCondition(condition.name(), condition.delegate())
                    )
            );
        }
    }
    //endregion of command builder

    //region entity set 实体集合
    /**
     * 实体集合提供实体{@code T}的CRUD操作，
     *
     * <p>
     * 相比直接使用{@link EntityFactory}更简洁。
     * 但是只支持单表操作，若需要多表关联、子查询等，还需借助{@code EntityFactory}
     * </p>
     * @remarks 它只在<code>EntityFactory</code>内部存在（非静态类）
     * @param <T> 实体类型
     * @param <K> 主键类型
     */
    public class EntitySet<T extends Entity<K>,K> {
        private final EntityFactory factory;
        private final Class<T> entityClass;
        private final Class<K> keyClass;
        @Getter
        private final MetaObject metaObject;

        public EntitySet(EntityFactory factory, Class<T> entityClass, Class<K> keyClass) {
            this.factory = factory;
            this.entityClass = entityClass;
            this.keyClass = keyClass;
            this.metaObject = factory.getMetaObject(entityClass,keyClass);
        }
        //region 增加 insert
        public int insert(T t) throws DataAccessException{
            return factory.insert(t);
        }
        public int[] batchInsert(List<T> list) throws DataAccessException{
            return factory.batchInsert(list);
        }
        //endregion of insert

        //region 删除 delete
        public int deleteById(K id){
            return factory.delete(entityClass, id);
        }

        public int deleteAll(){
            return factory.deleteBy(entityClass, null, 0);
        }

        /**
         * 根据命名的条件删除所有符合条件的实体数据。
         * <p>
         *     传入条件名称和条件表达式Lambda形式是为了
         *     使用命名条件{@link EntityFactory#namedCondition(String, Supplier)}，
         *     以避免每次重新编译条件表达式，优化性能。
         * </p>
         * <pre>
         *     {@code
         *     var partners = baseEntityFactory.Partners;
         *     //简单条件直接使用 SqlExp.criteria 构建
         *     var r = partners.deleteBy("status小于0", () -> SqlExp.criteria(_status).lt(0));
         *     //复杂条件使用 EntitySet.createExp() 构建，实际上调用 SqlExp.create(metaObject)
         *     var q = partners.deleteBy("极为复杂的条件",
         *      () -> partners
         *          .createExp(Partner::getStatus).lt(0)
         *          .and(Partner::getPartnerRoles).eq(8)
         *          .end()
         *     )
         *     }
         * </pre>
         * @param conditionName 条件名称
         * @param condition 条件表达式
         * @return 删除的记录数
         */
        public int deleteBy(final String conditionName, Supplier<SqlCriteriaExp> condition){
            return factory.deleteBy(entityClass, namedCondition(conditionName, condition) , 0);
        }

        //endregion of delete

        //region 更新 update

        /**
         * 更新单个实体对象
         * @param t 实体对象
         * @return 成功返回 1，否则为 0
         * @throws DataAccessException
         */
        public int update(@NonNull final T t) throws DataAccessException{
            return factory.update(t);
        }

        /**
         * 更新实体对象{@code t}的部分属性{@code partial}
         * <pre>
         *     {@code
         *      var partner = new Partner();//假设你有一个实体
         *      // 以下更新这个实体的三个字段值
         *      var r = factory.updatePartial(partner,
         *              //这么构建部分实体属性集合，也可以使用Partner.Meta中的字符串常量
         *              Partial.of(partner).with(Partner::getPartnerCode)
         *                  .with(Partner::getPartnerName)
         *                  .with(_shortName, "简称") //使用Partner.Meta._shortName，不要使用 "shortName"
         *                  .build()
         *          );
         *     }
         * </pre>
         * @param t 实体对象
         * @param partial 实体的部分属性构建函数
         * @return 更新的记录数
         * @throws DataAccessException
         */
        public int updatePartial(@NonNull final T t, Partial<T> partial) throws DataAccessException{
            return factory.updatePartial(t, partial);
        }

        /**
         * 更新一个实体主键为{@code id}的属性{@code propertyName}值为{@code value}
         * @param id 实体 id
         * @param propertyName 属性名称
         * @param value 属性值
         * @return 成功放回 1，否则为 0
         * @param <U> 属性类型
         * @throws DataAccessException
         *
         * @see #updateScalar(Object, LambdaSetter, Object)
         */
        public <U> int updateScalar(@NonNull K id, @NonNull final String propertyName, U value) throws DataAccessException{
            return factory.updateScalar(entityClass,id,propertyName,value);
        }

        /**
         * 更新一个实体主键为{@code id}的属性{@code propertyGetter}值为{@code value}
         *
         * @param id 实体 id
         * @param propertySetter 属性 setter 函数
         * @param value 属性值
         * @return 成功放回 1，否则为 0
         * @param <U> 属性类型
         * @throws DataAccessException
         *
         * @see #updateScalar(Object, String, Object)
         */
        public <U> int updateScalar(@NonNull K id, @NonNull final LambdaSetter<T,U> propertySetter, U value) throws DataAccessException{
            return factory.updateScalar(entityClass, id, LambdaUtil.getFieldName(propertySetter), value);
        }


        /**
         * 批量更新实体列表，实际调用{@link EntityFactory#batchUpdate(List)}
         *
         * @param list 实体列表
         * @return 整形数组，其中为1表示更新成功，0未更新
         * @throws DataAccessException
         *
         */
        public int[] batchUpdate(@NonNull final List<T> list) throws DataAccessException{
            return factory.batchUpdate(list);
        }

        /**
         * 批量更新所有符合条件{@code condition}的实体的多个属性{@code properties}
         * @param properties 要更新的属性集合
         * @param condition 更新条件，需要命名的Lambda条件表达式
         * @param tenantId 租户 id
         * @param args 条件参数
         * @return 更新的记录数
         * @throws DataAccessException
         *
         * @see #batchUpdatePartial(Partial, SqlCriteriaSupplier, int, Object...)
         */
        public int batchUpdatePartial(@NonNull final Map<String, Object> properties,
                                 @NonNull final SqlCriteriaSupplier condition,
                                 int tenantId, Object...args) throws DataAccessException{
            return factory.updatePartialBy(entityClass, properties, condition, tenantId, args);
        }
        public int batchUpdatePartial(@NonNull final Partial<T> properties,
                                      @NonNull final SqlCriteriaSupplier condition,
                                      int tenantId, Object...args) throws DataAccessException{
            return factory.updatePartialBy(entityClass, properties.get(), condition, tenantId, args);
        }
        /**
         * 批量更新符合条件{@code condition}的实体的单个属性{@code propertyName}值
         * @param propertyName 属性名称
         * @param value 要更新的属性值
         * @param condition 更新条件，需要命名的Lambda条件表达式
         * @param tenantId 租户 id
         * @return 更新的记录数
         * @param <U> 属性类型
         * @throws DataAccessException
         * 
         * @see #batchUpdateScalar(LambdaSetter, Object, SqlCriteriaSupplier, int) 
         */
        public <U> int batchUpdateScalar(@NonNull final String propertyName, U value, @NonNull final SqlCriteriaSupplier condition, int tenantId) throws DataAccessException{
            var properties = Map.of(propertyName, (Object) value);
            return factory.updatePartialBy(entityClass, properties, condition, tenantId, value);
        }

        /**
         * 批量更新符合条件{@code condition}的实体的单个属性{@code propertySetter}值
         * @param propertySetter 属性 setter 函数
         * @param value 要更新的属性值
         * @param condition 更新条件，需要命名的Lambda条件表达式
         * @param tenantId 租户 id
         * @return 更新的记录数
         * @param <U> 属性类型
         * @throws DataAccessException
         * 
         * @see #batchUpdateScalar(String, Object, SqlCriteriaSupplier, int) 
         */
        public <U> int batchUpdateScalar(@NonNull final LambdaSetter<T,U> propertySetter, U value, @NonNull final SqlCriteriaSupplier condition, int tenantId) throws DataAccessException{
            var properties = Map.of(LambdaUtil.getFieldName(propertySetter), (Object) value);
            return factory.updatePartialBy(entityClass, properties, condition, tenantId, value);
        }

        //endregion of update

        //region 创建表达式 createExp
        public SqlExp.ISqlArithmetic createExp(final String colName){
            return SqlExp.create(metaObject)
                    .start(colName);
        }
        public SqlExp.ISqlArithmetic createExp(LambdaGetter<T,?> getter){
            return SqlExp.create(metaObject)
                    .start(LambdaUtil.getFieldName(getter));
        }
        //endregion of 表达式


        public List<T> listAll(){
            return factory.list(entityClass, 0);
        }
        public T first(){
            return factory.findFirst(entityClass,0);
        }
    }
    //endregion
}

