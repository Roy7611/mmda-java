package cloud.mmda.core.services;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.caching.CachePolicy;
import cloud.mmda.core.caching.redis.EntityCacheProvider;
import cloud.mmda.core.caching.redis.TenancyEntityCacheProvider;
import cloud.mmda.core.data.jdbc.repository.TenancyEntityRepository;
import cloud.mmda.core.data.pagination.PagedList;
import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.entities.*;
import cloud.mmda.core.enums.ModuleActionType;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.metadata.MetaEnumMember;
import cloud.mmda.core.metadata.MetaRelation;
import cloud.mmda.core.security.models.UserAccount;
import cloud.mmda.core.services.exceptions.DomainException;
import cloud.mmda.core.services.exceptions.NoRecordsAffectedException;
import cloud.mmda.core.services.exceptions.OperationFailedException;
import cloud.mmda.core.utils.BaseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 多租户实体服务抽象基类。
 * @author roshion.luo
 * @since 2021.5.23
 *
 * @param <T>
 * @param <K>
 */
public abstract class TenancyEntityService<T extends TenancyEntity<K>, K> extends EntityService<T, K>{
    private static final Log logger = LogFactory.getLog(TenancyEntityService.class);

    private final TenancyEntityRepository<T, K> tRepository;
    protected final TenancyEntityCacheProvider<T,K> tCacheProvider;

    public TenancyEntityCacheProvider<T,K> getTenancyCacheProvider(){ return tCacheProvider;}
    /**
     * 构造器，子类必须覆盖
     *
     * @param repository-
     * @param factory
     */
    public TenancyEntityService(final TenancyEntityRepository<T, K> repository, final RedisConnectionFactory factory) {
        super(repository, factory);
        this.tRepository = repository;
        this.tCacheProvider = (TenancyEntityCacheProvider<T,K>)getCacheProvider();
    }
    @Override
    protected EntityCacheProvider<T,K> createCacheProvider(RedisConnectionFactory factory, Class<T> tClass, Class<K> kClass) {
        return new TenancyEntityCacheProvider<T,K>(factory,tClass,kClass);
    }


    //region create
    public long getMinEntityID(int tenantId){
        return tRepository.getMinEntityID(tenantId);
    }
    /**
     * 为客户端创建一个空的实体模板，
     * 生成多租户ID，设置默认值，客户端基于此模板编辑然后提交。
     * @param p 缓存策略
     * @return
     */
    @Override
    public T create(CachePolicy p){
        //默认实现
        var t = tRepository.create(p.getTenantID());
        //唯一键生成（单据号）
        if(tRepository.hasUniqueKeyCol()){
            var uniqueKeyExp = tRepository.findUniqueKeyExpression(p.getTenantID());
            if(uniqueKeyExp != null){
                try{
                    var uniqueKeyValue = getExpressionValue(t,uniqueKeyExp,String.class);
                    tRepository.setUniqueKeyValue(t,uniqueKeyValue);
                }
                catch(Exception e){
                    logger.error("Error on evaluating unique key expression " + uniqueKeyExp + ", use default instead.", e);
                    t.setDefaultUniqueKey();
                }
            }
            else{
                t.setDefaultUniqueKey();
            }
        }
        return t;
    }


    //endregion

    //region validate


    @Override
    protected List<ValidationError> validate(final T t){
        //必须调用父类方法
        List<ValidationError> errors = super.validate(t);
        //如果有唯一键，自动校验
        if(tRepository.hasUniqueKeyCol()){
            boolean valid = tRepository.validateUniqueKey(t);
            if(!valid) {
                MetaCol ukCol = tRepository.getUniqueKeyCol();
                errors.add(ValidationError.valueOf(ukCol.getColName(),ukCol.getDisplayLabel()+"重复"));
            }
        }
        return errors;
    }
    //endregion

    //region insert

