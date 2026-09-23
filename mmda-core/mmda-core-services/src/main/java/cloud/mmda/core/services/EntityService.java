package cloud.mmda.core.services;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.app.ApplicationContextProvider;
import cloud.mmda.core.caching.CachePolicy;
import cloud.mmda.core.caching.redis.EntityCacheProvider;
import cloud.mmda.core.data.jdbc.metadata.DbMetadataProvider;
import cloud.mmda.core.data.jdbc.repository.ChangeLogRepository;
import cloud.mmda.core.data.jdbc.repository.EntityRepository;
import cloud.mmda.core.data.jdbc.repository.Repository;
import cloud.mmda.core.data.pagination.*;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.entities.*;
import cloud.mmda.core.enums.ChangeType;
import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.file.excel.ExcelConstant;
import cloud.mmda.core.file.excel.ExcelFileReader;
import cloud.mmda.core.file.excel.ExcelFileWriter;
import cloud.mmda.core.file.exceptions.ExcelException;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.metadata.Module;
import cloud.mmda.core.models.BackgroundTask;
import cloud.mmda.core.models.ChangeLog;
import cloud.mmda.core.models.ReportTemplate;
import cloud.mmda.core.security.models.UserAccount;
import cloud.mmda.core.security.repository.RoleModuleAuthRepository;
import cloud.mmda.core.services.exceptions.*;
import cloud.mmda.core.services.i18n.LocalizedMessage;
import cloud.mmda.core.services.i18n.LocalizedMessageService;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.JsonUtil;
import cloud.mmda.core.utils.NamingUtil;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 实体服务抽象基类。
 * 可继承此类实现具体的实体服务，你应该在实体服务类中实现商业逻辑和数据处理。
 * 实体服务供控制器注入使用，实体服务不使用响应式编程。
 * 你必须在实体服务层管理事务。
 *
 * @author roshion.luo
 * @since 2021.5.22
 *
 *
 * @param <T> 实体类型
 * @param <K> 实体主键类型
 */
public abstract class EntityService<T extends Entity<K>, K> implements DomainService<T,K>, MessageSourceAware {
    private static final Log logger = LogFactory.getLog(EntityService.class);
    private static final String LOST_REF_TEXT = "-";
    protected static final Map<String,String> EMPTY_REF_ENUM_MAP = new HashMap<String,String>(){
        { put("", LOST_REF_TEXT); }
    };

    private final EntityRepository<T,K> repository;
//    private final ConcurrentMap<MetaCol,MetaRelation> refColRelationMap;
//    private final ConcurrentMap<MetaCol,MetaRelation> oneColRelationMap;
//    private final ConcurrentMap<MetaCol,MetaRelation> manyColRelationMap;

    protected final String tClassName;

    private List<EntityFilter> entityFilters;
    private List<EntitySortSet> entitySorts;
    private Map<String,MetaObject> metadata;

