package cloud.mmda.core.services;

import cloud.mmda.core.data.jdbc.metadata.DbMetadataProvider;
import cloud.mmda.core.models.ChangeLog;
import cloud.mmda.core.entities.EntityFilter;
import cloud.mmda.core.enums.ChangeType;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.metadata.MetaUi;
import cloud.mmda.core.data.pagination.*;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.caching.CachePolicy;
import cloud.mmda.core.security.models.UserAccount;

import java.time.Duration;
import java.util.*;

/**
 * 领域服务接口，所有商业逻辑服务实现此接口提供元数据访问、基础增删改查（CRUD）、组装领域实体的Value Objects能力
 * @param <T> 实体类型
 * @param <K> 健类型
 */
public interface DomainService<T, K> {

    DbMetadataProvider getMetadataProvider();

    K asKey(T t);
    /**
     * 将等式集合转化为SqlExpression表达式
     * @param attributes 等式集合
     * @return SqlExpression
     */
    SqlExpression buildExpression(Map<String, Object> attributes);
    Sort getDefaultSort();

    /**
     * T对应的元数据对象
     * @return
     */
    MetaObject getMetaObject();

    /**
     * 获取指定数据库表的元数据对象
     * @param dbSchema 数据库
     * @param objName 表
     * @return
     */
    MetaObject getMetaObject(String dbSchema, String objName);

    /**
     * 获取T及关联元数据对象，组装为map，
     * 主表键值为master，子表为关系名称，如items
     * @return
     */
    Map<String,MetaObject> getMetadata();
    /**
     * T 对应locale语言的元界面
     * @param locale 语言区域，如zh, zh-Hant, en
     * @param p 缓存策略
     * @param reload 重新从数据库载入缓存
     * @return
     */
    MetaUi getMetaUi(String locale, CachePolicy p, boolean reload);

    /**
     * 获取实体过滤器
     * @param locale
     * @param p
     * @param reload 重新从数据库载入缓存
     * @return
     */
    List<EntityFilter> getEntityFilters(String locale, CachePolicy p, boolean reload);

    List<EntitySortSet> getEntitySorts(String locale, CachePolicy p, boolean reload);
    /**
     * 默认读取后缓存5分钟
     * @param tenantID 租户标识
     * @return 返回读取后5分钟的默认缓存策略
     */
    default CachePolicy getDefaultCachePolicy(int tenantID){
        return CachePolicy.of(tenantID,CachePolicy.READ_THRU,CachePolicy.DEF_EXPIRED_5_MINUTES);
    }

    /**
     * 为客户端创建一个空的实体模板，
     * 生成多租户ID，设置默认值，客户端基于此模板编辑，
     * 实现类可自行添加createBy(CachePolicy p, P prototype)函数实现数据传输
     * @param p 缓存策略
     * @return
     */
    T create(CachePolicy p);

    //insert
    int insert(T t, CachePolicy p);
    default T insertAndGet(T t, CachePolicy p) {
        insert(t, p);
        return get(asKey(t),p);
    }
    default T insertAndLoad(T t, CachePolicy p) {
        insert(t, p);
        return load(asKey(t),p);
    }

    //insertMany

    //delete
    int delete(T t, CachePolicy p);
    int deleteByKey(K k, CachePolicy p);
    default int delete(T t){
        return delete(t, CachePolicy.CACHE_NONE);
    }
    default int deleteByKey(K k){
        return deleteByKey(k, CachePolicy.CACHE_NONE);
    }
    //int deleteByKey(K k, SqlExpression condition, CachePolicy p);

    //deleteAll
    int deleteAll(String condition, CachePolicy p, Object... args);
    int deleteAll(SqlExpression condition, CachePolicy p);
    default int deleteAll(Map<String,Object> condition,CachePolicy p) {
        return deleteAll(buildExpression(condition),p);
    };
    int deleteAll(final Collection<K> keys, final CachePolicy p);