    @Override
    public int insert(final T t, final CachePolicy p) throws OperationFailedException, NoRecordsAffectedException {
        t.setTenantID(p.getTenantID());
        try{
            return super.insert(t, p);
        }
        catch (DuplicateKeyException ex){
            //主键重复，尝试刷新
            tRepository.renewPartitionID(p.getTenantID(),t.getPartitionID());
            throw operationFailed("insert", t, ex);
        }
        catch (DomainException ex){
            throw ex;
        }
    }
    //endregion

    //region delete

    @Override
    protected int deleteFromDb(final K k, final CachePolicy p){
        return tRepository.delete(p.getTenantID(), k);
    }

    //endregion

    //region deleteAll
    @Override
    protected int deleteAllFromDb(final String condition, final CachePolicy p, Object... args){
        String deleteCondition = SqlExpression.and(condition,getDeletableCondition());
        return tRepository.deleteAll(p.getTenantID(), deleteCondition, args);
    }
    protected int deleteAllFromDb(final SqlExpression condition, final CachePolicy p){
        return tRepository.deleteAll(p.getTenantID(), condition.withAdditionalCondition(getDeletableCondition()));
    }
    protected int deleteAllFromDb(final Collection<K> keys, final CachePolicy p){
        int[] r = tRepository.deleteAll(p.getTenantID(),keys,getDeletableCondition());
        return Arrays.stream(r).sum();
    }
    //endregion

    //region get
    @Override
    protected T getFromCacheOrDb(final K k, final CachePolicy p){
        //确保不能跨租户访问
        tRepository.ensureMatchedTenantID(p.getTenantID(),k);
        //从缓存取
        T t = null;
        if(p.cached()) t = getFromCache(k);
        //没取到再从数据库取
        if(t == null) t = tRepository.find(p.getTenantID(),k);
        return t;
    }
    @Override
    protected T getFromDbByNo(final String no, CachePolicy p){
        return tRepository.findByUniqueKey(p.getTenantID(),no);
    }

    //endregion


    //region getAllKeys
    protected List<K> getAllKeysFromDbBy(final String condition, final CachePolicy p, Object... args){
        String deleteCondition = SqlExpression.and(condition,getDeletableCondition());
        return tRepository.findAllKeysBy(p.getTenantID(),deleteCondition, args);
    }
    protected List<K> getAllKeysFromDbBy(final SqlExpression condition, final CachePolicy p){
        return tRepository.findAllKeysBy(p.getTenantID(),condition.withAdditionalCondition(getDeletableCondition()));
    }
    //endregion

    //region update
    @Override
    public int update(final T t) throws OperationFailedException,NoRecordsAffectedException {
        return updateWithChangeLog(t,CachePolicy.of(t.getTenantID()),null);
    }
    //endregion

    //region updatePartial

    @Override
    protected int partialUpdateToDb(final K k, final SqlExpression attributes, final SqlExpression condition, final CachePolicy p){
        if(condition == null) return tRepository.update(p.getTenantID(), k, attributes);
        return tRepository.update(p.getTenantID(),k, attributes, condition);
    }
    @Override
    protected List<K> partialUpdateAllToDb(final Collection<Map<String, Object>> listOfKeyAndAttributes, final CachePolicy p){
        List<K> updatedKeys = new ArrayList<>();
        tRepository.updateAll(p.getTenantID(),listOfKeyAndAttributes, updatedKeys);
        return updatedKeys;
    }
    //endregion

    //region updateAll
    @Override
    protected int partialUpdateAllToDb(final SqlExpression attributes, final SqlExpression condition, final CachePolicy p){
        return tRepository.updateAll(p.getTenantID(), attributes, condition);
    }
    //endregion

    //region getAny
    @Override
    protected T getAnyFromDb(final String condition, final CachePolicy p, Object... args){
        return tRepository.findFirst(p.getTenantID(),condition,args);
    }
    @Override
    protected T getAnyFromDb(final SqlExpression condition, final CachePolicy p){
        return tRepository.findFirst(p.getTenantID(),condition);
    }
    //endregion