    private MessageSource messageSource;
    @Override
    public void setMessageSource(MessageSource messageSource){
        this.messageSource = messageSource;
    }
    public String getLocalizedMessage(String code, @Nullable Object[] args, Locale locale){
        printAllMessages(locale);
        return this.messageSource.getMessage(code,args,locale);
    }
    public void printAllMessages(Locale locale) {
        // 假设 messageSource 已经被正确初始化
        ReloadableResourceBundleMessageSource messageSource = (ReloadableResourceBundleMessageSource) this.messageSource;

        Set<String> keys = new HashSet<>();
        messageSource.getBasenameSet().forEach(baseName -> {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName, locale);
            Enumeration<String> propertyNames = resourceBundle.getKeys();
            while (propertyNames.hasMoreElements()) {
                keys.add(propertyNames.nextElement());
            }
        });
        System.out.println(JsonUtil.toJson(keys));
    }
    public String getLocalizedMessage(String code, Locale locale){
        return this.messageSource.getMessage(code,null,locale);
    }

    //缓存提供者
    private final EntityCacheProvider<T,K> cacheProvider;
    public EntityCacheProvider<T,K> getCacheProvider() { return cacheProvider; }

    /**
     * 获取元数据提供者
     * @return
     */
    public DbMetadataProvider getMetadataProvider(){
        return this.repository.getMetadataProvider();
    }

    //角色模块权限存储
    private RoleModuleAuthRepository roleModuleAuthRepository;
    @Autowired
    public void setRoleModuleAuthRepository(@Qualifier("securityRoleModuleAuthRepository") RoleModuleAuthRepository roleModuleAuthRepository) {
        this.roleModuleAuthRepository = roleModuleAuthRepository;
    }
    //修改日志存储
    protected ChangeLogRepository changeLogRepository;
    @Autowired
    public void setChangeLogRepository(ChangeLogRepository changeLogRepository) {
        this.changeLogRepository = changeLogRepository;
    }

    protected ReportTemplateService reportTemplateService;
    @Autowired
    public void setReportTemplateService(ReportTemplateService reportTemplateService) {
        this.reportTemplateService = reportTemplateService;
    }
    protected CustomizedQueryService customizedQueryService;
    @Autowired
    public void setCustomizedQueryService(CustomizedQueryService customizedQueryService) {
        this.customizedQueryService = customizedQueryService;
    }
    protected BackgroundTaskService backgroundTaskService;
    @Autowired
    public void setBackgroundTaskService(BackgroundTaskService backgroundTaskService) {
        this.backgroundTaskService = backgroundTaskService;
    }
    /**
     * 构造器，子类必须覆盖
     * @param repository
     */
    public EntityService(final EntityRepository<T, K> repository, final RedisConnectionFactory factory){
        this.repository = repository;
        this.tClassName = repository.getTClassName();
        this.cacheProvider = createCacheProvider(factory,repository.getTClass(),repository.getKClass());
//        this.refColRelationMap = repository.getMetaObject().buildColRelationMap(MetaRelation.REF);
//        this.oneColRelationMap = repository.getMetaObject().buildColRelationMap(MetaRelation.HAS_ONE);
//        this.manyColRelationMap = repository.getMetaObject().buildColRelationMap(MetaRelation.HAS_MANY);
    }
    /**
     * 留给子类定制的机会
     * @param factory
     */
    protected EntityCacheProvider<T,K> createCacheProvider(RedisConnectionFactory factory,Class<T> tClass, Class<K> kClass){
         return new EntityCacheProvider<T,K>(factory,tClass,kClass);
    }
    protected final SqlExpression.SqlExpressionBuilder sqlExpressionBuilder() {
        return repository.expressionBuilder();
    }
    @Override
    public Sort getDefaultSort(){
        return repository.getDefaultSort();
    }
    @Override
    public K asKey(final T t){ return t.getId(); }

    @Override
    public SqlExpression buildExpression(final Map<String, Object> attributes){
        return repository.buildExpression(attributes);
    }
    //////////////////////////////////////////////////////////////////////////
    // 国际化消息
    //////////////////////////////////////////////////////////////////////////

    protected LocalizedMessageService i18n;
    @Autowired
    public final void setLocalizedMessageService(final LocalizedMessageService localizedMessageService){
        this.i18n = localizedMessageService;
    }

    protected OperationFailedException operationFailed(String op, Object context, Throwable cause){
//        LocalizedMessage msg = i18n.getLocalizedMessage(OperationFailedException.CODE, tClassName, op, context);
        String localizedMessage=i18n.getLocalizedMessage(op).getMessage();
        LocalizedMessage msg = i18n.getLocalizedMessage(OperationFailedException.CODE,getMetaObject().getDisplayLabel(), localizedMessage, context);

        logger.error(msg.getMessage(), cause);
        return new OperationFailedException(msg, cause);
    }
    protected NotFoundException notFound(String op, Object context, Throwable cause){
//        LocalizedMessage msg = i18n.getLocalizedMessage(NotFoundException.CODE, tClassName, localizedMessage, context);
        String localizedMessage=i18n.getLocalizedMessage(op).getMessage();
        LocalizedMessage msg = i18n.getLocalizedMessage(NotFoundException.CODE,getMetaObject().getDisplayLabel(), localizedMessage, context);
        logger.error(msg.getMessage(), cause);
        return new NotFoundException(msg, cause);
    }

    protected DataInvalidException dataInvalid(String op, Object context, List<ValidationError> validationErrors, Throwable cause){
//        LocalizedMessage msg = i18n.getLocalizedMessage(DataInvalidException.CODE, tClassName, localizedMessage, context);
        String localizedMessage=i18n.getLocalizedMessage(op).getMessage();
        LocalizedMessage msg = i18n.getLocalizedMessage(DataInvalidException.CODE,getMetaObject().getDisplayLabel(), localizedMessage, context);

        logger.error(msg.getMessage(), cause);
        return new DataInvalidException(msg,validationErrors, cause);
    }

    protected <U> U expect(U value, Supplier<U> supplier){
        U result = supplier.get();
        if(result!=value) throw new DataIntegrityViolationException("数据不是你期望值");
        return result;
    }
    //////////////////////////////////////////////////////////////////////////
    // 提供给Web层调用接口
    //////////////////////////////////////////////////////////////////////////
    protected void applyConsumerIfNonNull(T t, Consumer<T> consumer){
        if(consumer==null || t==null) return;
        consumer.accept(t);
    }
    protected T applyFunctionIfNonNull(T t, Function<T, T> func){
        if(func==null || t==null) return t;
        return func.apply(t);
    }

    //region cache
    protected boolean tryPutToCache(final T t, final Duration timeout){
        try{
            cacheProvider.put(t, timeout);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    protected boolean tryPutToCacheIfAbsent(final T t, final Duration timeout){
        try{
            cacheProvider.putIfAbsent(t, timeout);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    protected boolean tryPutToCacheIfPresent(final T t, final Duration timeout){
        try{
            cacheProvider.putIfPresent(t, timeout);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    protected T getFromCache(final K k){
        try{
            return cacheProvider.getByKey(k);
        }
        catch (Exception e){
            return null;
        }
    }
    protected boolean tryRemoveFromCache(final T t){
        try{
            cacheProvider.remove(t);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    protected boolean tryRemoveFromCache(final K k){
        try{
            cacheProvider.removeByKey(k);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    protected Long tryRemoveAllFromCache(final Collection<K> keys){
        try{
            return cacheProvider.removeAllByKeys(keys);
        }
        catch (Exception e){
            return 0L;
        }
    }

    /**
     * 缓存预热
     */
    public void warmUp(final CachePolicy p){
        List<T> list = getAll(p);
        for (T t : list){
            cacheProvider.put(t,p.timeout());
        }
    }

    //endregion

    //region validate

    /**
     * 业务校验前根据需要设置实体的数据，例如生成行号
     * @param t
     */
    protected void beforeValidate(final T t){
        //模板方法，留给子类实现
    }
    /**
     * 业务校验，指根据业务规则校验实体数据的合法性，子类实现业务校验。不同于在Web层的纯粹数据校验。
     * 你可以修改t的属性值，但不能t = new T()
     * 若校验不通过，请抛出{@link DataInvalidException}异常或者其子类
     * 保存到数据库前需校验，被insert / update / save 调用
     * @param t
     * @return 返回校验错误列表
     */
    protected List<ValidationError> validate(final T t) throws DataInvalidException{
        beforeValidate(t);
        //模板方法，留给子类实现
        //例如验证订单总金额是否与明细项相符
        //最好不要在这里修改数据，默认值在控制器层设置
        return new ArrayList<>();
    }
    //endregion

    //region insert

    /**
     * 为客户端创建一个空的实体模板，
     * 生成多租户ID，设置默认值，客户端基于此模板编辑然后提交。
     * TODO 使用flyweight模式提升性能
     * @param p 租户ID
     * @return
     */
    @Override
    public T create(CachePolicy p){
        //默认实现
        return repository.create();
    }

    /**
     * 创建到数据库前拦截器，注意数据库字段的默认值不要在这里设置。
     * 而应该放在实际插入成功后设置，确保与数据库一致。
     * 建议在repository.insert(t)中声明变量，然后插入，成功后设置实体属性，返回。
     * @return
     */
    protected Consumer<T> beforeInsert(){
        //创建到数据库前，设置默认值
        return null;
    }

    /**
     * 成功创建后拦截器，例如你可以设置关联对象属性值。组装其他信息等。
     * 不要抛出异常，否则会导致客户端不能正常收到数据，而实际上已经创建好了。
     * @return
     */
    protected Consumer<T> afterInserted(){
        return null;
    }
    /**
     * 插入一个实体。
     * 先对实体进行业务校验，调用{@link EntityService#validate(Entity)}，例如数据完整性，关联性准确，插入前计算等，若抛出异常则不会插入。
     * 校验通过后插入数据库。
     * 若缓存策略为{@link CachePolicy#WRITE_THRU}则写缓存，缓存是否写成功均不会影响返回结果。
     *
     * @param t 实体对象
     * @param p 缓存策略
     * @return 插入成功的实体对象
     * @throws OperationFailedException,NoRecordsAffectedException
     */
    @Override
    public int insert(final T t, final CachePolicy p) throws OperationFailedException,NoRecordsAffectedException {
        if(p.writeBehind()){
            //写缓存
            if(!tryPutToCache(t, p.timeout())){
                LocalizedMessage msg = i18n.getLocalizedMessage(NoRecordsAffectedException.CODE, tClassName, t);
                throw new NoRecordsAffectedException(msg);
            }
            return 1;
        }
        else{
            try{
                //校验
                List<ValidationError> validationErrors = validate(t);
                if(!validationErrors.isEmpty()){
                    throw dataInvalid("insert",t,validationErrors,null);
                }

                //插入数据库
                applyConsumerIfNonNull(t, beforeInsert());
                int r = repository.insert(t);
                applyConsumerIfNonNull(t, afterInserted());

                //自动记录修改日志
                if(t instanceof ChangeLoggable loggable && loggable.isLoggable()){
                    K k = t.getId();
                    T newVal = t instanceof Computable
                            ? repository.find(k)
                            : t;
                    var changeData = ChangeLog.changed(null, newVal);
                    try{
                        var changeLog = new LinkedHashMap<String,Object>();
                        changeLog.put(tClassName,changeData);
                        var logID = changeLogRepository.insert(p.getTenantID(),changeLog,tClassName,k.toString(),loggable.getChangeLogID());
                        if(logID > 0){
                            int rl = updateChangeLogID(t.getId(),logID,p);
                            if(rl > 0) loggable.setChangeLogID(logID);
                        }
                    }
                    catch (Exception e){
                        var msg = String.format("写修改日志%1$s：%2$s失败。", tClassName, changeData);
                        logger.error(msg, e);
                    }
                }

                //写缓存，不论成功与否不影响结果
                if(p.writeThrough()) {
                    if(!tryPutToCache(t, p.timeout()))
                        logger.warn(String.format("写缓存%1$s：%2$s失败。", tClassName, t));
                }
                return r;
            }
            catch (DuplicateKeyException ex){
                //抛给子类处理
                throw ex;
            }
            catch (DomainException ex){
                throw ex;
            }
            catch (Exception ex){
                throw operationFailed("insert", t, ex);
            }
        }
    }

    //endregion

    //region delete


    /**
     * 删除拦截器，若抛出异常则不会删除
     * todo 根据元数据引用检查是否可删除
     * @return
     */
    protected Consumer<T> beforeDelete(){
        return null;
    }
    /**
     * 删除成功后拦截器，例如你可以对列表缓存进行处理
     * @return
     */
    protected Consumer<T> afterDeleted(){
        return null;
    }
    protected int deleteFromDb(final K k, final CachePolicy p){
        return repository.delete(k);
    }

    @Override
    public int delete(final T t, final CachePolicy p) throws OperationFailedException{
        try{
            //检查实体是否允许删除
            if(!t.isDeletable()){
                LocalizedMessage msg = i18n.getLocalizedMessage("deletion.not.allowed", tClassName,t);
                throw new OperationNotAllowException(msg);
            }
            //删除
            K k = t.getId();
            applyConsumerIfNonNull(t, beforeDelete());
            int r = deleteFromDb(k, p);
            //如果返回记录数0，报异常
            if(r == 0) {
                LocalizedMessage msg = i18n.getLocalizedMessage(NoRecordsAffectedException.CODE, tClassName, k);
                throw new NoRecordsAffectedException(msg);
            }
            applyConsumerIfNonNull(t, afterDeleted());

            //自动记录修改日志
            if(t instanceof ChangeLoggable loggable && loggable.isLoggable()){
                var changeData = ChangeLog.removed(t);
                var changeLog = new HashMap<String,Object>();
                changeLog.put(tClassName,changeData);
                try{
                    var logID = changeLogRepository.insert(p.getTenantID(),changeLog,tClassName,k.toString(),loggable.getChangeLogID());
                    loggable.setChangeLogID(logID);
                    //标记所有关联日志可清理
                    changeLogRepository.updateDeletedByRef(tClassName, k.toString());
                }
                catch (Exception e){
                    var msg = String.format("写修改日志%1$s：%2$s失败。", tClassName, changeData);
                    logger.error(msg, e);
                }
            }

            //清缓存，不论成功与否不影响结果
            if(p.cached() || cacheProvider.existsKey(k)) {
                if(!tryRemoveFromCache(t)){
                    //未能清除缓存是一个错误，可能导致get从缓存还能取得
                    logger.error(String.format("清缓存%1$s：%2$s失败。", tClassName, t));
                }
            }
            t.setEntityState(EntityState.DEFAULT);

            //清除引用缓存
            tryEvictRefCache(t);

            return r;
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("delete", t, ex);
        }
    }
    /**
     * 删除实体
     * @param k 实体主键
     * @param p 缓存策略
     * @return 返回记录数
     * @throws OperationFailedException
     */
    @Override
    public int deleteByKey(final K k, final CachePolicy p) throws OperationFailedException{
        try{
            //检查实体是否允许删除
            final T t = repository.find(k);
            return delete(t, p);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("deleteByKey", k, ex);
        }
    }

    //endregion

    //region deleteAll

    /**
     * 实体允许删除的条件，SQL表达式如 t.status=0。
     * 用于deleteAll的拦截，比如收货单已经确认后不允许删除。
     * 而delete单个实体对象则通过{@link Entity#isDeletable()}判断。
     * @return
     */
    protected String getDeletableCondition(){return null;}

    protected int deleteAllFromDb(final String condition, final CachePolicy p, Object... args){
        String deleteCondition = SqlExpression.and(condition,getDeletableCondition());
        return repository.deleteAll(deleteCondition, args);
    }
    protected int deleteAllFromDb(final SqlExpression condition, final CachePolicy p){
        return repository.deleteAll(condition.withAdditionalCondition(getDeletableCondition()));
    }
    protected int deleteAllFromDb(final Collection<K> keys, final CachePolicy p){
        int[] r = repository.deleteAll(keys,getDeletableCondition());
        return Arrays.stream(r).sum();
    }
    @Override
    public int deleteAll(final String condition, final CachePolicy p, Object... args) throws OperationFailedException {
        try {
            if(p.cached()){
                List<K> keys = getAllKeysFromDbBy(condition, p, args);
                tryRemoveAllFromCache(keys);
            }
            return deleteAllFromDb(condition, p, args);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("deleteAll", condition, ex);
        }
    }
    @Override
    public int deleteAll(final SqlExpression condition, final CachePolicy p) throws OperationFailedException{
        try {
            if(p.cached()){
                List<K> keys = getAllKeysFromDbBy(condition, p);
                tryRemoveAllFromCache(keys);
            }
            return deleteAllFromDb(condition, p);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("deleteAll", condition, ex);
        }
    }


    @Transactional
    public int deleteAll(final Collection<K> keys, final CachePolicy p) throws OperationFailedException{
        try {
            if(p.cached()){
                tryRemoveAllFromCache(keys);
            }
            return deleteAllFromDb(keys, p);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("deleteAll", keys, ex);
        }
    }
    //endregion

    //region update

    /**
     * 更新到数据库前拦截器
     * @return
     */
    protected Consumer<T> beforeUpdate(){
        return null;
    }

    /**
     * 更新成功后期拦截器，例如你可以对列表缓存进行处理
     * @return
     */
    protected Consumer<T> afterUpdated(){
        return null;
    }

    /**
     * 更新实体对象 t，附带额外的修改日志 changeLog，主要给Action重写使用
     * @param t 实体对象
     * @param p 缓存策略
     * @param changeLog 附加的修改日志，例如修改了其他实体数据
     * @return 影响记录数
     * @throws OperationFailedException
     * @throws NoRecordsAffectedException
     */
    protected int updateWithChangeLog(T t, final CachePolicy p, Map<String,Object> changeLog)
            throws OperationFailedException,NoRecordsAffectedException {
        if(p.writeBehind()){
            //写缓存
            if(!tryPutToCache(t, p.timeout())){
                LocalizedMessage msg = i18n.getLocalizedMessage(NoRecordsAffectedException.CODE, tClassName, t);
                throw new NoRecordsAffectedException(msg);
            }
            return 1;
        }
        else{
            try{
                //校验
                List<ValidationError> validationErrors = validate(t);
                if(!validationErrors.isEmpty()){
                    throw dataInvalid("update",t,validationErrors,null);
                }

                K k = t.getId();
                T oldVal = null;
                if((t instanceof ChangeLoggable loggable && loggable.isLoggable()) || (t instanceof FlowableEntity<?> flowable && flowable.shouldLog())){
                    oldVal = load(k,p/*CachePolicy.CACHE_NONE*/);//修改日志需要原值
                }

                //更新数据库
                applyConsumerIfNonNull(t, beforeUpdate());
                int r = repository.update(t);
                if(r==0){
                    LocalizedMessage msg = i18n.getLocalizedMessage(NoRecordsAffectedException.CODE, tClassName, t);
                    throw new NoRecordsAffectedException(msg);
                }
                applyConsumerIfNonNull(t, afterUpdated());

                //自动记录修改日志
                if(oldVal != null){
                    T newVal = t;
                    if(t instanceof Computable computable) computable.compute();

                    var diff = differ(oldVal, newVal);
                    if(changeLog != null && !changeLog.isEmpty()) diff.putAll(changeLog);
                    try{
                        Long prevLogId = null;
                        if(oldVal instanceof ChangeLoggable oldLoggable){
                            prevLogId = oldLoggable.getChangeLogID();
                        }
                        String changeLogTags = null;
                        if (t instanceof ChangeLoggable loggable){
                            changeLogTags=loggable.getChangeLogTags();
                        }else if(t instanceof FlowableEntity<?> flowable){
                            changeLogTags=flowable.getChangeLogTags();
                        }
                        var logID = changeLogRepository.insert(p.getTenantID(),diff,tClassName,k.toString(),prevLogId,changeLogTags);
                        if(t instanceof ChangeLoggable loggable){
                            int rl = updateChangeLogID(k, logID,p);
                            if(rl > 0) loggable.setChangeLogID(logID);
                        }
                        else if(t instanceof FlowableEntity<?> flowable){
                            flowable.setNewChangeLogID(logID);
                        }
                    }
                    catch (Exception e){
                        var msg = String.format("写修改日志%1$s：%2$s失败。", tClassName, diff);
                        logger.error(msg, e);
                    }
                }

                //写缓存，不论成功与否不影响结果
                if(p.writeThrough()) {
                    if(!tryPutToCache(t, p.timeout()))
                        logger.warn(String.format("写缓存%1$s：%2$s失败。", tClassName, t));
                }
                else if(p.cached() || cacheProvider.existsKey(k)){
                    if(!tryRemoveFromCache(k)){
                        //未能清除缓存是一个错误，可能导致get从缓存还能取得
                        logger.error(String.format("清缓存%1$s：%2$s失败。", tClassName, k));
                    }
                }

                //清除引用缓存
                tryEvictRefCache(t);

                return r;
            }
            catch (DomainException ex){
                throw ex;
            }
            catch (Exception ex){
                throw operationFailed("update", t, ex);
            }
        }
    }
    @Override
    public int update(final T t, final CachePolicy p) throws OperationFailedException,NoRecordsAffectedException {
            return updateWithChangeLog(t,p,null);
    }

    public int update(final T t) throws OperationFailedException,NoRecordsAffectedException {
        return updateWithChangeLog(t, CachePolicy.CACHE_NONE, null);
    }
    //endregion

    //region partialUpdate
    protected int updateChangeLogID(final K k, Long logID,  CachePolicy p){
        var attr = sqlExpressionBuilder().exp("changeLogID").eq(logID).result();
        //传入CachePolicy，多租户模式下p.getTenantID()报错
        return partialUpdateToDb(k,attr,null, p);
    }
    protected int partialUpdateToDb(final K k, final SqlExpression attributes, final SqlExpression condition, final CachePolicy p){
        if(condition == null) return repository.update(k, attributes);
        return repository.update(k, attributes, condition);
    }
    @Override
    public int partialUpdate(final K k, final SqlExpression attributes, final SqlExpression condition, final CachePolicy p)
            throws OperationFailedException,NoRecordsAffectedException{
        try{
            int r = partialUpdateToDb(k, attributes,condition,p);
            if(r == 0){
                LocalizedMessage msg = i18n.getLocalizedMessage(NoRecordsAffectedException.CODE);
                throw new NoRecordsAffectedException(msg);
            }
            if(p.cached() || cacheProvider.existsKey(k)){
                if(!tryRemoveFromCache(k)){
                    //未能清除缓存是一个错误，可能导致get从缓存还能取得
                    logger.error(String.format("清缓存%1$s：%2$s失败。", tClassName, k));
                }
            }
            return r;
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("updatePartial", k, ex);
        }
    }
    //endregion

    //region partialUpdateAll
    protected List<K> partialUpdateAllToDb(final Collection<Map<String, Object>> listOfKeyAndAttributes, final CachePolicy p){
        List<K> updatedKeys = new ArrayList<>();
        repository.updateAll(listOfKeyAndAttributes, updatedKeys);
        return updatedKeys;
    }

    @Override
    public int partialUpdateAll(final Collection<Map<String, Object>> listOfKeyAndAttributes, final CachePolicy p){
        try{
            List<K> updatedKeys = partialUpdateAllToDb(listOfKeyAndAttributes, p);
            if(p.cached()){
                tryRemoveAllFromCache(updatedKeys);//必须先移除
            }
            return updatedKeys.size();
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("partialUpdateAll", listOfKeyAndAttributes, ex);
        }
    }


    protected int partialUpdateAllToDb(final SqlExpression attributes, final SqlExpression condition, final CachePolicy p){
        return repository.updateAll(attributes, condition);
    }
    @Override
    public int partialUpdateAll(final SqlExpression attributes, final SqlExpression condition, final CachePolicy p) throws OperationFailedException {
        try{
            if(p.cached()){
                List<K> keys = getAllKeysFromDbBy(condition,p);
                tryRemoveAllFromCache(keys);//必须先移除
            }
            return partialUpdateAllToDb(attributes, condition, p);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("partialUpdateAll", condition, ex);
        }
    }
    //endregion

    //region updateAll
    protected int updateAllToDb(final Collection<T> tCollection, final CachePolicy p){
        int[] result = repository.updateAll(tCollection);
        return Arrays.stream(result).sum();
    }
    public int updateAll(final Collection<T> tCollection, final CachePolicy p) throws OperationFailedException{
        try{
            if(p.cached()){
                List<K> keys = tCollection.stream().map(t -> asKey(t)).collect(Collectors.toList());
                tryRemoveAllFromCache(keys);//必须先移除
            }
            return updateAllToDb(tCollection, p);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("updateAll", tCollection, ex);
        }
    }
    //endregion

    //region get

    protected Consumer<T> afterGot(){
        return null;
    }
    protected T getFromCacheOrDb(final K k, final CachePolicy p){
        T t = null;
        if(p.cached()) t = getFromCache(k);
        if(t == null) t = repository.find(k);
        return t;
    }
    protected T getFromDbByNo(final String no, CachePolicy p){
        return repository.findByUniqueKey(no);
    }
    /**
     * 获取单个实体
     *
     * @param k 主键
     * @param p 缓存策略
     * @return
     * @throws NotFoundException
     */
    @Override
    public T get(final K k, final CachePolicy p) throws NotFoundException,OperationFailedException{
        try{
            T t = getFromCacheOrDb(k,p);
            applyConsumerIfNonNull(t, afterGot());
            if(p.readThrough()){
                if(!tryPutToCacheIfAbsent(t, p.timeout()))
                    logger.warn(String.format("写缓存%1$s：%2$s失败。", tClassName, t));
            }
            return t;
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (EmptyResultDataAccessException ex){
            throw notFound("get", k, ex);
        }
        catch (Exception ex){
            throw operationFailed("get", k, ex);
        }
    }

    /**
     * 根据单据号获取单个实体，单据号在租户数据集合中是唯一的。
     * 例如ReceivingNote.recNo
     * @param no 单据号
     * @param p 缓存策略
     * @return
     * @throws NotFoundException
     */
    public T getByNo(final String no, final CachePolicy p) throws NotFoundException,OperationFailedException{
        try{
            T t =  getFromDbByNo(no, p);
            applyConsumerIfNonNull(t, afterGot());
            if(p.readThrough()){
                if(!tryPutToCache(t, p.timeout()))
                    logger.warn(String.format("写缓存%1$s：%2$s失败。", tClassName, t));
            }
            return t;
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (EmptyResultDataAccessException ex){
            throw notFound("getByNo", no, ex);
        }
        catch (Exception ex){
            throw operationFailed("getByNo", no, ex);
        }
    }
    @Override
    public Optional<T> getOrDefault(final K k, final CachePolicy p, final T d){
        try{
            T t = get(k, p);
            return Optional.of(t);
        }
        catch (NotFoundException ex){
            return Optional.ofNullable(d);
        }
    }

    //endregion

    //region getAny
    protected T getAnyFromDb(final String condition, final CachePolicy p, Object... args){
        return repository.findFirst(condition,args);
    }
    protected T getAnyFromDb(final SqlExpression condition, final CachePolicy p){
        return repository.findFirst(condition);
    }
    @Override
    public T getAny(final String condition, final CachePolicy p, Object... args) throws NotFoundException{
        try{
            T t = getAnyFromDb(condition, p, args);
            applyConsumerIfNonNull(t, afterGot());
            if(p.readThrough()){
                if(!tryPutToCache(t, p.timeout()))
                    logger.warn(String.format("写缓存%1$s：%2$s失败。", tClassName, t));
            }
            return t;
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw notFound("getAny", condition, ex);
        }
    }
    @Override
    public T getAny(final SqlExpression condition, final CachePolicy p) throws NotFoundException{
        try{
            T t = getAnyFromDb(condition, p);
            applyConsumerIfNonNull(t, afterGot());
            if(p.readThrough()){
                if(!tryPutToCache(t, p.timeout()))
                    logger.warn(String.format("写缓存%1$s：%2$s失败。", tClassName, t));
            }
            return t;
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw notFound("getAny", condition, ex);
        }
    }
    @Override
    public Optional<T> getAnyOrDefault(final String condition, final CachePolicy p, final T d, Object... args){
        try {
            T t = getAny(condition, p, args);
            return Optional.of(t);
        }
        catch (NotFoundException ex){
            return Optional.ofNullable(d);
        }
    }

    //endregion

    //region getAllKeys
    protected List<K> getAllKeysFromDbBy(final String condition, final CachePolicy p, Object... args){
        return repository.findAllKeysBy(condition, args);
    }
    protected List<K> getAllKeysFromDbBy(final SqlExpression condition, final CachePolicy p){
        return repository.findAllKeysBy(condition);
    }
    //endregion

    //region getAll (no cache)

    protected List<T> getList(Supplier<List<T>> listSupplier) throws OperationFailedException{
        try{
            return listSupplier.get();
        }
        catch (Exception ex){
            throw operationFailed("getList", "", ex);
        }
    }
    protected PagedList<T> getPagedList(Supplier<PagedList<T>> pagedListSupplier) throws OperationFailedException{
        try{
            return pagedListSupplier.get();
        }
        catch (Exception ex){
            throw operationFailed("getPagedList", "", ex);
        }
    }
    @Override
    public List<T> getAll(final CachePolicy p) throws OperationFailedException{
        return getList(()->repository.findAll());
    }

    @Override
    public List<T> getAll(final Sort sort, final CachePolicy p) throws OperationFailedException{
        BaseUtil.requireNonNull(sort,"sort");
        return getList(()->repository.findAll(sort));
    }

    @Override
    public PagedList<T> getAll(final Paginator paginator, final CachePolicy p) throws OperationFailedException{
        BaseUtil.requireNonNull(paginator,"pager");
        BaseUtil.requireNonNull(p,"p");
        return getPagedList(()->repository.findAll(paginator));
    }

    @Override
    public List<T> getAllBy(final SqlExpression condition, final Sort sort, final CachePolicy p) throws OperationFailedException{
        BaseUtil.requireNonNull(condition,"condition");
        BaseUtil.requireNonNull(sort,"sort");
        BaseUtil.requireNonNull(p,"p");
        return getList(() -> repository.findAllBy(condition, sort));
    }
    @Override
    public List<T> getAllBy(final SqlExpression condition, final CachePolicy p) throws OperationFailedException{
        BaseUtil.requireNonNull(condition,"condition");
        BaseUtil.requireNonNull(p,"p");
        return getList(()->repository.findAllBy(condition));
    }
    @Override
    public List<T> getAllBy(final String condition, final Sort sort, CachePolicy p, Object... args) throws OperationFailedException{
        BaseUtil.requireNonBlank(condition,"condition");
        BaseUtil.requireNonNull(sort,"sort");
        BaseUtil.requireNonNull(p,"p");
        return getList(() -> repository.findAllBy(condition, sort, args));
    }



    @Override
    public PagedList<T> getAllBy(final Paginator paginator, final String condition, final CachePolicy p, Object... args) throws OperationFailedException{
        BaseUtil.requireNonNull(paginator,"pager");
        BaseUtil.requireNonBlank(condition,"condition");
        BaseUtil.requireNonNull(p,"p");
        return getPagedList(() -> repository.findAllBy(paginator,condition, args));
    }

    @Override
    public PagedList<T> getAllBy(final Paginator paginator, final SqlExpression condition, final CachePolicy p) throws OperationFailedException{
        BaseUtil.requireNonNull(paginator,"pager");
        BaseUtil.requireNonNull(condition,"condition");
        BaseUtil.requireNonNull(p,"p");
        return getPagedList(()->repository.findAllBy(paginator,condition));
    }
    //endregion


    //region getAllIn or not in
    @Override
    public List<T> getAllIn(final String field, final String list, final boolean ordered, final CachePolicy p,
                            final String condition, Object...args) throws OperationFailedException{
        try{
            boolean hasCond = BaseUtil.hasText(condition);
            if(p.cached() && !hasCond){
                String listCacheKey = cacheProvider.getListCacheKey(field+(ordered?"OL":"UL"),list);
                List<T> result = cacheProvider.getList(listCacheKey);
                if(result==null || result.isEmpty()) result = repository.findAllIn(field, list, ordered);
                if(p.readThrough() && !result.isEmpty()){
                    cacheProvider.putList(listCacheKey, result);
                }
                return result;
            }

            return hasCond
                    ? repository.findAllIn(field, list, ordered, condition, args)
                    : repository.findAllIn(field, list, ordered);
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
            boolean hasCond = BaseUtil.hasText(condition);
            if(p.cached() && !hasCond){
                String listCacheKey = cacheProvider.getListCacheKey(field+"!", paginator.toString(),list);
                List<T> data = cacheProvider.getList(listCacheKey);
                boolean hasCache = data != null && !data.isEmpty();
                PagedList<T> result = hasCache
                        ? PagedList.of(data, paginator) //TODO pager没有计数
                        : repository.findAllNotIn(paginator, field, list);

                if(p.readThrough() && !hasCache && !result.isEmpty()){
                    cacheProvider.putList(listCacheKey, result.getData());
                }
                return result;
            }

            return hasCond
                    ? repository.findAllNotIn(paginator, field, list, condition, args)
                    : repository.findAllNotIn(paginator, field, list);
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
    protected PagedList<T> searchAllFromDb(final Paginator paginator, final String word, final CachePolicy p){
        return repository.searchAll(paginator, word);
    }
    protected PagedList<T> searchAllFromDb(final Paginator paginator, final String word, final CachePolicy p, final String condition, Object...args){
        return repository.searchAll(paginator, word, condition, args);
    }
    protected List<T> searchAllFromDb(final String word, final Sort sort, final CachePolicy p){
        return repository.searchAll(word, sort);
    }
    protected List<T> searchAllFromDb(final String word, final Sort sort, final CachePolicy p, final String condition, Object...args){
        return repository.searchAll(word, sort, condition, args);
    }

    private PagedList<T> tryGetSearchPage(final String word, Supplier<PagedList<T>> supplier){
        try{
            return supplier.get();
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("searchAll", word, ex);
        }
    }
    private List<T> tryGetSearchResult(final String word, Supplier<List<T>> supplier){
        try{
            return supplier.get();
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("searchAll", word, ex);
        }
    }
    @Override
    public PagedList<T> searchAll(final Paginator paginator, final String word, final CachePolicy p) throws OperationFailedException{
        return tryGetSearchPage(word,()->searchAllFromDb(paginator, word, p));
    }
    @Override
    public PagedList<T> searchAll(final Paginator paginator, final String word, final CachePolicy p, final String condition, Object...args) throws OperationFailedException{
        return tryGetSearchPage(word,()->searchAllFromDb(paginator, word, p, condition, args));
    }
    @Override
    public List<T> searchAll(final String word, final Sort sort, final CachePolicy p) throws OperationFailedException{
        return tryGetSearchResult(word,()->searchAllFromDb(word, sort, p));

    }
    @Override
    public List<T> searchAll(final String word, final Sort sort, final CachePolicy p, final String condition, Object...args) throws OperationFailedException{
        return tryGetSearchResult(word,()->searchAllFromDb(word, sort, p, condition, args));
    }

    //endregion

    //region export
    @Override
    public PagedList<T> getAllExport(final Paginator paginator, final String word, final CachePolicy p, final String condition, Object...args) throws OperationFailedException{
        return tryGetSearchPage(word,()->{
            if (BaseUtil.hasText(word)) {
                return BaseUtil.hasText(condition)
                        ? searchAll(paginator, word, p, condition, args)
                        : searchAll(paginator, word, p);
            } else {
                return BaseUtil.hasText(condition)
                        ? getAllBy(paginator, condition, p,args)
                        : getAll(paginator, p);
            }
        });
    }
    public PagedList<T> getAllExportWithParams(final Paginator paginator, final String word, final CachePolicy p, final Map<String, Object> params, final String condition, Object...args){
        return getAllExport(paginator, word, p, condition, args);
    }

    //endregion

    //region count (no cache)
    protected int countFromDb(final CachePolicy p){
        return repository.count();
    }
    protected int countFromDb(final String condition, final CachePolicy p, Object... args){
        return repository.count(condition,args);
    }
    protected int countFromDb(final SqlExpression condition, final CachePolicy p){
        return repository.count(condition);
    }
    protected int countFromDb(final String word,final String condition, final CachePolicy p, Object... args){
        return repository.count(word,condition,args);
    }
    protected int countFromDb(final String word,final CachePolicy p){
        return repository.countBySearchWord(word);
    }
    protected boolean existsInDb(final K k, final CachePolicy p){
        return repository.exists(k);
    }
    @Override
    public int count(final CachePolicy p){
        try{
            return countFromDb(p);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("count", "", ex);
        }
    }
    @Override
    public int countBy(final String condition, final CachePolicy p, Object... args){
        try{
            return countFromDb(condition,p,args);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("count", condition, ex);
        }
    }
    @Override
    public int countBy(final SqlExpression condition, final CachePolicy p){
        try{
            return countFromDb(condition,p);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("count", condition, ex);
        }
    }

    @Override
    public int countBy(String word, String condition, CachePolicy p, Object... args) {
        try{
            return countFromDb(word,condition,p,args);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("count", condition, ex);
        }
    }
    @Override
    public int countBySearchWord(String word,  CachePolicy p) {
        try{
            return countFromDb(word,p);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("count", word, ex);
        }
    }

    @Override
    public boolean exists(final K k, final CachePolicy p){
        try{
            if(p.cached()){
                if(cacheProvider.existsKey(k)) return true;
            }
            return existsInDb(k,p);
        }
        catch (DomainException ex){
            throw ex;
        }
        catch (Exception ex){
            throw operationFailed("exists", k, ex);
        }
    }
    @Override
    public int prepareExportRecordCount(final String word,final Map<String,Object> params, final String condition, final CachePolicy p){
        // 计算记录数
        if (BaseUtil.hasText(word)) {
            int count = BaseUtil.hasText(condition) ?
                    countBy(word, condition, p)
                    : countBySearchWord(word, p);
            return count;
        } else {
            int count = BaseUtil.hasText(condition) ?
                    countBy(condition, p)
                    : count(p);
           return count;
        }
    }
    //endregion

    //region metadata

    @Override
    public MetaObject getMetaObject(){
        return repository.getMetaObject();
    }
    @Override
    public MetaObject getMetaObject(final String dbSchema, final String objName){
        MetaObject mo = cacheProvider.getMetaObject(dbSchema, objName);
        if(mo == null) {
            mo = repository.getMetaObject(dbSchema, objName);
            cacheProvider.putMetaObject(mo);
        }
        return mo;
    }

    private synchronized Map<String,MetaObject> assembleMetadata(){
        Map<String,MetaObject> md = new HashMap<>();
        MetaObject master = getMetaObject();
        md.put("master",master);
        String db = master.getDbSchema();
        for(MetaRelation relation : master.getRelations()){
            if(relation.getRelationType() != MetaRelationType.HAS_ONE && relation.getRelationType() != MetaRelationType.HAS_MANY) continue;
            MetaObject relative = getMetaObject(db,relation.getRelativeObjName());
            md.put(relation.getRelationName(),relative);
        }
        return md;
    }
    @Override
    public Map<String,MetaObject> getMetadata(){
        if(metadata == null) metadata = assembleMetadata();
        return metadata;
    }

    @Override
    public MetaUi getMetaUi(String lang, CachePolicy p,boolean reload){
        return repository.getMetaUi(p.getTenantID(),lang,reload);
    }
    //endregion

    //region refs & enums assemble

    /**
     * 获取实体引用字段对应的关联对象引用字典的缓存键值
     * @param t
     * @param relation
     * @return
     */
    private String getCacheKeyOfRefMap(final T t, final MetaRelation relation){
        String relativeDbName = relation.getRelativeDbSchema() != null ? relation.getRelativeDbSchema() : relation.getDbSchema();
        MetaObject metaObj = getMetaObject(relativeDbName,relation.getRelativeObjName());
        assert metaObj!=null;

//        String cacheKey= col.getEnumSet().replaceAll(" ","_");
        String cacheKey = "REF_"+metaObj.getObjName();//缓存统一命名，便于修改数据后刷新
        if(BaseUtil.hasText(metaObj.getPartitionKey()) && t instanceof Tenancy){
            int tenantID = ((Tenancy)t).getTenantID();//不会出现通用实体引用多租户数据
            cacheKey = cacheProvider.joinCacheKey(String.valueOf(tenantID),cacheKey);
        }
        return cacheKey;
    }

    /**
     * 尝试清除引用缓存，更新后调用。比如解决仓库名称修改后组装引用属性仍然是老名字问题
     * @param t
     */
    protected void tryEvictRefCache(final T t){
        try{
            String cacheKey = "REF_"+tClassName;
            cacheProvider.evictRefKey(cacheKey,t.getId().toString());
        }
        catch (Exception ex){
            String msg = String.format("清除%1$s引用缓存异常。", tClassName);
            logger.error(msg, ex);
        }
    }

    /**
     * 查找单个引用值对应的显示文本
     * @param refEnumMapCacheKey
     * @param relation
     * @param refValue
     * @return
     */
    private String findRefText(final String refEnumMapCacheKey, final MetaRelation relation, final Object refValue){
        //NULL不用组装引用文本，但是空字符串可能需要
        if(refValue==null) return LOST_REF_TEXT;

        try{
            String refText = repository.findRefTextByValue(relation,refValue);
            assert refText!=null;

           if (BaseUtil.hasText(refEnumMapCacheKey))
               cacheProvider.setRefEnumText(refEnumMapCacheKey, refValue.toString(), refText);
            return refText;
        }
        catch (Exception ex){
            return LOST_REF_TEXT;
        }
    }

    /**
     * 载入引用字典，并自动缓存。
     * 被{@link TenancyEntityService#loadRefEnumMap(String, MetaRelation)}覆盖
     * @param relation
     * @return
     */
    protected Map<String, String> loadRefEnumMap(final String refEnumMapCacheKey, final MetaRelation relation){
        List<MetaEnumMember> items = repository.findAllRefMap(relation);
        //处理空结果集
        if (items.isEmpty()) {
            return Collections.unmodifiableMap(EMPTY_REF_ENUM_MAP) ;
        }
        Map<String, String> refEnumMap = items.stream().collect(Collectors.toMap(MetaEnumMember::getName, MetaEnumMember::getText,(existing, replacement) -> replacement));
        cacheProvider.putRefMap(refEnumMapCacheKey, refEnumMap);
        return refEnumMap;
    }
    /**
     * 组装引用属性
     * @param t 实体对象
     * @param col 字段
     * @return 返回组装了引用属性的实体，t.getRefProperty("userID") = userName
     */
    private T assebmleRefProperty(final T t, final MetaCol col, final MetaRelation relation){
        try{
            Object propVal = repository.getPropertyValue(t, col.getColName());
            if(null==propVal) return t;
            if(col.getDataType().isBigInt() && (Long) propVal==0L) return t;

            String cacheKeyOfRefRefMap = getCacheKeyOfRefMap(t, relation);
            String refValue = propVal.toString();

            if (relation.isOneTime()) {
                // oneTime: 只查一次，不走 Redis 缓存
                String refEnumText = findRefText(null, relation, propVal);
                t.setRefProperty(col.getColName(), refEnumText);
                return t;
            }

            if(cacheProvider.existsRefMap(cacheKeyOfRefRefMap)){
                String refEnumText = cacheProvider.getRefText(cacheKeyOfRefRefMap,refValue);
                if(refEnumText == null) {
                    refEnumText = findRefText(cacheKeyOfRefRefMap,relation,propVal);
                    cacheProvider.setRefEnumText(cacheKeyOfRefRefMap,refValue,refEnumText);
                }
                t.setRefProperty(col.getColName(),refEnumText);
            }
            else{
                if(t instanceof Tenancy){
                    int tenantID = ((Tenancy)t).getTenantID();//不会出现通用实体引用多租户数据
                }
                Map<String,String> refEnumMap = loadRefEnumMap(cacheKeyOfRefRefMap, relation);
                t.setRefProperty(col.getColName(), refEnumMap.get(refValue));
            }
        }
        catch (Exception ex){
            String msg = String.format("组装%1$s.%2$s引用属性值异常。", tClassName, col.getColName());
            logger.error(msg, ex);
        }
        return t;
    }


    /**
     * 组装引用属性，涉及到缓存，在Service层组装
     * @param t
     * @return
     */
    @Override
    public T assembleRefProperties(T t){
        Objects.requireNonNull(t);
        //不要重复组装
        if(t.isAssembled(Entity.ASSEMBLE_REF)) return t;

        //如果有引用属性
        var refColRelationMap = repository.getRefColRelations();
        if (refColRelationMap != null && !refColRelationMap.isEmpty()) {
        //if(!refColRelationMap.isEmpty()){    //解决  Cannot invoke "java.util.concurrent.ConcurrentMap.isEmpty()" because "refColRelationMap" is null
            //组装所有引用属性值对应的文本
            refColRelationMap.forEach((col, relation) -> assebmleRefProperty(t, col, relation));
        }
        t.setAssembled(Entity.ASSEMBLE_REF);
        return t;
    }


    /**
     * 组装枚举属性，枚举是常数，因此在Repository层就可组装
     * @param t
     * @return
     */
    @Override
    public T assembleEnumProperties(T t){
        Objects.requireNonNull(t);
        return repository.assembleEnumProperties(t);
    }

    //endregion

    //region entity filters

    protected EntityFilter buildRefFilter(MetaCol col, String lang, CachePolicy p){
        return repository.buildRefFilter(col);
    }
    /**
     * 创建实体过滤器，子类可覆盖。
     * TODO 实现多语言版本createEntityFilters
     * @return 过滤器列表，客户端用于渲染过滤UI
     */
    protected synchronized List<EntityFilter> createEntityFilters(String lang, CachePolicy p){
        MetaObject metaObj = getMetaObject();
        List<EntityFilter> filters = new ArrayList<>();
        String fixedFilterName = metaObj.getFixedFilter();
        boolean fixed = fixedFilterName == null;
        for (MetaCol col : metaObj.getCols()){
            //仓库通常不过滤
            if(!col.isFilterable() || col.getColName().equals("whID")) continue;
            //默认创建枚举和日期字段
            if(col.getRelationType() == MetaRelationType.ENUM){
                EntityFilter filter = repository.buildEnumFilter(col);
                if(!fixed) filter.setFixed(filter.getFilterName().equals(fixedFilterName));
                filters.add(filter);
            }
            else if(col.getRelationType() == MetaRelationType.REF){
                EntityFilter filter =buildRefFilter(col,lang,p);
                if(!fixed) filter.setFixed(filter.getFilterName().equals(fixedFilterName));
                filters.add(filter);
            }
            else {
                if(col.getDataType().hasDatePart())
                    filters.add(repository.buildDateFilter(col));
                else if(col.getDataType() == DataType.BOOL)
                    filters.add(repository.buildBoolFilter(col));
            }
        }
        return filters;
    }

    @Override
    public List<EntityFilter> getEntityFilters(String locale, CachePolicy p, boolean reload){
        if(entityFilters == null || reload){
            entityFilters = createEntityFilters(locale,p);
        }
        return entityFilters;
    }

    protected synchronized List<EntitySortSet> createEntitySorts(String locale, CachePolicy p){
        MetaObject metaObj = getMetaObject();

        List<EntitySortSet> sortSets = new ArrayList<>();
        SortSet defSortSet = SortSet.of("", getDefaultSort());
        EntitySortSet ess = new EntitySortSet("defaultSort","默认", defSortSet);
        sortSets.add(ess);

        for (MetaCol col : metaObj.getCols()) {
            if(!col.isFilterable()) continue;

            SortSet descSortSet = SortSet.of("降序",Sort.of("t."+col.getColName(), Sort.Order.DESC));
            SortSet ascSortSet = SortSet.of("升序",Sort.of("t."+col.getColName(), Sort.Order.ASC));
            EntitySortSet colSortSet = new EntitySortSet(col.getColName(),col.getDisplayLabel(),ascSortSet,descSortSet);
            sortSets.add(colSortSet);
        }
        return  sortSets;
    }
    @Override
    public List<EntitySortSet> getEntitySorts(String locale, CachePolicy p, boolean reload){
        if(entitySorts == null || reload){
            entitySorts = createEntitySorts(locale,p);
        }
        return entitySorts;
    }
    //endregion


    //region load & save
    /**
     * 载入整个实体，默认只载入本身
     *
     * 留给子类实现一并载入子表、关联表和引用属性
     * @param k 主键值
     * @param p 缓存策略
     * @return
     */
    @Override
    public T load(K k, CachePolicy p) throws NotFoundException{
        return get(k, p);
    }


    /**
     * 根据实体状态保存数据，子类需实现子表保存
     * 实体不能直接调用{@link Repository#save(Object)} 否则拦截器没有执行。
     * 子表等没有逻辑的可直接调用<code>repository.save(T)</code>
     * 可能插入、更新和删除，用于主表和子表一起提交的时候控制子表的保存，
     * 删除主表请使用{@link #delete(Entity, CachePolicy)}，即t参数的实体状态不能为{@link EntityState#DELETED}
     *
     * @param t 实体对象
     * @param p 缓存策略
     * @param checkExists 先检查数据库中实体是否存在，不使用entityState
     * @param changeLog 额外的修改日志，比如连带着修改了其他实体
     * @return
     */
    @Override
    public int save(T t, CachePolicy p, boolean checkExists, final Map<String,Object> changeLog)
            throws NoRecordsAffectedException, OperationFailedException{
        int r = 0;
        if(checkExists){
            if(existsInDb(asKey(t), p)) r = update(t, p);
            else r = insert(t, p);
        }
        else{
            switch (t.getEntityState()){
                case EntityState.CREATED,
                     EntityState.NEW_MODIFIED:  r = insert(t,p); break;
                case EntityState.MODIFIED:      r = updateWithChangeLog(t,p,changeLog); break;
                case EntityState.DELETED:       r = delete(t,p); break;
//                case EntityState.DELETED:       throw new OperationNotAllowException("删除不能使用save，请使用delete");
                default:                        break;
            }
        }
        return r;
    }

    //endregion

    //region export
    // 导出空表格模板、导出查询结果
    // 导出表单（可选择模板、上传模板）

    /**
     * 获取所有报表模板，给用户选择
     * @param p 缓存策略
     * @return 所有可用的报表模板
     */
    public List<ReportTemplate> getReportTemplates(CachePolicy p){
        return reportTemplateService.getAllReportTemplatesOf(this.tClassName, p.ofDuration(CachePolicy.DEF_EXPIRED_1_WEEK));
    }

    /**
     * 添加报表模板
     * @param t 报表模板实体对象
     * @param p 缓存策略
     * @return
     */
    public int saveReportTemplate(ReportTemplate t, boolean checkExists, CachePolicy p){
        return reportTemplateService.save(t, p, checkExists);
    }


    /**
     * 将tList列表数据导出Excel文件，
     * @param tList 实体数据列表
     * @param templateId 报表模板标识
     * @param colNames 列名称集合
     * @param tenantID 租户ID
     * @param print 只显示详情，不加载下拉框
     * @return 导出的临时文件
     * @remarks templateId不为空的时候 colNames 忽略
     */
    public File exportExcel(List<T> tList, Long templateId, Collection<String> colNames,String format,boolean print, int tenantID) {
        try {
            ExcelFileWriter<T, K> excelExporter = new ExcelFileWriter<T, K>(repository);
            MetaObject metaObj = getMetaObject();
            var refDatas=print?null:assembleRefData(getDefaultCachePolicy(tenantID));
            if (!BaseUtil.isNullOrZero(templateId)) {
                ReportTemplate reportTemplate = reportTemplateService.getById(templateId);
                return excelExporter.writeAll(reportTemplate.getTemplateFile(), tList, metaObj.getObjName(), tenantID,format,refDatas,print?false:true);
            }
            return excelExporter.writeAll(tList, colNames, metaObj.getObjName(), tenantID,format);
        }
        catch (Exception ex){
            throw operationFailed("exportToExcel", colNames, ex);
        }
    }
    public File exportFailedExcel(File file, Long templateId, List<FileImportErrorInfo> errorInfos) {
        try {
            ExcelFileWriter<T, K> excelExporter = new ExcelFileWriter<T, K>(repository);
            if (!BaseUtil.isNullOrZero(templateId)) {
                ReportTemplate reportTemplate = reportTemplateService.getById(templateId);
                return excelExporter.writeError(errorInfos, file,reportTemplate.getTemplateFile());
            }
            return excelExporter.writeError(errorInfos, file,null);
        }
        catch (Exception ex){
            throw operationFailed("exportToExcel", errorInfos, ex);
        }
    }

    public File exportExcelTemplate(long templateId,Map<MetaRelation, List<Object>> refDatas) {
        try {
            ExcelFileWriter<T, K> excelExporter = new ExcelFileWriter<T, K>(repository);
            ReportTemplate reportTemplate = reportTemplateService.getById(templateId);
            return excelExporter.writeBasicDataForTemplate(reportTemplate.getTemplateFile(), refDatas);
        }
        catch (Exception ex){
            throw operationFailed("exportExcelTemplate", templateId, ex);
        }
    }

    /**
     * 导出Excel模板文件，全部字段导出，有标题栏并能提示如何填写，最终用于导入
     * @return
     */
    public File exportExcelTemplate(int tenantID){
        try{
            ExcelFileWriter<T,K> excelExporter = new ExcelFileWriter<T,K>(repository);
            MetaObject metaObj = getMetaObject();
            return excelExporter.writeAll(new ArrayList<T>(), null, metaObj.getObjName(), tenantID);
        }
        catch (Exception ex){
            throw operationFailed("exportExcelFile", null, ex);
        }
    }
//
//    public File exportGroupTableFile(List<T> tList, Collection<String> colNames,String relationName,String groupColName ,String categoryColName){
//        try{
//            ExcelExporter<T,K> excelExporter = new ExcelExporter<T,K>(repository,relationName);
//            MetaObject metaObj = getMetaObject();
//
//            return excelExporter.exportGroupTableFile(colNames, tList, metaObj.getObjName(),groupColName,categoryColName);
//        }
//        catch (Exception ex){
//            throw operationFailed("exportExcel", colNames, ex);
//        }
//    }
//    public File exportTemplateFile( int tenantID,List<T> tList,Collection<String> colNames){
//        try{
//            ExcelExporter<T,K> excelExporter = new ExcelExporter<T,K>(repository);
//            MetaObject metaObj = getMetaObject();
//            List<MetaCol> cols = colNames.stream()
//                    .map(colName->metaObj.getCol(colName))
//                    .collect(Collectors.toList());
//
//            return excelExporter.exportTemplateFile(tenantID,cols,tList, metaObj.getObjName());
//        }
//        catch (Exception ex){
//            throw operationFailed("exportExcel", colNames, ex);
//        }
//    }
//
//    public File exportTemplateFile( int tenantID,List<T> tList,Collection<String> colNames,Map<String,List<Map<String, Object>>> relatives){
//        try{
//            ExcelExporter<T,K> excelExporter = new ExcelExporter<T,K>(repository);
//            MetaObject metaObj = getMetaObject();
//            List<MetaCol> cols = colNames.stream()
//                    .map(colName->metaObj.getCol(colName))
//                    .collect(Collectors.toList());
//
//            return excelExporter.exportTemplateFile(tenantID,cols,tList,relatives, metaObj.getObjName());
//        }
//        catch (Exception ex){
//            throw operationFailed("exportExcel", colNames, ex);
//        }
//    }
//
//    public File exportJasperReportTableFile(String reportName,Locale locale,List<Object> bindingData,Map<String,Object> params,String fileName,String suffix){
//        try{
//            JasperReportExporter<T,K> excelExporter = new JasperReportExporter<T,K>(repository);
//
//            return excelExporter.exportTableFile(reportName,locale,bindingData,params, fileName, suffix);
//        }
//        catch (Exception ex){
//            throw operationFailed("export"+suffix,"", ex);
//        }
//    }
//
//    public File exportJasperReportFile(String reportName,Locale locale,List<Object> bindingData,Map<String,Object> params,String fileName,String suffix){
//        try{
//            JasperReportExporter<T,K> excelExporter = new JasperReportExporter<T,K>(repository);
//
//            return excelExporter.exportFile(reportName,locale,bindingData,params, fileName, suffix);
//        }
//        catch (Exception ex){
//            throw operationFailed("export"+suffix,"", ex);
//        }
//    }
//    public File exportJasperReportFile(String reportName,Locale locale,List<Object> bindingData,String fileName,String suffix){
//        try{
//            JasperReportExporter<T,K> excelExporter = new JasperReportExporter<T,K>(repository);
//
//            return excelExporter.exportFile(reportName,locale,bindingData, fileName, suffix);
//        }
//        catch (Exception ex){
//            throw operationFailed("export"+suffix,"", ex);
//        }
//    }
    //endregion of export

    //region import
    public List<T> importExcel(int tenantID,File file){
        try{
            ExcelFileReader<T,K> excelImporter = new ExcelFileReader<T,K>(repository);
            return excelImporter.readAll(tenantID,file);
        }
        catch (Exception ex){
            throw operationFailed("excelImporter","", ex);
        }
    }

    public List<T> importExcel(int tenantID,File file,Long templateID){
        try{
            ExcelFileReader<T,K> excelImporter = new ExcelFileReader<T,K>(repository);
            if (!BaseUtil.isNullOrZero(templateID)) {
                ReportTemplate reportTemplate = reportTemplateService.getById(templateID);
                return excelImporter.readAll(tenantID,file,reportTemplate.getTemplateFile());
            }

            return excelImporter.readAll(tenantID,file);
        }catch (ExcelException ex){
            throw ex;
        }catch (Exception ex){
            throw operationFailed("excelImporter","", ex);
        }
    }
    //endregion

    //region module & auth

    /**
     * 获取模块，如果实体没有在模块中，则返回系统模块
     * @param locale
     * @return
     */
    public Optional<Module> getModule(final Locale locale){
//        var metadataProvider = this.getMetadataProvider();
//        try{
//            var module = metadataProvider.getModule(this.getMetaObject(),locale.toString());
//            return Optional.of(module);
//        }
//        catch (Exception ex){
//            var metaObj = getMetaObject();
//            var metaDb = metadataProvider.getMetaDb(metaObj.getDbSchema());
//            return Optional.of(metadataProvider.getModule(metaDb.getSystemCode(), locale.toString()));
//        }
        return getModule(null,locale);
    }
    /**
     * 获取模块，如果实体没有在模块中，则返回系统模块
     *
     * @param moduleCode 模块编码，可为空
     * @param locale     区域信息
     * @return 模块的 Optional 包装
     */
    public Optional<Module> getModule(String moduleCode, final Locale locale) {
        var metadataProvider = this.getMetadataProvider();

        try {
            // 如果 moduleCode 不为空，则根据 moduleCode 获取模块
            if (BaseUtil.hasText(moduleCode)) {
                var module = metadataProvider.getModule(moduleCode, locale.toString());
                return Optional.of(module);
            }
            // 如果 moduleCode 为空，则根据实体的元对象获取模块
            var module = metadataProvider.getModule(this.getMetaObject(),locale.toString());
            return Optional.of(module);
        } catch (Exception ex) {
            // 异常时返回系统模块
            var metaObj = getMetaObject();
            var metaDb = metadataProvider.getMetaDb(metaObj.getDbSchema());
            return Optional.of(metadataProvider.getModule(metaDb.getSystemCode(), locale.toString()));
        }
    }

    /**
     * 获取模块操作
     * @param actionName 操作名称
     * @param locale 语言区域
     * @return 返回模块操作或者空
     */
    public Optional<ModuleAction> getModuleAction(final String actionName, final Locale locale){
        var module = getModule(locale);
        if(module.isPresent()) return module.get().getActionByName(actionName);
        return Optional.empty();
    }


    public Authority getModuleAuthority(long userId, final Locale locale){
        return getModuleAuthority(userId, null, locale);
    }
    public Authority getModuleAuthority(long userId, String moduleCode, final Locale locale) {
        var metadataProvider = this.getMetadataProvider();
        // 如果 moduleCode 未提供，则使用默认方式获取模块
        var module = BaseUtil.hasText(moduleCode)
                ? getModule(moduleCode, locale)
                : getModule(locale);

        if (module.isEmpty()) return Authority.NONE;

        var tenancyModule = metadataProvider.getTenancyModule(
                Tenancy.parseTenantID(userId),
                getSupportLangFromLocale(locale),
                module.get().getModuleCode());
        if(tenancyModule == null) return Authority.NONE;

        var roleModuleAuths = roleModuleAuthRepository.findAllByUserID(userId);
        var moduleAuth = new ModuleAuth(module.get(), false);
        for(var roleModuleAuth : roleModuleAuths){
            roleModuleAuth.addToModuleAuth(moduleAuth);
        }
        return moduleAuth.getAuthority();
    }


    public static String getSupportLangFromLocale(final Locale locale){
        String lang = locale.getLanguage();
        if("en".equals(lang)) return lang;
        else if("zh".equals(lang)){
            if(locale.equals(Locale.TRADITIONAL_CHINESE)) return "zh-Hant";
            String script = locale.getScript();
            if(BaseUtil.hasText(script) && "Hant".equals(script)) return "zh-Hant";
        }
        return "zh";
    }
    /**
     * 获取指定用户和名称的已授权模块操作
     * @param userId 用户标识
     * @param actionName 操作名称
     * @param locale 语言区域
     * @return 模块操作或者空，空可能没有权限或者操作名称不存在
     */
    public Optional<ModuleAction> getAuthorizedAction(long userId, final String actionName, final Locale locale){
        var moduleAuth = getModuleAuthority(userId,locale);
        return moduleAuth.findAuthorizedActionByName(actionName);
    }

    //endregion

    //region actions

    /**
     * 构建实体操作，供客户端渲染
     * @param action 模块操作
     * @return
     */
    protected EntityAction buildEntityAction(final ModuleAction action){
        return EntityAction.of(
                action.getActionName(),
                action.getDisplayLabel(),
                action.getDisplayIcon(),
                action.getDescription()
        ).withParam(
                EntityActionParam.ofExecute(
                        action.getPromptType().name(),
                        action.getDisplayHint()
                )
        );
    }
    protected T assembleEntityActions(final T t, final Authority authority)
    {
        return t;
    }
    /**
     * 执行操作
     * @param t 实体对象
     * @param user 执行用户
     * @param actionParam 操作参数
     * @return
     */
    @Override
    @Transactional
    public T doAction(final T t, final UserAccount user, final DomainActionParam actionParam){
        return doAction(t, user, false, actionParam);
    }

    @Override
    @Transactional
    public T doAction(T t, UserAccount user,  final boolean isAuthorized, DomainActionParam actionParam) {
        Assert.notNull(user, "user must not be null");
        Assert.notNull(t, "t must not be null");
        Assert.notNull(actionParam, "actionParam must not be null");

        var actionName = actionParam.getActionName();
        var opAction = getDomainActions().stream()
                .filter(a->a.getName().equals(actionName))
                .findFirst();

        if(!opAction.isPresent()) throw new OperationNotAllowException(actionName+".not.found");

        var action = opAction.get();
        if(!isAuthorized && !action.canExecute(t, user)) throw new OperationFailedException(actionName+".not.allow");

        action.beforeExecute(t);
        if(t instanceof Ownable ownable){
            ownable.ownedByIf(actionParam.getOwnerID(),actionParam.getOwnerDeptID());
        }
        if(t instanceof Auditable auditable){
            auditable.modifyBy(user.getUserID());
        }
        if (t instanceof FlowableEntity<?> flowable){
            flowable.setLogAsAction(true);
            if (BaseUtil.isNullOrEmpty(flowable.getChangeLogTags()))
                flowable.setChangeLogTags(String.join(" ",user.getUsername(), Date.valueOf(LocalDate.now())+":",action.getMetadata(LocaleContextHolder.getLocale()).getLabel()));
        }
        tryRemoveFromCache(t);//necessary
        return action.execute(t, user, actionParam.getPayload());
    }

    //endregion

    //region expression
    private final SpelParserConfiguration expParserConfig = new SpelParserConfiguration(SpelCompilerMode.MIXED,
            this.getClass().getClassLoader());
    private final ExpressionParser expParser = new SpelExpressionParser(expParserConfig);

    public Expression parseExpression(final String exp){
        return expParser.parseExpression(exp);
    }
    /**
     * 获取表达式值
     * @param t 实体对象
     * @param expression SpEl表达式
     * @param desiredResultType 期望返回的结果类型
     * @return 返回表达式的值
     * @param <U> 表达式结果类型
     * @throws EvaluationException 当评估表达式失败
     */
    public <U> U getExpressionValue(final T t, final String expression, Class<U> desiredResultType) throws ParseException,EvaluationException {
        var exp = expParser.parseExpression(expression);
        return exp.getValue(t,desiredResultType);
    }
    public boolean tryGetBoolExpressionValue(final T t, String expression, boolean fallbackValue){
        try{
            Boolean result = getExpressionValue(t,expression,Boolean.class);
            return result;
        }
        catch (ParseException ex){
            logger.error(String.format("Expression %s$1(%s$2) parse failure.",this.repository.getTClassName(),expression), ex);
            return fallbackValue;
        }
        catch (EvaluationException ex){
            logger.error(String.format("Expression %s$1(%s$2) evaluation failure.",this.repository.getTClassName(),expression), ex);
            return fallbackValue;
        }
    }
    public boolean tryGetBoolExpressionValue(final T t, String expression){
        return tryGetBoolExpressionValue(t,expression,false);
    }
    //endregion

    //region differ

    public ChangeLog.ChangeData differProperties(T o, T n){
        //子类可重写
        return repository.differ(o, n);
    }

    public List<ChangeLog.ChangeData> differProperties(List<T> ol, List<T> nl){
//        Assert.notNull(ol, "Old entities mustn't be null");
//        Assert.notNull(nl, "New entities mustn't be null");

        // 如果任意一个列表为空，处理空列表的情况
        if (CollectionUtils.isEmpty(ol) && CollectionUtils.isEmpty(nl)) {
            return Collections.emptyList(); // 两个都为空时，返回空列表
        }

        List<ChangeLog.ChangeData> result = new ArrayList<>();

        // 处理 ol 为空的情况，所有新列表的元素都被标记为"新增"
        if (CollectionUtils.isEmpty(ol)) {
            for (var ni : nl) {
                result.add(ChangeLog.added(ni));
            }
            return result;
        }

        // 处理 nl 为空的情况，所有旧列表的元素都被标记为"删除"
        if (CollectionUtils.isEmpty(nl)) {
            for (var oi : ol) {
                result.add(ChangeLog.removed(oi));
            }
            return result;
        }


        List<T> untouched = new ArrayList<>(nl);
        for(var oi : ol){
            var k = oi.getId();
            var ni = nl.stream().filter(i -> i.getId().equals(k)).findFirst();
            if(ni.isPresent()){
                //判断是否被删除
                if (ni.get().isDeleted()){
                    result.add(ChangeLog.removed(oi));
                }else {
                    var difference = differProperties(oi, ni.get());
                    if (difference != ChangeLog.unchanged()) {
                        //changed
//                        result.add(ChangeLog.changed(oi, difference));
                        result.add(difference);
                    }
                }
                untouched.remove(ni.get());
            }
            else{
                //removed
                result.add(ChangeLog.removed(oi));
            }
        }
        //added
        for(var ni : untouched){
            result.add(ChangeLog.added(ni));
        }
        return result;
    }

    public Map<String, Object> differ(T o, T n){
        var diff = differProperties(o,n);
        var result = new LinkedHashMap<String,Object>();
        if(diff != ChangeLog.unchanged()){
//            result.put(repository.getTClassName(), ChangeLog.changed(o,diff));
            result.put(repository.getTClassName(), diff);
        }
        return result;
    }

    //endregion of differ

    //region undo & redo
    /**
     * 撤销实体 t 数据修改，恢复到上一次操作 flowTrailId 前状态
     * @param t 实体对象
     * @param user 当前用户
     * @param flowTrailId 上一次操作产生的流程追踪标识
     * @return 成功否
     */
    public boolean undo(final T t, final UserAccount user, final Long flowTrailId){
        //默认实现，FlowableEntityService重写
        return false;
    }
    /**
     * 撤销实体 t 数据修改，恢复到上一次保存前状态
     * @param t 实体对象
     * @param user 当前用户
     * @param changeLogID 修改日志标识
     * @param deleteChangeLog 是否删除修改日志，默认标记为undone以后可以redo
     * @return 成功否
     * @throws BeansException
     */
    @Transactional
    public boolean undo(final T t, final UserAccount user, long changeLogID, boolean deleteChangeLog) throws BeansException {
        CachePolicy p = getDefaultCachePolicy(Tenancy.toTenantID(user.getUserID()));

        if(t instanceof ChangeLoggable loggable) {
            Assert.isTrue(loggable.getChangeLogID() == changeLogID, "Change log ID mismatch.");
        }

        //载入修改日志
        var changeLog = changeLogRepository.find(changeLogID);
        var difference = (LinkedHashMap<String,Object>)changeLog.readDifference();
        if(difference == null){
            throw operationFailed("undo", t, new RuntimeException("Empty change log."));
        }

        //恢复至修改前
        //todo difference should be reversed
        for(var entry : difference.entrySet()){
            var objName = entry.getKey();
//            var serviceName = NamingUtils.firstLetterLower(objName) + "Service";
            //使用getServiceName(自定义比如subOrders可由子类重写)
            var serviceName=getServiceName(objName);
            var service = ApplicationContextProvider.getApplicationContext().getBean(serviceName, DomainService.class);

            var objChange = entry.getValue();
            if(objChange instanceof ChangeLog.ChangeData<?> change){
                int r = service.revert(change,p);
            }
            else if(objChange instanceof List<?> changeList){
                for(var c : changeList.reversed()){
                    if(c instanceof ChangeLog.ChangeData<?> changeData) service.revert(changeData,p);
                    else throw new OperationFailedException("Invalid change log.");
                }
            }
            else{
                //don't know how to revert
                //todo raise an error of ChangeLog Invalid
            }
        }

        //恢复到前一次日志标识
        if(t instanceof ChangeLoggable loggable) {
            var prevLogId = changeLog.getPrevLogID();
            int r = updateChangeLogID(t.getId(),prevLogId,getDefaultCachePolicy(Tenancy.parseTenantID(user.getUserID())));
            if(r == 0) throw operationFailed("undo", t, new RuntimeException("Entity Not found when reverting to previous change log id."));
            loggable.setChangeLogID(prevLogId);
        }

        //删除或者标识为已撤消
        int r = deleteChangeLog
                ? changeLogRepository.delete(changeLogID)
                : changeLogRepository.updateUndoneById(changeLogID);
        if(r == 0) throw operationFailed("undo", t, new RuntimeException("Failed to undo change log."));
        return true;
    }


    public String getServiceName(String objName){
        var serviceName = NamingUtil.firstLetterLower(objName) + "Service";
        return serviceName;
    }

    //endregion of undo & redo
    public List<Long> getDescendantDivisionDepartmentIDs(long pid) {
        return new ArrayList<>();
    }

    //校验参数
    @Autowired
    private Validator validator;
    public List<ValidationError> validateEntity(T t){
        MetaObject metaObject=getMetaObject();
        List<ValidationError> validationErrors=new ArrayList<>();
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (violations!=null && !violations.isEmpty()){
            for (ConstraintViolation<T> violation : violations) {
                String colName=violation.getPropertyPath().toString();
                logger.error("校验失败"+ LocaleContextHolder.getLocale().toString()+colName+"-"+violation.getMessage());
                if (metaObject.hasCol(colName)) {
                    validationErrors.add(ValidationError.valueOf(colName,violation.getMessage()));
                }else{
                    if(metaObject.hasRelations()){
                        String[] results=formatFieldName(colName);
                        if (results!=null) {
                            validationErrors.add(ValidationError.valueOf(results[0]+"/"+results[1]+"/"+results[2],violation.getMessage()));
                        }
                    }
                }
            }
        }
        return validationErrors;

    }
    private static String[] formatFieldName(String fieldName) {
        if (fieldName.contains("[") && fieldName.contains("]")) {
            String prefix = fieldName.substring(0, fieldName.indexOf('['));
            String index = fieldName.substring(fieldName.indexOf('[') + 1, fieldName.indexOf(']'));
            String actualFieldName = fieldName.substring(fieldName.indexOf(']') + 1).replace(".", "");
            return new String[]{prefix ,index+"" ,actualFieldName};
        }
        return null;
    }
    //region 导入
    @Transactional
    public T importOne(File file, Long templateId,boolean enableSave, UserAccount user, CachePolicy p, Map<String, String> param) {
        List<T> importedList = importExcel(p.getTenantID(),file, templateId);
        if (!BaseUtil.hasAny(importedList)) {
            throw new OperationFailedException("import.failed.not.data");
        }
        T t =importedList.get(0);
       //导入留给子类重写
        applyConsumerIfNonNull(t, beforeImportValidate(param));
        
        handleImportEntity(t,true,p,user);
        //导入
        applyConsumerIfNonNull(t, outImport());

        t=assembleSingle(t);

        if (enableSave)  save(t, p, true);

        return t;
    }
    protected T handleImportEntity(T t, boolean checkExists,  CachePolicy p, UserAccount user) {
        t.setEntityState(EntityState.CREATED);
        if (checkExists) {
            T oldT = null;
            //如果有唯一键，自动校验
            if (repository.hasUniqueKeyCol()) {
                String uniqueNo = null;
                var uniqueKeyObj = repository.getUniqueKeyValue(t);
                if (uniqueKeyObj != null) uniqueNo = (String) uniqueKeyObj;
                if (StringUtils.hasText(uniqueNo)) {
                    try {
                        oldT = assemble(getByNo(uniqueNo, p));
                    } catch (Exception e) {
                        logger.error("getByNo.not.found", e);
                    }
                }
            } else {
                if (exists(t.getId(), p)) {
                    oldT = load(t.getId(), p);
                }
            }
            if (oldT != null) {
                handleSubEntities(t, oldT);
                // 选择需要复制的字段
                List<String> ignoreProperties = getIgnoreProperties();
                for (String ignoreProperty : ignoreProperties){
                    repository.setPropertyValue(t, ignoreProperty,repository.getPropertyValue(oldT,ignoreProperty));
                }
                t.setEntityState(EntityState.MODIFIED);
            }
        }

        if (t instanceof Auditable auditable) {
            // 根据存在与否来设置创建或修改操作
            if (t.isModified()) {
                auditable.modifyBy(user.getUserID());
            } else {
                auditable.createBy(user.getUserID(), user.getDeptID());
            }
        }
        if (t instanceof  Computable){
            ((Computable) t).compute();
        }
        return t;

    }
    /**
     * 获取需要忽略的属性字段列表，默认包括主键字段和一些固定字段。
     * 子类可以根据需要重写此方法来定制。
     *
     * @return 返回忽略的属性字段数组
     */
    protected List<String> getIgnoreProperties() {
        Set<String> ignoreProperties = getMetaObject().getKeyCols().stream()
                .map(MetaCol::getColName)
                .collect(Collectors.toSet());

        // 添加固定字段
        ignoreProperties.add("creatorID");
        ignoreProperties.add("deptID");
        ignoreProperties.add("createDate");

        // 添加只读字段
        getMetaObject().getCols().forEach(col -> {
            if (col.isReadOnly()) ignoreProperties.add(col.getColName());
        });

        return new ArrayList<>(ignoreProperties);
    }

    protected void handleSubEntities(T newT, T oldT) {
        if (getMetaObject().hasRelations()) {
            Object partitionKeyValue =null;
            if(BaseUtil.hasText(getMetaObject().getPartitionKey())){
                 partitionKeyValue =repository.getPropertyValue(oldT,getMetaObject().getPartitionKey());
            }
            for (var relation : getMetaObject().getRelations()) {
                if (relation.getRelationType() != MetaRelationType.HAS_MANY) continue;

                var newSubList = (List<Entity>) repository.getRelativeValue(newT, relation);
                if (!BaseUtil.hasAny(newSubList)) continue;

                var existingSubList = (List<Entity>) repository.getRelativeValue(oldT, relation);
                if (!BaseUtil.hasAny(existingSubList)) {
                    repository.setRelativeValue(newT, relation, newSubList);
                } else {
                    MetaObject relObj = getMetaObject(relation.getRelativeDbSchema() == null ? relation.getDbSchema() : relation.getRelativeDbSchema(), relation.getRelativeObjName());
                    var repositoryName= NamingUtil.firstLetterLower(relObj.getObjName()) + "Repository";
                    var subRepository = ApplicationContextProvider.getApplicationContext().getBean(repositoryName, EntityRepository.class);
                    for (Entity newSubEntity : newSubList) {
                        if (partitionKeyValue!=null){
                            subRepository.setPropertyValue(newSubEntity,relObj.getPartitionKey(),partitionKeyValue);
                        }
                        String newKey = relObj.getKeyCols().stream().map(keyCol -> subRepository.getColValue(newSubEntity,keyCol).toString()).collect(Collectors.joining(","));
                        Optional<Entity> existingSubEntity = existingSubList.stream()
                                .filter(sub -> {
                                    String oldKey = relObj.getKeyCols().stream().map(keyCol -> subRepository.getColValue(sub,keyCol).toString()).collect(Collectors.joining(","));
                                    return oldKey.equals(newKey);
                                })
                                .findFirst();
                        if (existingSubEntity.isPresent()) {
                            // 如果子表存在，覆盖子表的数据
                            Entity existingEntity = existingSubEntity.get();
                            BeanUtils.copyProperties(newSubEntity, existingEntity);
                            existingEntity.setEntityState(EntityState.MODIFIED);
                        } else {
                            newSubEntity.setEntityState(EntityState.CREATED);
                            // 如果子表不存在，添加到子表列表
                            existingSubList.add(newSubEntity);
                        }
                    }
                    repository.setRelativeValue(newT, relation, existingSubList);
                }
            }
        }
    }
    /**
     * 导入进行数据处理
     * @return
     */
    protected Consumer<T> beforeImportValidate(Map<String, String> param){
        return null;
    }
    /**
     * 导入后保存数据库之前进行数据处理
     * @return
     */
    protected Consumer<T> outImport(){
        return null;
    }

    @Transactional
    public ImportResult importExcelFile(File file, Long templateId, boolean checkExists, boolean ignoreError,boolean enableSave, CachePolicy cachePolicy, UserAccount user, Map<String, String> param) {
        List<T> importedList = importExcel(cachePolicy.getTenantID(),file, templateId);
        if (!BaseUtil.hasAny(importedList)) {
            throw new OperationFailedException("import.failed.not.data");
        }

        ImportResult result = new ImportResult();
        List<FileImportErrorInfo> errors = new ArrayList<>();
        List<T> datas = new ArrayList<>();

        for (T t : importedList) {
            //导入留给子类重写
            applyConsumerIfNonNull(t, beforeImportValidate(param));

            List<ValidationError> validationErrors = validateEntity(t);
            if (!validationErrors.isEmpty()) {
                result.incrementFailedCount();
                addImportError(errors, t, validationErrors);
                if (!ignoreError) return result.withErrors(errors);
                continue;
            }
            try {
                //导入数据处理
               handleImportEntity(t, checkExists, cachePolicy, user);

                //导入留给子类重写
                applyConsumerIfNonNull(t, outImport());

                t=assembleSingle(t);

                if (enableSave) result.incrementSuccessCount(save(t, cachePolicy, false));

                datas.add(t);

            } catch (Exception e) {
                logger.error(String.format("Import failed: [%s] %s", file.getName(), e.getMessage()), e);
                result.incrementFailedCount();
                handleImportException(errors, t, e);
                if (!ignoreError) return result.withErrors(errors);
            }
        }
        result.setDatas(datas);
        return result.withErrors(errors);
    }

    private void addImportError(List<FileImportErrorInfo> errors, T entity, List<ValidationError> validationErrors) {
        String errorMsg = validationErrors.stream()
                .map(error ->{
                    if (error.getField().contains("/")){
                         var results=error.getField().split("/");
                         var rel= getMetaObject().getRelation(results[0]);
                         var relObj=getMetaObject(rel.getRelativeDbSchema()==null?rel.getDbSchema():rel.getRelativeDbSchema(),rel.getRelativeObjName());
                         return i18n.getLocalizedMessage("import.row.col.error",relObj.getDisplayLabel(),
                                 ((List<AbstractEntity>) repository.getRelativeValue(entity, rel)).get(Integer.parseInt(results[1])).getCustomProperty(ExcelConstant.EXCEL_ROW_NUM),
                                 relObj.getCol(results[2]).getDisplayLabel(),error.getError()).getMessage();
                    }
                    return getMetaObject().getCol(error.getField()).getDisplayLabel() + ":" + error.getError();
                })
                .limit(3)
                .collect(Collectors.joining(";"));
        errors.add(new FileImportErrorInfo(getMetaObject().getDisplayLabel(), (int) entity.getRowNum(),
                i18n.getLocalizedMessage("row.col.error", getMetaObject().getDisplayLabel(),
                        ((AbstractEntity)entity).getCustomProperty(ExcelConstant.EXCEL_ROW_NUM), errorMsg).getMessage(), validationErrors));
    }

    private void handleImportException( List<FileImportErrorInfo> errors, T entity, Exception e) {
        List<ValidationError> validationErrors = new ArrayList<>();
        MetaCol errorFieldCol = getMetaObject().getCol(getMetaObject().getNameCol() != null ? getMetaObject().getNameCol() : getMetaObject().getUniqueKey());

        if (e instanceof OperationFailedException && e.getCause() instanceof DuplicateKeyException) {
            validationErrors.add(ValidationError.valueOf(errorFieldCol.getColName(), i18n.getLocalizedMessage("already.exist", getMetaObject().getDisplayLabel()).getMessage()));
        } else if (e instanceof DataInvalidException ex) {
            validationErrors.addAll(ex.getValidationErrors());
        } else if (e instanceof DomainException ex) {
            validationErrors.add(ValidationError.valueOf(errorFieldCol.getColName(), ex.getLocalizedMessage()));
        } else {
            validationErrors.add(ValidationError.valueOf(errorFieldCol.getColName(), i18n.getLocalizedMessage("saveFile.fail", getMetaObject().getDisplayLabel()).getMessage()));
        }
        addImportError(errors,entity, validationErrors);
    }
    //endregion

    public DomainActionParam prepareAction(final T t, final String actionName, final UserAccount user) {
        DomainActionParam param = new DomainActionParam(actionName);
        var opAction = getDomainActions().stream()
                .filter(a->a.getName().equals(actionName))
                .findFirst();

        if(!opAction.isPresent()) throw new OperationNotAllowException(actionName+".not.found");

        var action = opAction.get();
        var moduleAction=action.getMetadata(LocaleContextHolder.getLocale());
        var metaObj=getMetaObject();

        MetaCol labelCol=repository.hasUniqueKeyCol()
                ?repository.getUniqueKeyCol()
                : (StringUtils.hasText(metaObj.getNameCol())?metaObj.getCol(metaObj.getNameCol()):null);
        // 生成通知消息
        String notification = String.format("%s%s %s%s",
                user.getUsername(),
                moduleAction.getLabel(),
                getMetaObject().getDisplayLabel(),
                labelCol==null ? "":"【"+repository.getMetaObjectAccess().getProperty(t, labelCol)+"】"
        );
        param.setNotification(notification);

        // 准备用户列表
        action.prepareActionUsers(t, param, user);

        return param;
    }
    public File exportTemplate(long templateId,CachePolicy p){
        var refDatas=assembleRefData(p);
        return exportExcelTemplate(templateId,refDatas);
    }

    /**
     * 组装引用数据
     *
     * @return 每个关系及其对应的引用数据列表
     */
    protected Map<MetaRelation, List<Object>> assembleRefData(CachePolicy p) {
        return null;
    }

    public List<ChangeWrapper> readChangeLog(long logID,CachePolicy p) {
        ChangeLog changeLog = changeLogRepository.find(logID);
        var difference = (LinkedHashMap<String, Object>) changeLog.readDifference();
        if (CollectionUtils.isEmpty(difference)) {
            throw new OperationFailedException("empty.change.log");
        }
        List<ChangeWrapper> changeWrappers = new ArrayList<>();
        for (var entry : difference.entrySet()) {
            var objName = entry.getKey();
            var serviceName = getServiceName(objName);
            var service = ApplicationContextProvider.getApplicationContext().getBean(serviceName, DomainService.class);
            Object objChange = entry.getValue();
            if (objChange instanceof ChangeLog.ChangeData<?> change) {
                var wrapper = buildChangeWrapper(change, service);
                changeWrappers.add(wrapper);
            } else if (objChange instanceof List<?> changeList) {
                for (var c : changeList.reversed()) {
                    if (c instanceof ChangeLog.ChangeData<?> changeData) {
                        var changeWrapper = buildChangeWrapper(changeData, service);
                        changeWrappers.add(changeWrapper);
                    } else throw new OperationFailedException("invalid.change.log");
                }
            }
        }

        return changeWrappers;
    }

    private ChangeWrapper buildChangeWrapper(ChangeLog.ChangeData<?> change, DomainService service) {
        String objName=service.getMetaObject().getObjName();
        var wrapper = new ChangeWrapper(objName, change);
        var newObj = wrapper.getNewValue();
        var oldObj = wrapper.getOldValue();
        try {
            String repositoryName = NamingUtil.firstLetterLower(objName) + "Repository";
            EntityRepository repository = ApplicationContextProvider.getApplicationContext().getBean(repositoryName, EntityRepository.class);
            if (wrapper.getChangeType() == ChangeType.CHANGED && wrapper.getNewValue() == null) {
                 newObj = repository.getTClass().newInstance();
                if (oldObj != null) {
                    BeanUtils.copyProperties(oldObj, newObj,"assemblyState");
                }
                for (FieldChange fieldChange : wrapper.getFieldChanges()) {
                    repository.getMetaObjectAccess().setProperty(newObj, fieldChange.getFieldName(), fieldChange.getNewValue());
//                    ClassUtils.setFieldValue(newObj, fieldChange.getFieldName(), fieldChange.getNewValue());
                }
            }
            if (!Objects.isNull(newObj)) service.assemble(newObj);
            if (!Objects.isNull(oldObj)) service.assemble(oldObj);

//            assembleFieldChangeProperty(newObj, oldObj, wrapper.getFieldChanges(),repository);

            wrapper.setOldValue(oldObj);
            wrapper.setNewValue(newObj);

        } catch (Exception e) {
            logger.error(String.format("Error assembling object for changeWrapper: %s", objName), e);
        }

        return wrapper;
    }

//    private void assembleFieldChangeProperty(Object newEntity, Object oldEntity, List<FieldChange> fieldChanges, EntityRepository repository) {
//        if (CollectionUtils.isEmpty(fieldChanges)) return;
//
//        for (var fieldChange : fieldChanges) {
//            try {
//                var col=repository.getMetaObject().getCol(fieldChange.getFieldName());
//                if (col!=null && col.getRelationType()!=MetaRelation.NONE) {
//                    if (newEntity != null) {
//                        Object colText = repository.getColText((Entity) newEntity, col);
//                        if (colText != null) {
//                            fieldChange.setRefProperty(FieldChange.Meta._newValue, colText.toString());
//                        }
//                    }
//                    if (oldEntity != null) {
//                        Object colText = repository.getColText((Entity) oldEntity, col);
//                        if (colText != null) {
//                            fieldChange.setRefProperty(FieldChange.Meta._oldValue, colText.toString());
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                logger.error(String.format("assembleFieldChangeProperty failed for field: %s", fieldChange.getFieldName()), e);
//            }
//        }
//    }


    public PagedList<ChangeLog> searchWordChangeLogs(String refKey, String searchWord, Paginator paginator){
        return BaseUtil.hasText(searchWord)
                ? changeLogRepository.searchAllByRef(tClassName, refKey, searchWord, paginator)
                : changeLogRepository.findAllByRef(tClassName, refKey, paginator);
    }

    public List<ChangeLog> getChangeLogs(String  k) {
        return changeLogRepository.findAllByRef(tClassName, k);
    }

    public BackgroundTask addBackgroundTask(UserAccount user, EntityExport entityExport, CachePolicy p){

       return backgroundTaskService.save(user, entityExport, p);
    }
}