    //update
    int update(T t, CachePolicy p);
    default T updateAndGet(T t, CachePolicy p){
        update(t, p);
        return get(asKey(t), p);
    }
    default T updateAndLoad(T t, CachePolicy p){
        update(t, p);
        return load(asKey(t), p);
    }
    //updateAll
    int updateAll(Collection<T> tCollection, CachePolicy p); //批量修改实体

    //updatePartial 批量修改实体部分属性
    int partialUpdate(K k, SqlExpression attributes, SqlExpression condition, CachePolicy p);
    default int partialUpdate(K k, Map<String,Object> attributes, Map<String,Object> condition, CachePolicy p){
        return partialUpdate(k, buildExpression(attributes), buildExpression(condition),p);
    }
    default int partialUpdate(K k, SqlExpression attributes, CachePolicy p){
        return partialUpdate(k, attributes, null, p);
    }
    default int partialUpdate(K k, Map<String, Object> attributes, CachePolicy p) {
        return partialUpdate(k, buildExpression(attributes), null, p);
    };

    //partialUpdateAll
    int partialUpdateAll(Collection<Map<String, Object>> listOfKeyAndAttributes, CachePolicy p);

    int partialUpdateAll(SqlExpression attributes, SqlExpression condition, CachePolicy p);
    default int partialUpdateAll(Map<String, Object> attributes, Map<String, Object> condition, CachePolicy p) {
        return partialUpdateAll(buildExpression(attributes), buildExpression(condition), p);
    };

    //get
    T get(K k, CachePolicy p);
    /**
     * 载入整个实体，默认只载入本身
     *
     * 留给子类实现一并载入子表、关联表和引用属性
     * @param k 主键值
     * @param p 缓存策略
     * @return
     */
    default T load(K k, CachePolicy p){
        return get(k, p);
    }

    //save
    int save(T t, CachePolicy p, boolean checkExists, Map<String,Object> changeLog);
    default int save(T t, CachePolicy p, Map<String,Object> changeLog){
        return save(t,p,false,changeLog);
    }
    default int save(T t, CachePolicy p, boolean checkExists){
        return save(t,p,checkExists,null);
    }
    default int save(T t, Map<String,Object> changeLog){
        return save(t,CachePolicy.CACHE_NONE, false, changeLog);
    }
    default int save(T t){
        return save(t,CachePolicy.CACHE_NONE, false, null);
    }
    default T saveAndGet(T t, CachePolicy p, boolean checkExists){
        save(t, p, checkExists, null);
        return get(asKey(t), p);
    }
    default T saveAndGet(T t, CachePolicy p){
        return saveAndGet(t,p,false);
    }
    default T saveAndLoad(T t, CachePolicy p, boolean checkExists){
        save(t, p, checkExists, null);
        return load(asKey(t), p);
    }
    default T saveAndLoad(T t, CachePolicy p){
        return saveAndLoad(t, p, false);
    }

    Optional<T> getOrDefault(K k, CachePolicy p, T d);
    default Optional<T> getOrDefault(K k, CachePolicy p){
        return getOrDefault(k, p, null);
    }

    //getAny
    T getAny(String condition, CachePolicy p, Object... args);
    T getAny(SqlExpression condition, CachePolicy p);
    default T getAny(Map<String, Object> condition, CachePolicy p) {
        return getAny(buildExpression(condition),p);
    };
    Optional<T> getAnyOrDefault(String condition, CachePolicy p, T d, Object... args);
    default Optional<T> getAnyOrEmpty(String condition, CachePolicy p, Object... args){
        return getAnyOrDefault(condition, p, null, args);
    }
    //getAll
    List<T> getAll(CachePolicy p);
    List<T> getAll(Sort sort, CachePolicy p);
    PagedList<T> getAll(Paginator paginator, CachePolicy p);