    //region getAll (no cache)
    @Override
    public List<T> getAll(final CachePolicy p) throws OperationFailedException{
        return getList(()->tRepository.findAll(p.getTenantID()));
    }
    @Override
    public List<T> getAll(final Sort sort, final CachePolicy p) throws OperationFailedException{
        return getList(()->tRepository.findAll(p.getTenantID(),sort));
    }

    @Override
    public PagedList<T> getAll(final Paginator paginator, final CachePolicy p) throws OperationFailedException{
        return getPagedList(()->tRepository.findAll(p.getTenantID(), paginator));
    }

    @Override
    public List<T> getAllBy(final SqlExpression condition, final Sort sort, final CachePolicy p) throws OperationFailedException{
        return getList(()->tRepository.findAllBy(p.getTenantID(),condition, sort));
    }
    @Override
    public List<T> getAllBy(final SqlExpression condition, final CachePolicy p) throws OperationFailedException{
        return getList(()->tRepository.findAllBy(p.getTenantID(),condition));
    }
    @Override
    public List<T> getAllBy(final String condition, final Sort sort, CachePolicy p, Object... args) throws OperationFailedException{
        return getList(()->tRepository.findAllBy(p.getTenantID(), condition, sort, args));
    }


    @Override
    public PagedList<T> getAllBy(final Paginator paginator, final String condition, final CachePolicy p, Object... args) throws OperationFailedException{
        return getPagedList(()->tRepository.findAllBy(p.getTenantID(), paginator,condition, args));
    }

    @Override
    public PagedList<T> getAllBy(final Paginator paginator, final SqlExpression condition, final CachePolicy p) throws OperationFailedException{
        return getPagedList(()->tRepository.findAllBy(p.getTenantID(), paginator,condition));
    }
    //endregion

    //region getAllIn or not in
    @Override
    public List<T> getAllIn(final String field, final String list, final boolean ordered, final CachePolicy p,
                            final String condition, Object...args) throws OperationFailedException{
        try{
            int tenantID = p.getTenantID();
            boolean hasCond = BaseUtil.hasText(condition);
            if(p.cached() && !hasCond){
                String listCacheKey = tCacheProvider.getListCacheKey(tenantID,field+(ordered?"OL":"UL"),list);
                List<T> result = tCacheProvider.getList(listCacheKey);
                if(result.isEmpty()) result = tRepository.findAllIn(tenantID, field, list, ordered);
                if(p.readThrough() && !result.isEmpty()){
                    tCacheProvider.putList(listCacheKey, result);
                }
                return result;
            }

            return hasCond
                    ? tRepository.findAllIn(tenantID, field, list, ordered, condition, args)
                    : tRepository.findAllIn(tenantID, field, list, ordered);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("getAllIn", String.join(":",field,list), ex);
        }
    }
    @Override
    public PagedList<T> getAllNotIn(final Paginator paginator, final String field, final String list, final CachePolicy p,
                                    final String condition, Object...args) throws OperationFailedException{
        try{
            int tenantID = p.getTenantID();
            boolean hasCond = BaseUtil.hasText(condition);
            if(p.cached() && !hasCond){
                String listCacheKey = tCacheProvider.getListCacheKey(tenantID,field+"!", paginator.toString(),list);
                List<T> data = tCacheProvider.getList(listCacheKey);
                boolean hasCache = data != null && !data.isEmpty();
                PagedList<T> result = hasCache
                        ? PagedList.of(data, paginator) //TODO pager没有计数
                        : tRepository.findAllNotIn(tenantID, paginator, field, list);

                if(p.readThrough() && !hasCache && !result.isEmpty()){
                    tCacheProvider.putList(listCacheKey, result.getData());
                }
                return result;
            }

            return hasCond
                    ? tRepository.findAllNotIn(tenantID, paginator, field, list, condition, args)
                    : tRepository.findAllNotIn(tenantID, paginator, field, list);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("getAllNotIn", String.join(":",field,list), ex);
        }
    }
    //endregion

