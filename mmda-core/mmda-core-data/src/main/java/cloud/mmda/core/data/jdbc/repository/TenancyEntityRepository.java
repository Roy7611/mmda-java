package cloud.mmda.core.data.jdbc.repository;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.data.jdbc.mappers.RowMapperPagedResultSetExtractor;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.data.sql.SqlOperator;
import cloud.mmda.core.data.sql.SqlQuery;
import cloud.mmda.core.entities.*;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.data.pagination.*;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.data.exceptions.TenancyDataAccessException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.redis.core.StringRedisTemplate;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import jakarta.validation.constraints.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 多租户数据仓储
 *
 * @author roshion 2020.3.28
 *
 * @param <T>
 */
public abstract class TenancyEntityRepository<T extends TenancyEntity<K>,K>
        extends EntityRepository<T,K>  //只支持K为bigint的实体
        implements TenancyRepository<T,K> {

    private static final Log logger = LogFactory.getLog(TenancyEntityRepository.class);

    /**
     * 构造函数
     * @param ds 数据源
     */
    public TenancyEntityRepository(DataSource ds) {
        super(ds);
    }

    private StringRedisTemplate idRedisTemplate;
    @Autowired
    public final void setIdRedisTemplate(StringRedisTemplate idRedisTemplate) {
        this.idRedisTemplate = idRedisTemplate;
    }
    private String idCacheKey;

    private String partitionKey;
    private MetaCol partitionCol;

//    private String uniqueKey;
//    private ConcurrentHashMap<Short,SqlExpression> cachedPartitionConds;//多租户分区查询表达式缓存

    /**
     * 初始化函数
     */
    @Override
    @PostConstruct
    protected void initialize() {
        super.initialize();

        partitionKey = metaObj.getPartitionKey();
        Objects.requireNonNull(partitionKey,tClassName + "元数据未定义partitionKey");
        partitionCol = metaObj.getCol(partitionKey);
        Objects.requireNonNull(partitionCol,tClassName + "元数据定义的partitionKey不在字段列表中");

        idCacheKey = "IDs:"+metaObj.getFullName().replace('.',':');
    }


    /**
     * 获取某个租户当前实体最小id
     *
     * 可考虑缓存优化
     * @param tenantID
     * @return
     */
    public long getMinEntityID(int tenantID){
        return Tenancy.buildEntityID(tenantID, BaseUtil.getValueOrZero(metaObj.getMinID()));
    }

    /**
     * 获取某个租户当前实体最大id
     *
     * 可考虑缓存优化
     * @param tenantID
     * @return
     */
    public long getMaxEntityID(int tenantID){
        return Tenancy.buildEntityID(tenantID, BaseUtil.getValueOrDefault(metaObj.getMaxID(),Tenancy.MAX_REAL_ID));
    }

    private Object[] addPartitionArgs(int tenantID,Object...args){
        Object[] tArgs = new Object[args.length+2];
        tArgs[0] = getMinEntityID(tenantID);
        tArgs[1] = getMaxEntityID(tenantID);
        if(args.length>0) System.arraycopy(args,0,tArgs,2,args.length);
        return tArgs;
    }

    /**
     * 构建多租户分区查询表达式
     *
     * @param tenantID 租户id
     * @return
     */
    @Override
    public SqlExpression buildPartitionExpression(int tenantID){
        return expressionBuilder()
                .exp(partitionKey)
                .between(getMinEntityID(tenantID),getMaxEntityID(tenantID))
                .result();
    }

    @Override
    public String getPartitionKeyCondition(int tenantID, boolean finalized){
        if(finalized){
            StringBuilder sb = new StringBuilder();
            return sb.append("t.").append(partitionKey)
                    .append(SqlOperator.BETWEEN.getOp())
                    .append(getMinEntityID(tenantID))
                    .append(SqlOperator.AND.getOp())
                    .append(getMaxEntityID(tenantID))
                    .toString();
        }
        return buildPartitionExpression(tenantID)
                .buildExpression(c->"t."+c.getColName(),metadataProvider.getParamPlaceholder());
    }


    @Override
    public SqlExpression buildExpression(int tenantID, Map<String, Object> attributes){
        return addPartitionCondition(tenantID, buildExpression(attributes));
    }

    @Override
    public long newPartitionID(int tenantID){
        //Long autoIncrID = idRedisTemplate.opsForHash().increment(idCacheKey,String.valueOf(tenantID), 1L);
        //改为不用hash
        Long autoIncrID = idRedisTemplate.opsForValue().increment(idCacheKey+':'+String.valueOf(tenantID), 1L);
        long newID = Tenancy.buildEntityID(tenantID,autoIncrID);
        if(metaObj.getMinID()!=null && autoIncrID<metaObj.getMinID()) newID+=metaObj.getMinID();
        return newID;
    }

    @Override
    public long newPartitionIDs(int tenantID, long count){
        Long autoIncrID = idRedisTemplate.opsForValue().increment(idCacheKey+':'+String.valueOf(tenantID), count);
        long newID = Tenancy.buildEntityID(tenantID,autoIncrID);
        if(metaObj.getMinID()!=null && autoIncrID<metaObj.getMinID()) newID+=metaObj.getMinID();
        return newID;
    }

    @Override
    public long renewPartitionID(int tenantID, long duplicatedKey){
        MetaCol partitionKeyCol = metaObj.getCol(partitionKey);
        SqlQuery findQuery = SqlQuery.builder(metadataProvider)
                .from(metaObj)
                .select(partitionKeyCol).withoutRowNum()
                .build();
        Paginator paginator = new Paginator(1,1, Sort.of(partitionKey,Sort.Order.DESC));
        String sql = findQuery.executor().select(tenantID, paginator,metadataProvider,jdbcTemplate);
        long newID = jdbcTemplate.queryForObject(sql,Long.class);
        long delta = newID - duplicatedKey + 1;
        Long autoIncrID = idRedisTemplate.opsForValue().increment(idCacheKey+':'+String.valueOf(tenantID), delta);
        newID = Tenancy.buildEntityID(tenantID,autoIncrID);
        return newID;
    }

    public void setDefaultUniqueKey(T t){
        t.setDefaultUniqueKey();
    }
    @Override
    protected void beforeInsert(T t){
        //调用父类方法
        super.beforeInsert(t);

        //自动处理多租户id生成
        if(Tenancy.getRealID(t.getPartitionID())==0){
            long partitionID = newPartitionID(t.getTenantID());
            t.setPartitionID(partitionID);
        }
    }


    private final boolean enableNamedPartition(){
        return metaObj.isPartitioned() && metadataProvider.supportNamedPartition();
    }

    @Override
    public T find(int tenantID, K k) throws DataAccessException{
        ensureMatchedTenantID(tenantID, k);
        if(enableNamedPartition()){
            SqlQuery findQuery = getFindByKeyQuery(k);
            String sql = findQuery.getTenancySql(tenantID,metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.queryForObject(sql, toKeyArgArray(k), rowMapper);
        }
        return super.find(k);
    }

    @Override
    public T findByUniqueKey(int tenantID, String uniqueKey) throws DataAccessException{
        if(uniqueKeyCol == null) throw new InvalidDataAccessResourceUsageException("没有定义唯一索引列");
        SqlQuery sqlQuery = getFindByUniqueKeyQuery();
        String sql = null;
        if(enableNamedPartition()){
            //如果启用了表分区且支持指定命名分区
            sql = sqlQuery.getTenancySqlLimit(tenantID, 1, metadataProvider);//会解决SLOT_PARTITION
        }
        else {
            //否则使用限制partitionKey BETWEEN minID AND maxID
            sql = sqlQuery.getSql() + SqlExpression.AND + getPartitionKeyCondition(tenantID,true);
        }
        if(logger.isDebugEnabled()) logger.debug(sql);
        return jdbcTemplate.queryForObject(sql, rowMapper, uniqueKey);
    }
    /**
     * 查找第一个符合条件condition等式集合的T实体对象
     * @param tenantID 租户id
     * @param condition 等式集合，可被转化为SqlExpression
     * @return T实体对象或者null
     */
    @Override
    public T findFirst(int tenantID, SqlExpression condition) throws DataAccessException{
        if(enableNamedPartition()){
            //如果启用了表分区且支持指定命名分区
            SqlQuery sqlQuery = getFindFirstQuery(condition);
            String sql = sqlQuery.getTenancySqlLimit(tenantID, 1, metadataProvider);//会解决SLOT_PARTITION
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.queryForObject(sql, condition.getArgValues().toArray(), rowMapper);
        }
        else{
            //否则使用限制partitionKey BETWEEN minID AND maxID
            //此时肯定没有SLOT_PARTITION，可以调用基类的方法
            return super.findFirst(addPartitionCondition(tenantID, condition));
        }
    }
    @Override
    public T findFirst(int tenantID, SqlExpression condition, Sort sort) throws DataAccessException{
        Assert.notNull(sort, "sort must not be null");
        if(enableNamedPartition()){
            //如果启用了表分区且支持指定命名分区
            SqlQuery sqlQuery = getFindFirstQuery(condition);
            String sql = sqlQuery.getTenancySqlLimit(tenantID, 1, sort.toString(), metadataProvider);//会解决SLOT_PARTITION
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.queryForObject(sql, condition.getArgValues().toArray(), rowMapper);
        }
        else{
            //否则使用限制partitionKey BETWEEN minID AND maxID
            //此时肯定没有SLOT_PARTITION，可以调用基类的方法
            return super.findFirst(addPartitionCondition(tenantID, condition));
        }
    }
    @Override
    public T findFirst(int tenantID, String condition, Object...args) throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery findAllQuery = getFindAllQuery();
            String sql = findAllQuery.getTenancySqlWithLimit(tenantID, condition,1, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.queryForObject(sql, args, rowMapper);
        }
        return super.findFirst(addPartitionCondition(tenantID, condition),addPartitionArgs(tenantID,args));
    }
    @Override
    public T findFirst(int tenantID, String condition, Sort sort, Object...args) throws DataAccessException{
        Assert.notNull(sort, "sort must not be null");
        if(enableNamedPartition()){
            SqlQuery findAllQuery = getFindAllQuery();
            String sql = findAllQuery.getTenancySqlWithLimit(tenantID, condition,1, sort.toString(), metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.queryForObject(sql, args, rowMapper);
        }
        return super.findFirst(addPartitionCondition(tenantID, condition),addPartitionArgs(tenantID,args));
    }
    @Override
    public <U> U findScalar(int tenantID, String colName, SqlExpression condition, Class<U> requiredType) throws DataAccessException {
        if(enableNamedPartition()){
            SqlQuery findQuery = getFindScalarQuery(colName, condition);
            String sql = findQuery.getTenancySqlLimit(tenantID, 1, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.queryForObject(sql, condition.getArgValues().toArray(), requiredType);
        }
        return super.findScalar(colName,addPartitionCondition(tenantID,condition),requiredType);
    }


    @Override
    public List<T> findAll(int tenantID) throws DataAccessException {
        if(enableNamedPartition()){
            SqlQuery findAllQuery = getFindAllQuery();
            String sql = findAllQuery.getTenancySql(tenantID, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.query(sql, rowMapper);
        }
        return super.findAllBy(buildPartitionExpression(tenantID));
    }


    @Override
    public List<T> findAll(int tenantID, Sort sort) throws DataAccessException {
        if(enableNamedPartition()){
            SqlQuery findAllQuery = getFindAllQuery();
            String sql = findAllQuery.getTenancySqlWith(tenantID, sort, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.query(sql, rowMapper);
        }
        return super.findAllBy(buildPartitionExpression(tenantID),sort);
    }

    @Override
    public PagedList<T> findAll(int tenantID, Paginator paginator) throws DataAccessException {
        if(enableNamedPartition()){
            int rc = count(tenantID);
            paginator.setRecordCount(rc);
            SqlQuery findAllQuery = getFindAllQuery();
            String sql = findAllQuery.getTenancySqlWith(tenantID, paginator, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            List<T> data = jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<>(rowMapper, paginator));
            return new PagedList<T>(data, paginator);
        }
        return super.findAllBy(paginator,buildPartitionExpression(tenantID));

    }


    @Override
    public List<T> findAllIn(int tenantID,String field,String list,boolean ordered) throws DataAccessException{
        SqlQuery findAllQuery = getFindAllQuery();
        String alias = findAllQuery.getAliasName(metaObj.getObjName());
        String fieldName = (alias!=null ? alias+"."+field : field);
        String condition = fieldName + " IN(" + list +")";
        boolean namedPartition = enableNamedPartition();
        if(!namedPartition){
            //不指定命名分区，则需要加ID BETWEEN
            condition = addPartitionCondition(tenantID, condition);
        }
        if(ordered && metadataProvider.supportOrderByField()) condition += " ORDER BY FIELD("+fieldName+"," + list +")";//MySql Only
        String sql = findAllQuery.getTenancySqlWith(tenantID,condition, metadataProvider);
        if(logger.isDebugEnabled()) logger.debug(sql);
        return namedPartition
                ? jdbcTemplate.query(sql,rowMapper)
                : jdbcTemplate.query(sql,rowMapper,getMinEntityID(tenantID),getMaxEntityID(tenantID));
    }
    @Override
    public List<T> findAllIn(int tenantID,final String field,final String list,boolean ordered, final String condition, Object... args) throws DataAccessException{
        Objects.requireNonNull(condition);
        Objects.requireNonNull(args);

        SqlQuery findAllQuery = getFindAllQuery();
        String alias = findAllQuery.getAliasName(metaObj.getObjName());
        String fieldName = (alias!=null ? alias+"."+field : field);
        String cond = SqlExpression.and(condition, fieldName + " IN(" + list +")");
        boolean namedPartition = enableNamedPartition();
        if(!namedPartition){
            //不指定命名分区，则需要加ID BETWEEN
            cond = addPartitionCondition(tenantID, cond);
        }
        if(ordered && metadataProvider.supportOrderByField()) cond += " ORDER BY FIELD("+fieldName+"," + list +")";//MySql Only
        String sql = findAllQuery.getTenancySqlWith(tenantID,cond, metadataProvider);
        if(logger.isDebugEnabled()) logger.debug(sql);
        if(namedPartition){
            return jdbcTemplate.query(sql,rowMapper,args);
        }
        else{
            List<Object> queryArgs = Arrays.asList(args);
            queryArgs.add(0,getMinEntityID(tenantID));
            queryArgs.add(1,getMaxEntityID(tenantID));
            return jdbcTemplate.query(sql, queryArgs.toArray(), rowMapper);
        }
    }

    @Override
    public PagedList<T> findAllNotIn(int tenantID, final Paginator paginator, final String field, final String list) throws DataAccessException{
        SqlQuery findAllQuery = getFindAllQuery();
        String alias = findAllQuery.getAliasName(metaObj.getObjName());
        String fieldName = (alias!=null ? alias+"."+field : field);
        String condition = fieldName + " NOT IN(" + list +")";

        int rc = count(tenantID,condition);
        paginator.setRecordCount(rc);

        boolean namedPartition = enableNamedPartition();
        if(!namedPartition){
            condition = addPartitionCondition(tenantID, condition);
        }

        String sql = findAllQuery.getTenancySqlWith(tenantID, condition, paginator, metadataProvider);
        if(logger.isDebugEnabled()) logger.debug(sql);
        RowMapperPagedResultSetExtractor<T> resultSetExtractor = new RowMapperPagedResultSetExtractor<>(rowMapper, paginator);
        List<T> data = namedPartition
                ? jdbcTemplate.query(sql,resultSetExtractor)
                : jdbcTemplate.query(sql, resultSetExtractor,getMinEntityID(tenantID),getMaxEntityID(tenantID));
        return new PagedList<T>(data, paginator);
    }
    @Override
    public PagedList<T> findAllNotIn(int tenantID, final Paginator paginator, final String field, final String list, final String condition, Object... args) throws DataAccessException{
        SqlQuery findAllQuery = getFindAllQuery();
        String alias = findAllQuery.getAliasName(metaObj.getObjName());
        String fieldName = (alias!=null ? alias+"."+field : field);
        String cond = SqlExpression.and(condition, fieldName + " NOT IN(" + list +")");

        int rc = count(tenantID, cond, args);
        paginator.setRecordCount(rc);

        boolean namedPartition = enableNamedPartition();
        if(!namedPartition){
            cond = addPartitionCondition(tenantID, cond);
        }

        String sql = findAllQuery.getTenancySqlWith(tenantID, cond, paginator, metadataProvider);
        if(logger.isDebugEnabled()) logger.debug(sql);
        RowMapperPagedResultSetExtractor<T> resultSetExtractor = new RowMapperPagedResultSetExtractor<>(rowMapper, paginator);
        if(namedPartition){
            List<T> data = jdbcTemplate.query(sql,resultSetExtractor, args);
            return new PagedList<T>(data, paginator);
        }
        else {
            List<Object> queryArgs = Arrays.asList(args);
            queryArgs.add(0,getMinEntityID(tenantID));
            queryArgs.add(1,getMaxEntityID(tenantID));
            List<T> data = jdbcTemplate.query(sql, queryArgs.toArray(), resultSetExtractor);
            return new PagedList<T>(data, paginator);
        }
    }
    @Override
    public List<T> findAllBy(int tenantID, SqlExpression condition) throws DataAccessException {
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getFindAllByQuery(condition);
            String sql = sqlQuery.getTenancySql(tenantID,metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.query(sql, condition.getArgValues().toArray(), rowMapper);
        }
        return super.findAllBy(addPartitionCondition(tenantID, condition));
    }
    @Override
    public List<T> findAllBy(int tenantID, SqlExpression condition, @NotNull Sort sort) throws DataAccessException {
        Objects.requireNonNull(sort, "sort cannot be null");

        if(enableNamedPartition()){
            SqlQuery sqlQuery = getFindAllByQuery(condition);
            String sql = sqlQuery.getTenancySqlWith(tenantID, sort, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.query(sql, rowMapper, condition.getArgValues().toArray());
        }
        return super.findAllBy(addPartitionCondition(tenantID,condition),sort);
    }
    @Override
    public List<T> findAllBy(int tenantID, String condition, Object...args) throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getFindAllQuery();
            String sql = sqlQuery.getTenancySqlWith(tenantID, condition, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.query(sql, rowMapper, args);
        }
        return super.findAllBy(addPartitionCondition(tenantID, condition),addPartitionArgs(tenantID,args));
    }
    @Override
    public List<T> findAllBy(int tenantID, String condition, Sort sort, Object...args) throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getFindAllQuery();
            String sql = sqlQuery.getTenancySqlWith(tenantID, condition, sort, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.query(sql, rowMapper, args);
        }

        return super.findAllBy(addPartitionCondition(tenantID, condition),sort,addPartitionArgs(tenantID,args));
    }

    @Override
    public PagedList<T> findAllBy(int tenantID, Paginator paginator, SqlExpression condition) throws DataAccessException {
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getFindAllByQuery(condition);

            int rc = count(tenantID, condition);
            paginator.setRecordCount(rc);

            String sql = sqlQuery.getTenancySqlWith(tenantID, paginator, metadataProvider);
            Object[] argValueArray = condition.getArgValues().toArray();
            if(logger.isDebugEnabled()) logger.debug(sql);
            List<T> data = jdbcTemplate.query(sql, argValueArray, new RowMapperPagedResultSetExtractor<>(rowMapper, paginator));
            return new PagedList<T>(data, paginator);
        }
        return super.findAllBy(paginator, addPartitionCondition(tenantID,condition));
    }

    @Override
    public PagedList<T> findAllBy(int tenantID, Paginator paginator, String condition, Object...args) throws DataAccessException {
        if(enableNamedPartition()){

            int rc = count(tenantID, condition, args);
            paginator.setRecordCount(rc);

            SqlQuery sqlQuery = getFindAllQuery();
            String sql = sqlQuery.getTenancySqlWith(tenantID, condition, paginator, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            List<T> data = jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<>(rowMapper, paginator), args);
            return new PagedList<T>(data, paginator);
        }

        return super.findAllBy(paginator, addPartitionCondition(tenantID, condition),addPartitionArgs(tenantID,args));
    }
    @Override
    public EntityFilter buildRefFilter(int tenantID, final MetaCol col){
        BaseUtil.requireNonNull(col,"col");

        String colName = col.getColName();
        if(col.getRelationType() != MetaRelationType.REF) throw new IllegalArgumentException(colName+"字段不是REF类型");
        EntityFilter filter = new EntityFilter(colName+FILTER, col.getDisplayLabel());

        MetaRelation relation = metaObj.getRelation(col.getRelationName());
        List<MetaEnumMember> enumItems = findAllRefMap(tenantID, relation);
        enumItems.stream().forEach((e)->{
            String cond = "t."+colName+"="+metadataProvider.quoteColValue(col,e.getValue());
            String text = e.getText();
            filter.addFilterCondition(cond,text);
        });

        if(col.isNullable()) filter.addFilterCondition(colName+SqlExpression.IS_NULL, NULL_LABEL);

        return filter;
    }
    @Override
    public List<MetaEnumMember> findAllRefMap(int tenantID, MetaRelation refRelation){
//        String sql = "SELECT "+refRelation.getValueColName()+","+refRelation.getLabelColName()+
//                " FROM "
//                + metadataProvider.getFrom(refRelation.getFullRelativeObjName());
        BaseUtil.requireNonNull(refRelation, "refRelation");
        String sql = SqlQuery.SELECT+refRelation.getValueColName() + "," + refRelation.getLabelColExp()
                + SqlQuery.FROM + metadataProvider.getFrom(refRelation.getFullRelativeObjName());

        refRelation.setSql(sql);
        String relativeDbName = refRelation.getRelativeDbSchema();
        if(relativeDbName==null) relativeDbName = refRelation.getDbSchema();
        MetaObject relativeMetaObj = getMetaObject(relativeDbName,refRelation.getRelativeObjName());
        if(tenantID>0 && BaseUtil.hasText(relativeMetaObj.getPartitionKey())){
            StringBuilder sb = new StringBuilder();
            String partitionCond = sb.append(relativeMetaObj.getPartitionKey())
                    .append(SqlOperator.BETWEEN.getOp())
                    .append(Tenancy.buildEntityID(tenantID, BaseUtil.getValueOrZero(relativeMetaObj.getMinID())))
                    .append(SqlOperator.AND.getOp())
                    .append(Tenancy.buildEntityID(tenantID, BaseUtil.getValueOrDefault(relativeMetaObj.getMaxID(),Tenancy.MAX_REAL_ID)))
                    .toString();
            if(BaseUtil.hasText(refRelation.getWhere())){
                sql+=SqlQuery.WHERE+ partitionCond + " AND ("+refRelation.getWhere()+")";
            }
            else{
                sql+=SqlQuery.WHERE+ partitionCond;
            }
        }
        else{
            if(BaseUtil.hasText(refRelation.getWhere())) sql += SqlQuery.WHERE+refRelation.getWhere();
        }

        //跨数据库访问视图，需要切换数据源
        if(!relativeDbName.equals(tDbName) && relativeMetaObj.getObjType().startsWith("V")){
            var dataSource = getDataSource(relativeDbName);
            var relJdbcTemplate = new JdbcTemplate(dataSource);
            return relJdbcTemplate.query(sql, new EnumItemResultSetExtractor());
        }
        return jdbcTemplate.query(sql, new EnumItemResultSetExtractor());
    }
    public List<Map<String, Object>> findAllRefItems(int tenantID, MetaRelation refRelation){
        String sql = "SELECT * FROM "
                + metadataProvider.getFrom(refRelation.getFullRelativeObjName());
        String relativeDbName = refRelation.getRelativeDbSchema();
        if(relativeDbName==null) relativeDbName = refRelation.getDbSchema();
        MetaObject relativeMetaObj = getMetaObject(relativeDbName,refRelation.getRelativeObjName());
        if(tenantID>0 && BaseUtil.hasText(relativeMetaObj.getPartitionKey())){
            StringBuilder sb = new StringBuilder();
            String partitionCond = sb.append(relativeMetaObj.getPartitionKey())
                    .append(SqlOperator.BETWEEN.getOp())
                    .append(Tenancy.buildEntityID(tenantID, BaseUtil.getValueOrZero(relativeMetaObj.getMinID())))
                    .append(SqlOperator.AND.getOp())
                    .append(Tenancy.buildEntityID(tenantID, BaseUtil.getValueOrDefault(relativeMetaObj.getMaxID(),Tenancy.MAX_REAL_ID)))
                    .toString();
//            if(BaseUtils.hasText(refRelation.getWhere())){
//                sql+=SqlQuery.WHERE+ partitionCond + " AND ("+refRelation.getWhere()+")";
//            }
//            else{
                sql+=SqlQuery.WHERE+ partitionCond;
//            }
        }
//        else{
//            if(BaseUtils.hasText(refRelation.getWhere())) sql += SqlQuery.WHERE+refRelation.getWhere();
//        }

        //跨数据库访问视图，需要切换数据源
        if(!relativeDbName.equals(tDbName) && relativeMetaObj.getObjType().startsWith("V")){
            var dataSource = getDataSource(relativeDbName);
            var relJdbcTemplate = new JdbcTemplate(dataSource);
            return relJdbcTemplate.queryForList(sql);
        }
        return jdbcTemplate.queryForList(sql);
    }
    /**
     * 全表模糊搜索
     * @param tenantID 租户id
     * @param paginator
     * @param word 关键字
     * @return
     * @throws DataAccessException
     */
    @Override
    public PagedList<T> searchAll(int tenantID, Paginator paginator, String word) throws DataAccessException {
        boolean onlyString = isNotNumber(word);
        SqlQuery sqlQuery = getSearchAllQuery(onlyString);
        String sql = sqlQuery.executor()
                .searchWord(word)
                .select(tenantID, paginator,metadataProvider,jdbcTemplate);

        if(logger.isDebugEnabled()) logger.debug(sql);
        List<T> data = jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<>(rowMapper, paginator));
        return new PagedList<T>(data, paginator);
    }
    @Override
    public PagedList<T> searchAll(int tenantID, Paginator paginator, String word, String condition, Object...args) throws DataAccessException {
        Objects.requireNonNull(condition);
        boolean onlyString = isNotNumber(word);
        SqlQuery sqlQuery = getSearchAllQuery(onlyString);
        String sql = sqlQuery.executor()
                .searchWord(word)
                .preAnd(condition)
                .select(tenantID, paginator,metadataProvider,jdbcTemplate);

        List<T> data = jdbcTemplate.query(sql, args, new RowMapperPagedResultSetExtractor<>(rowMapper, paginator));
        return new PagedList<T>(data, paginator);
    }
    @Override
    public List<T> searchAll(int tenantID, String word, Sort sort) throws DataAccessException {
        Objects.requireNonNull(sort);
        boolean onlyString = isNotNumber(word);
        SqlQuery sqlQuery = getSearchAllQuery(onlyString);
        String sql = sqlQuery.executor()
                .searchWord(word)
                .orderBy(sort)
                .select(tenantID,metadataProvider);

        return jdbcTemplate.query(sql, rowMapper);
    }
    @Override
    public List<T> searchAll(int tenantID, String word, Sort sort, String condition, Object...args) throws DataAccessException {
        Objects.requireNonNull(condition);
        boolean onlyString = isNotNumber(word);
        SqlQuery sqlQuery = getSearchAllQuery(onlyString);
        String sql = sqlQuery.executor()
                .searchWord(word)
                .preAnd(condition)
                .orderBy(sort)
                .select(tenantID,metadataProvider);

        return jdbcTemplate.query(sql, args, rowMapper);
    }
    @Override
    public int count(int tenantID)  throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getCountAllQuery();
            String sql = sqlQuery.getTenancySql(tenantID,metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return this.jdbcTemplate.queryForObject(sql, Integer.class);
        }
        return super.count(buildPartitionExpression(tenantID));
    }
    @Override
    public List<K> findAllKeysBy(int tenantID, String condition, Object...args) throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getFindAllKeysQuery();
            String sql = sqlQuery.getTenancySqlWith(tenantID,condition,metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            if(keyColNum>1)
                return jdbcTemplate.query(sql, args, keyMapper);
            return jdbcTemplate.queryForList(sql,args,kClass);
        }
        return super.findAllKeysBy(addPartitionCondition(tenantID, condition),addPartitionArgs(tenantID,args));
    }
    @Override
    public List<K> findAllKeysBy(int tenantID, SqlExpression condition) throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getFindAllKeysQuery();
            String where = SqlExpression.buildExpression(condition, col->sqlQuery.getAliasDotName(col), SqlQuery.QM);
            String sql = sqlQuery.getTenancySqlWith(tenantID,where,metadataProvider);
            condition.buildArgs( col->sqlQuery.getAliasDotName(col), SqlQuery.QM,false);
            Object[] args = condition.getArgValues().toArray();
            if(logger.isDebugEnabled()) logger.debug(sql);

            if(keyColNum>1){
                int[] argJdbcTypes = condition.getArgTypes().stream().mapToInt(metadataProvider::getJdbcType).toArray();
                return jdbcTemplate.query(sql, args, argJdbcTypes, keyMapper);
            }
            return jdbcTemplate.queryForList(sql, args, kClass);
        }
        return super.findAllKeysBy(addPartitionCondition(tenantID, condition));
    }
    @Override
    public int count(int tenantID, SqlExpression condition)  throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getCountByQuery(condition);
            String sql = sqlQuery.getTenancySql(tenantID,metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return this.jdbcTemplate.queryForObject(sql, condition.getArgValues().toArray(), Integer.class);
        }
        return super.count(addPartitionCondition(tenantID, condition));
    }


    @Override
    public int count(int tenantID, String condition, Object...args) throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getCountAllQuery();
            String sql = sqlQuery.getTenancySqlWith(tenantID,condition,metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return this.jdbcTemplate.queryForObject(sql, Integer.class, args);
        }
        return super.count(addPartitionCondition(tenantID, condition),addPartitionArgs(tenantID,args));
    }

    @Override
    public int count(int tenantID, String condition) throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getCountAllQuery();
            String sql = sqlQuery.getTenancySqlWith(tenantID,condition,metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return this.jdbcTemplate.queryForObject(sql, Integer.class);
        }
        return super.count(addPartitionCondition(tenantID, condition),getMinEntityID(tenantID),getMaxEntityID(tenantID));
    }

    @Override
    public int count(int tenantID, String word, String condition, Object... args) throws DataAccessException {
        BaseUtil.requireNonNull(word, "word");
        BaseUtil.requireNonNull(condition, "condition");
        boolean onlyString = isNotNumber(word);
        String finalCondition = addPartitionCondition(tenantID, condition);
        Object[] finalArgs = addPartitionArgs(tenantID, args);

        SqlQuery sqlQuery = getSearchAllQuery(onlyString);

        String sql = sqlQuery.executor()
                .searchWord(word)
                .and(finalCondition)
                .count();

        if (logger.isDebugEnabled()) {
            logger.debug(sql);
        }
        return super.count(finalCondition, finalArgs);
    }
    @Override
    public int countBySearchWord(int tenantID, String word) throws DataAccessException {
        BaseUtil.requireNonNull(word, "word");

        boolean onlyString = isNotNumber(word);
        String finalCondition = addPartitionCondition(tenantID,"");
        Object[] finalArgs = addPartitionArgs(tenantID);

        SqlQuery sqlQuery = getSearchAllQuery(onlyString);

        String sql = sqlQuery.executor()
                .searchWord(word)
                .and(finalCondition)
                .count();

        if (logger.isDebugEnabled()) {
            logger.debug(sql);
        }
        return super.count(finalCondition, finalArgs);
    }

    public int execute(int tenantID, SqlQuery sqlQuery, List<Object> args) throws DataAccessException{
        String sql = sqlQuery.getTenancySql(tenantID,metadataProvider);
        if(logger.isDebugEnabled()) logger.debug(sql);
        return jdbcTemplate.update(sql, args.toArray(), sqlQuery.getArgJdbcTypeArray());
    }

    @Override
    public boolean exists(int tenantID, K k) throws DataAccessException {
        if(enableNamedPartition()){
            SqlQuery countByKeyQuery = getCountByKeyQuery();
            String sql = countByKeyQuery.getTenancySql(tenantID,metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.queryForObject(sql, toKeyArgArray(k), Integer.class) > 0;
        }
        List<Object> argList = toKeyArgList(k);
        SqlExpression conditions = SqlExpression.ofAllEqual(metaObj.getKeyCols(),argList);
        return super.count(addPartitionCondition(tenantID, conditions))>0;
    }

    @Override
    public int delete(int tenantID, K k) throws DataAccessException{
        ensureMatchedTenantID(tenantID, k);
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getDeleteQuery();
            List<Object> keyArgs = toKeyArgList(k);
            return execute(tenantID, sqlQuery, keyArgs);
        }
        return super.delete(k);
    }

    @Override
    public int delete(int tenantID, K k, SqlExpression conditions) throws DataAccessException {
        ensureMatchedTenantID(tenantID, k);
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getDeleteByKeyAndCondQuery(k,conditions);
            List<Object> argValues = toKeyArgList(k);
            argValues.addAll(conditions.getArgValues());

            return execute(tenantID, sqlQuery, argValues);
        }
        return super.delete(k,addPartitionCondition(tenantID, conditions));
    }

    @Override
    public int deleteAll(int tenantID, SqlExpression conditions) throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getDeleteAllByQuery(conditions);
            return execute(tenantID, sqlQuery,conditions.getArgValues());
        }
        //TODO 会导致每个租户缓存？还是需要增加argValues
        return super.deleteAll(addPartitionCondition(tenantID, conditions));
    }

    @Override
    public int deleteAll(int tenantID, String condition, Object...args) throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getDeleteAllQuery();
            String sql = sqlQuery.getTenancySqlWith(tenantID, condition, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return this.jdbcTemplate.update(sql, args);
        }

        return super.deleteAll(addPartitionCondition(tenantID, condition),addPartitionArgs(tenantID,args));
    }

    @Override
    public int[] deleteAll(final int tenantID, final Collection<K> keys, final String andCondition) throws DataAccessException{
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getDeleteQuery();
            String sql = BaseUtil.hasText(andCondition)
                    ? sqlQuery.getTenancySqlWith(tenantID, andCondition, metadataProvider)
                    : sqlQuery.getTenancySql(tenantID,metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            List<Object[]> batchArgs = keys.stream()
                    .map(k -> toKeyArgArray(k))
                    .collect(Collectors.toList());
            return this.jdbcTemplate.batchUpdate(sql, batchArgs);
        }
        if(keys.stream().anyMatch(k -> Tenancy.toTenantID(k)!=tenantID)){
            throw new TenancyDataAccessException("有主键与租户ID不匹配");
        }
        return super.deleteAll(keys,andCondition);
    }

    @Override
    public int[] updateAll(final int tenantID, final Collection<T> tCollection) throws DataAccessException{
        BaseUtil.requireNoneEmpty(tCollection,"tCollection");
        if(tCollection.stream().anyMatch(t -> t.getTenantID()!=tenantID)){
            throw new TenancyDataAccessException("有主键与租户ID不匹配");
        }
        if(enableNamedPartition()){
            SqlQuery sqlQuery = getUpdateQuery();
            String sql = sqlQuery.onNamedPartition(tenantID, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            List<Object[]> batchArgs = buildBatchUpdateArgs(tCollection);
            return this.jdbcTemplate.batchUpdate(sql, batchArgs);
        }

        return super.updateAll(tCollection);
    }

    @Override
    public int updateByUniqueKey(int tenantID, final String codeNo, SqlExpression attributes)  throws DataAccessException{
        BaseUtil.requireNonBlank(codeNo,"codeNo");
        if(BaseUtil.isNullOrWhitesapce(metaObj.getUniqueKey()))
            throw new InvalidDataAccessResourceUsageException(tClassName+"元数据未定义唯一索引。");

        SqlQuery sqlQuery = getUpdateByUniqueKeyQuery(attributes);
        List<Object> argValues = attributes.getAttrValues();
        argValues.add(codeNo);

        if(enableNamedPartition()){
            String sql = sqlQuery.onNamedPartition(tenantID, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.update(sql, argValues.toArray(), sqlQuery.getArgJdbcTypeArray());
        }

        // tenancy
        String sql = sqlQuery.executor().update(tenantID,metadataProvider);
        if(logger.isDebugEnabled()) logger.debug(sql);
        return jdbcTemplate.update(sql, argValues.toArray(), sqlQuery.getArgJdbcTypeArray());
    }

    @Override
    public int updateByUniqueKey(int tenantID, final String codeNo, SqlExpression attributes, SqlExpression condition)  throws DataAccessException{
        BaseUtil.requireNonBlank(codeNo,"codeNo");
        if(BaseUtil.isNullOrWhitesapce(metaObj.getUniqueKey()))
            throw new InvalidDataAccessResourceUsageException(tClassName+"元数据未定义唯一索引。");

        SqlQuery sqlQuery = getUpdateByUniqueKeyAndQuery(attributes,condition);
        List<Object> argValues = attributes.getAttrValues();
        argValues.add(codeNo);
        argValues.addAll(condition.getArgValues());

        if(enableNamedPartition()){
            String sql = sqlQuery.onNamedPartition(tenantID, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);
            return jdbcTemplate.update(sql, argValues.toArray(), sqlQuery.getArgJdbcTypeArray());
        }

        // tenancy
        String sql = sqlQuery.executor().update(tenantID,metadataProvider);
        if(logger.isDebugEnabled()) logger.debug(sql);
        return jdbcTemplate.update(sql, argValues.toArray(), sqlQuery.getArgJdbcTypeArray());
    }

    private int updateBy(int tenantID, Map<String,Object> keyAndAttributes){
        K k = popKey(keyAndAttributes);
        ensureMatchedTenantID(tenantID, k);
        if(keyAndAttributes.isEmpty()) return 0;
        return update(k, keyAndAttributes);
    }

    @Override
    public int[] updateAll(final int tenantID, final Collection<Map<String,Object>> listOfKeyAndAttributes, final List<K> updatedKeys){
        return listOfKeyAndAttributes.stream().mapToInt(
                keyAndAttributes -> {
                    try{
                        K k = popKey(keyAndAttributes);
                        if(keyAndAttributes.isEmpty()) return 0;
                        ensureMatchedTenantID(tenantID, k);
                        int r = update(k, keyAndAttributes);
                        if(r>0) updatedKeys.add(k);
                        return r;
                    }
                    catch (Exception ex){
                        return 0;
                    }
                }
        ).toArray();
    }
    @Override
    public int[] insertMany(final int tenantID, final Collection<T> tCollection) throws DataAccessException{
        BaseUtil.requireNoneEmpty(tCollection, "tCollection");
        if(tCollection.stream().anyMatch(t -> t.getTenantID()!=tenantID)){
            throw new TenancyDataAccessException("有主键与租户ID不匹配");
        }
        if(enableNamedPartition()){
            Optional<T> first = tCollection.stream().findFirst();
            SqlQuery insertQuery = getInsertQuery(first.get());
            String sql = insertQuery.onNamedPartition(tenantID, metadataProvider);
            if(logger.isDebugEnabled()) logger.debug(sql);

            if(insertQuery.getGeneratedKeys().isEmpty()) {
                List<Object[]> batchArgs = buildBatchInsertArgs(tCollection);
                return this.jdbcTemplate.batchUpdate(sql, batchArgs);
            }
            else{
                return tCollection.stream()
                        .mapToInt(t -> insert(t))
                        .toArray();
            }
        }

        return super.insertMany(tCollection);
    }

    /**
     * 查找唯一键编码表达式
     * @param tenantId
     * @return
     */
    public String findUniqueKeyExpression(int tenantId){
        var codeRuleTable = metadataProvider.getDbPrefix()+"base.CodeRule";
        var sql = "select uniqueKeyExpression from " + codeRuleTable
                + " where ruleID between ? and ? and uniqueKey = ?";
        var minId = Tenancy.buildEntityID(tenantId,0);
        var maxId = minId | 0xFFFF_FFFF_FFFFL;
        var uniqueKey = String.join(".",
                uniqueKeyCol.getDbSchema(),
                uniqueKeyCol.getObjName(),
                uniqueKeyCol.getColName()
        );
        try{
            return jdbcTemplate.queryForObject(sql, new Object[]{minId, maxId, uniqueKey }, String.class);
        }
        catch(Exception e){
            logger.error("Code rule of " + uniqueKey + " not found.", e);
            return null;
        }
    }

    @Override
    public boolean validateUniqueKey(long id, final String uniqueNo) throws DataAccessException,NullPointerException{
        Objects.requireNonNull(uniqueKeyCol,metaObj.getObjName()+"没有定义唯一键字段");

        if(!BaseUtil.hasText(uniqueNo)){
            return uniqueKeyCol.isNullable();
        }
        int tenantID = Tenancy.parseTenantID(id);
        SqlExpression condition = this.expressionBuilder()
                .exp(partitionCol).notEqual(id)
                .and(uniqueKeyCol).equal(uniqueNo)
                .result();
        int r = count(tenantID,condition);
        return r == 0;
    }

    @Override
    public boolean validateUniqueKey(T t) throws DataAccessException,NullPointerException{
        Objects.requireNonNull(uniqueKeyCol,metaObj.getObjName()+"没有定义唯一键字段");

        String uniqueNo = null;
        long partitionID;
        try {
            Object uniqueKeyObj = getUniqueKeyValue(t);
            if(uniqueKeyObj!=null) uniqueNo = (String) uniqueKeyObj;
            Object partitionIDObj = getPropertyValue(t, partitionKey);
            partitionID = (long)partitionIDObj;
        }
        catch (Exception ex){
            throw new TenancyDataAccessException("无法获得租户唯一键值partitionID和uniqueKey",ex);
        }

        return validateUniqueKey(partitionID,uniqueNo);
    }

    public List<Long> findDescendantDivisionDepartmentIDs(long deptID) {
        try {
            var database = metadataProvider.getDbPrefix() + "base";
            String sql = "CALL " + database + ".GetDescendantDivisionDepartmentIDs(?)";

            if (!metadataProvider.getCurrentDbName().contains("base")) {
               return new JdbcTemplate(getDataSource(database)).queryForList(sql, Long.class, deptID);
            }
            return jdbcTemplate.queryForList(sql, Long.class, deptID);
        } catch (Exception ex) {
            logger.error("获取部门下的子公司异常" + ex.getLocalizedMessage(), ex.getCause());
        }
        return Collections.emptyList();

    }
}