    //getAllBy
    List<T> getAllBy(SqlExpression condition, Sort sort, CachePolicy p);
    default List<T> getAllBy(Map<String, Object> condition, Sort sort, CachePolicy p) {
        return getAllBy(buildExpression(condition), sort,p);
    }
    List<T> getAllBy(SqlExpression condition, CachePolicy p);
    default List<T> getAllBy(Map<String, Object> condition, CachePolicy p) {
        return getAllBy(buildExpression(condition),p);
    }
    List<T> getAllBy(String condition, Sort sort, CachePolicy p, Object... args);
    default List<T> getAllBy(String condition, CachePolicy p, Object... args){
        return getAllBy(condition,getDefaultSort(),p,args);
    }

    //getAllBy with pager
    PagedList<T> getAllBy(Paginator paginator, String condition, CachePolicy p, Object... args);
    PagedList<T> getAllBy(Paginator paginator, SqlExpression condition, CachePolicy p);
    default PagedList<T> getAllBy(Paginator paginator, Map<String, Object> condition, CachePolicy p) {
        return getAllBy(paginator, buildExpression(condition),p);
    }

    //getAllIn or not in
    List<T> getAllIn(String field, String list, boolean ordered, CachePolicy p, String condition, Object...args);
    default List<T> getAllIn(String field, String list, boolean ordered, CachePolicy p){
        return getAllIn(field,list,ordered,p,null);
    }
    PagedList<T> getAllNotIn(Paginator paginator, String field, String list, CachePolicy p, String condition, Object...args);
    default PagedList<T> getAllNotIn(Paginator paginator, String field, String list, CachePolicy p){
        return getAllNotIn(paginator,field,list,p,null);
    }

    //searchAll
    PagedList<T> searchAll(Paginator paginator, String word, CachePolicy p);
    PagedList<T> searchAll(Paginator paginator, String word, CachePolicy p, String condition, Object...args);

    List<T> searchAll(String word, Sort sort, CachePolicy p);
    List<T> searchAll(String word, Sort sort, CachePolicy p, String condition, Object...args);

    //count and exists
    int count(final CachePolicy p);
    int countBy(final String condition, final CachePolicy p, Object... args);
    int countBy(final SqlExpression condition, final CachePolicy p);
    default int countBy(final Map<String,Object> condition, final CachePolicy p){
        return countBy(buildExpression(condition), p);
    }
    int countBy(final String word, final String condition, final CachePolicy p, Object... args);
    int countBySearchWord(final String word,  final CachePolicy p);
    boolean exists(K k, CachePolicy p);
    //export
    int prepareExportRecordCount(final String word,final Map<String, Object> params, final String condition, final CachePolicy p) ;
    PagedList<T> getAllExport(Paginator paginator, String word, CachePolicy p, String condition, Object...args);
    PagedList<T> getAllExportWithParams(Paginator paginator, String word, CachePolicy p, Map<String, Object> params, String condition, Object...args) ;

    //assemble
    default CachePolicy getCachePolicyOfAssemblyOne(int tenantID){
        //组装一对一关联对象默认使用的缓存策略
        return CachePolicy.of(tenantID,CachePolicy.READ_THRU,CachePolicy.DEF_EXPIRED_2_HOURS);
    }
    default CachePolicy getCachePolicyOfAssemblyMany(int tenantID){
        //组装一对多关联对象默认使用的不缓存策略
        return CachePolicy.of(tenantID,CachePolicy.NONE, Duration.ZERO);
    }
    /**
     * 组装一对一关联对象
     * 默认单表无需组装，子类应该覆盖自行组装一对一关联对象
     * @param t 实体对象
     * @return 返回组装后的实体对象
     */
    default T assembleOne(final T t) {
        return t;
    }

    /**
     * 组装一对多关联对象
     * @param t 实体对象
     * @return 返回组装后的实体对象
     */
    default T assembleMany(final T t) {
        return t;
    }