    //region searchAll (no cache)
    @Override
    protected PagedList<T> searchAllFromDb(final Paginator paginator, final String word, final CachePolicy p){
        return tRepository.searchAll(p.getTenantID(), paginator, word);
    }
    @Override
    protected PagedList<T> searchAllFromDb(final Paginator paginator, final String word, final CachePolicy p, final String condition, Object...args){
        return tRepository.searchAll(p.getTenantID(), paginator, word, condition, args);
    }
    protected List<T> searchAllFromDb(final String word, final Sort sort, final CachePolicy p){
        return tRepository.searchAll(p.getTenantID(), word, sort);
    }
    @Override
    protected List<T> searchAllFromDb(final String word, final Sort sort, final CachePolicy p, final String condition, Object...args){
        return tRepository.searchAll(p.getTenantID(), word, sort, condition, args);
    }
    //endregion

    //region count (no cache)
    protected int countFromDb(final CachePolicy p){
        return tRepository.count(p.getTenantID());
    }
    protected int countFromDb(final String condition, final CachePolicy p, Object... args){
        return tRepository.count(p.getTenantID(),condition,args);
    }
    protected int countFromDb(final SqlExpression condition, final CachePolicy p){
        return tRepository.count(p.getTenantID(),condition);
    }
    protected int countFromDb(final String word,final String condition, final CachePolicy p, Object... args){
        return tRepository.count(p.getTenantID(),word,condition,args);
    }
    protected int countFromDb(final String word, final CachePolicy p){
        return tRepository.countBySearchWord(p.getTenantID(),word);
    }
    protected boolean existsInDb(final K k, final CachePolicy p){
        return tRepository.exists(p.getTenantID(),k);
    }
    //endregion

    //region refs

    /**
     * 载入引用字典，分租户
     * @param relation
     * @return
     */
    @Override
    protected Map<String, String> loadRefEnumMap(String refEnumMapCacheKey, MetaRelation relation){
        int tenantID = 0;
        if (refEnumMapCacheKey.indexOf(":") != -1) {
            tenantID = Short.parseShort(refEnumMapCacheKey.substring(0, refEnumMapCacheKey.indexOf(":")));
        }
        List<MetaEnumMember> items = tRepository.findAllRefMap(tenantID, relation);
        //处理空结果集
        if (items.isEmpty()) {
            return Collections.unmodifiableMap(EMPTY_REF_ENUM_MAP) ;
        }
        Map<String, String> refEnumMap = items.stream().collect(Collectors.toMap(MetaEnumMember::getValue, MetaEnumMember::getText,(existing, replacement) -> replacement));
        tCacheProvider.putRefMap(refEnumMapCacheKey, refEnumMap);
        return refEnumMap;
    }


    /**
     * 尝试清除引用缓存，更新后调用。比如解决仓库名称修改后组装引用属性仍然是老名字问题
     * @param t
     */
    @Override
    protected void tryEvictRefCache(final T t){
        try{
            int tenantID = t.getTenantID();
            String cacheKey = tenantID+":REF_"+tClassName;
            tCacheProvider.evictRefKey(cacheKey,t.getId().toString());
        }
        catch (Exception ex){
            String msg = String.format("清除%1$s引用缓存异常。", tClassName);
            logger.error(msg, ex);
        }
    }

    @Override
    protected EntityFilter buildRefFilter(MetaCol col, String lang, CachePolicy p){
        return tRepository.buildRefFilter(p.getTenantID(),col);
    }
    //endregion

    //region load & save
    @Override
    public int save(final T t, final CachePolicy p, boolean checkExists, final Map<String, Object> changeLog)
            throws NoRecordsAffectedException, OperationFailedException{
        if(t.isCreated() && p.getTenantID()>0) t.setTenantID(p.getTenantID());
        return super.save(t, p, checkExists, changeLog);
    }

    public int save(final T t, final Map<String,Object> changeLog)
            throws NoRecordsAffectedException, OperationFailedException{
        return save(t, CachePolicy.of(t.getTenantID()), false, changeLog);
    }

    /**
     * 保存，不缓存
     * @param t 实体对象
     * @return 返回影响记录数
     * @throws NoRecordsAffectedException 当数据库记录已经不存在
     * @throws OperationFailedException 当数据保存未成功
     */
    public int save(final T t) throws NoRecordsAffectedException, OperationFailedException{
        return super.save(t, CachePolicy.of(t.getTenantID()), false);
    }


    /**
     * 更新实体对象 t，附带额外的修改日志 changeLog，主要给Action重写使用
     * @param t 实体对象
     * @param changeLog 附加的修改日志，例如修改了其他实体数据
     * @return 影响记录数
     * @throws NoRecordsAffectedException
     * @throws OperationFailedException
     */
    public int updateWithChangeLog(T t, Map<String,Object> changeLog)
            throws NoRecordsAffectedException, OperationFailedException {
        return super.updateWithChangeLog(t, CachePolicy.of(t.getTenantID()), changeLog);
    }
    //endregion


    //region actions

    /**
     * 组装可执行的操作给客户端
     * @param t 实体对象
     * @param user 拟执行用户
     * @return
     */
    @Override
    public T assembleActions(final T t, final UserAccount user, final Locale locale) {
        //组装actions，根据实体对象的业务属性动态添加action
        List<EntityAction> actions = new ArrayList<>();
        var auth = this.getModuleAuthority(user.getUserID(),locale);
        var authorizedModuleActions = auth.getAuthorizedActions();
        for(var action : getDomainActions()){
            //仅组装用户任务，其他的后台自动执行
            if(action.getType()!= ModuleActionType.USER_TASK) continue;
            //权限过滤
            if(!authorizedModuleActions.stream().anyMatch(a->action.getName().equals(a.getActionName()))) continue;
            //判断能否执行，能的加入列表
            if(action.canExecute(t,user)) actions.add(action.getMetadata(locale));
        }
        t.setActions(actions);

        // 子类可继续添加...
        return assembleEntityActions(t,auth);
    }

    //endregion
    public List<Long> getDescendantDivisionDepartmentIDs(long pid){
        return tRepository.findDescendantDivisionDepartmentIDs(pid);
    }
    @Override
    protected T handleImportEntity(T t, boolean checkExists, CachePolicy p, UserAccount user) {
        t.setTenantID(p.getTenantID());
        t = super.handleImportEntity(t, checkExists, p, user);
        // 如果该实体不存在，则生成一个新的标识ID
        if (!getMetaObject().hasCompositeKeys() && BaseUtil.isNullOrZero(Tenancy.getRealID(t.getPartitionID()))) {
            t.setPartitionID(tRepository.newPartitionID(p.getTenantID()));
        }
        if (tRepository.hasUniqueKeyCol()) {
            String uniqueNo = null;
            Object uniqueKeyObj = tRepository.getUniqueKeyValue(t);
            if (uniqueKeyObj != null) uniqueNo = (String) uniqueKeyObj;
            if (BaseUtil.isNullOrEmpty(uniqueNo) || uniqueNo.equals("@")) {
                //唯一键生成（单据号）
                var uniqueKeyExp = tRepository.findUniqueKeyExpression(p.getTenantID());
                if (uniqueKeyExp != null) {
                    try {
                        var uniqueKeyValue = getExpressionValue(t, uniqueKeyExp, String.class);
                        tRepository.setUniqueKeyValue(t, uniqueKeyValue);
                    } catch (Exception e) {
                        logger.error("Error on evaluating unique key expression " + uniqueKeyExp + ", use default instead.", e);
                        t.setDefaultUniqueKey();
                    }
                } else {
                    t.setDefaultUniqueKey();
                }
            }

        }
        return t;
    }
}