    /**
     * 仅组装关联对象和子表，常用于service层商业逻辑处理。
     * 引用属性的文本和枚举属性的文本对于业务层没有意义。
     * @param t
     * @return
     */
    default T assembleOneAndMany(final T t){
        return assembleOne(assembleMany(t));
    }

    /**
     * 组装引用属性
     * @param t
     * @return
     */
    T assembleRefProperties(final T t);

    /**
     * 组装枚举文本属性
     * @param t
     * @return
     */
    T assembleEnumProperties(final T t);

    /**
     * 组装单行数据，包括一对一关联对象、枚举、引用数据
     * @param t
     * @return
     */
    default T assembleSingle(final T t){
        return assembleEnumProperties(
                assembleRefProperties(
                        assembleOne(t)
                )
        );
    }

    /**
     * 组装所有关联数据，包括一对一关联对象、一对多子表、枚举和引用属性数据
     * @param t
     * @return
     */
    default T assemble(final T t){
        return assembleEnumProperties(
                assembleRefProperties(
                        assembleOneAndMany(t)
                )
        );
    }

    /**
     * 组装实体操作
     * @param t
     * @return
     */
    default T assembleActions(final T t, UserAccount user, Locale locale){
        return t;
    }
    /**
     * 获取领域操作{@link DomainAction}列表，领域操作指增删改查等标准功能以外的业务操作
     * @return
     */
    default List<DomainAction<T,K>> getDomainActions(){
        return List.of();
    }
    default Optional<DomainAction<T,K>> getDomainAction(final String actionName){
        return getDomainActions().stream()
                .filter(a->a.getName().equals(actionName))
                .findFirst();
    }
    /**
     * 执行操作，操作名称放在操作参数中
     * @param t 实体对象
     * @param user 执行用户
     * @param actionParam 操作参数
     * @return
     */
    default T doAction(final T t, final UserAccount user, final DomainActionParam actionParam){
        return t;
    }
    /**
     * 执行操作，操作名称放在操作参数中
     * @param t 实体对象
     * @param user 执行用户
     * @param actionParam 操作参数
     * @return
     */
    default T doAction(final T t, final UserAccount user,boolean hasPermission, final DomainActionParam actionParam){
        return t;
    }

    /**
     * 计算单个实体差异，不会计算子表
     * @param o 原值
     * @param n 新值
     * @return 修改数据包含原值、变更项
     */
    ChangeLog.ChangeData differProperties(T o, T n);

    /**
     * 计算实体列表差异，不会计算子表
     * @param ol 原值列表
     * @param nl 新值列表
     * @return 修改数据集合
     */
    List<ChangeLog.ChangeData> differProperties(List<T> ol, List<T> nl);

    /**
     * 计算实体完整差异（计算子表），存储为修改日志{@link ChangeLog}，用于流程追踪、审计追踪和变更日志
     * @param o 原值对象
     * @param n 新值对象
     * @return 修改数据集合
     */
    Map<String, Object> differ(T o, T n);
    /**
     * 恢复修改数据
     * @param change 修改
     * @return 返回影响记录数
     */
    default int revert(ChangeLog.ChangeData change){
        return switch (change.getT()){
            case ChangeType.ADDED   -> delete((T) change.getN(), CachePolicy.CACHE_NONE);
            case ChangeType.REMOVED -> insert((T) change.getO(), CachePolicy.CACHE_NONE);
            case ChangeType.CHANGED -> update((T) change.getO(), CachePolicy.CACHE_NONE);
            default -> 0;
        };
    }

    default int revert(ChangeLog.ChangeData change,CachePolicy p){
        return switch (change.getT()){
            case ChangeType.ADDED   -> delete((T) change.getN(), p);
            case ChangeType.REMOVED -> insert((T) change.getO(), p);
            case ChangeType.CHANGED -> update((T) change.getO(), p);
            default -> 0;
        };
    }
}
