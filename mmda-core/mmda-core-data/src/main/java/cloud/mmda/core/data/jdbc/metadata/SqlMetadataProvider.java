package cloud.mmda.core.data.jdbc.metadata;

import cloud.mmda.core.data.jdbc.metadata.mappers.*;
import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.data.sql.SqlQuery;
import cloud.mmda.core.entities.Entity;
import cloud.mmda.core.entities.EntityFilterCondition;
import cloud.mmda.core.enums.DisplayShape;
import cloud.mmda.core.enums.ExtensionType;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.enums.ModuleType;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.metadata.Module;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.DateRangeKind;
import cloud.mmda.core.utils.NamingUtil;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * Sql元数据提供者
 * @author roshion
 *
 * 采用Jdbc实现的元数据提供者，具体实现类有{@link MySqlMetadataProvider}，{@link SqlServerMetadataProvider}
 */
public abstract class SqlMetadataProvider implements DbMetadataProvider {
    private static final Log logger = LogFactory.getLog(SqlMetadataProvider.class);
    public static final String DOT = ".";
    public static final String DEFAULT_LOCALE = "zh";

    protected static final String SELECT_ALL_FROM = "select * from ";
    protected static final String COUNT_ALL_FROM = "select count(*) from ";
    protected static final String WHERE = " where dbSchema=? and objName = ?";
    private static final String SELECT_SCHEMA = "select dbSchema from MetaObject where namespace=? and objName=?";

    //其中relativeObj需支持跨数据库引用，例如 mmda_base.Country
//    private static final Pattern enumSetPattern = Pattern.compile("^(?<relationType>\\w+)\\s+(?<relativeObj>\\w+(\\.\\w+)?)\\((?<relativeCols>[\\w|,]+)\\)(\\s+AS\\s+(?<relationName>\\w+))?(\\s+WHERE\\s*\\((?<where>.+)\\))?(\\s+GROUP\\s+BY\\s+(?<groupBy>\\w+))?(\\s+(?<readOnly>READONLY))?$");
//    private static final Pattern enumSetPattern = Pattern.compile("^(?<relationType>\\w+)\\s+(?<relativeObj>\\w+(\\.\\w+)?)\\((?<relativeCols>[\\w|,]+)\\)(\\s+AS\\s+(?<relationName>\\w+))?(\\s+WHERE\\s*\\((?<where>.+)\\))?(\\s+GROUP\\s+BY\\s+(?<groupBy>\\w+))?(\\s+(?<shape>[LIST|TREE|HIERARCHY]))?(\\s+(?<oneTime>ONETIME))?$");
    private static final Pattern enumSetPattern = Pattern.compile("^(?<relationType>\\w+)\\s+(?<relativeObj>\\w+(\\.\\w+)?)\\((?<relativeCols>[\\w|,]+)\\)(\\s+AS\\s+(?<relationName>\\w+))?(\\s+WHERE\\s*\\((?<where>.+)\\))?(\\s+GROUP\\s+BY\\s+(?<groupBy>\\w+))?(\\s+(?<shape>LIST|TREE|HIERARCHY|PHOTO))?(\\s+(?<readOnly>READONLY))?(\\s+(?<oneTime>ONETIME))?$");

    protected final JdbcTemplate jdbcTemplate;
    protected String currentDbName,metadataDbName, dbPrefix;
    //meta data
    protected final MetaObjectRowMapper objRowMapper;
    private final MetaDbRowMapper dbRowMapper;
    private final MetaColRowMapper colRowMapper;
    private final MetaDataTypeRowMapper dtRowMapper;
    private final MetaEnumRowMapper enumRowMapper;
    private final MetaRelationRowMapper relationRowMapper;
    //meta ui
    private final MetaUiFieldRowMapper uiFieldRowMapper;
    private final MetaUiFieldI18ntRowMapper uiFieldI18ntRowMapper;
    private final MetaUi18nRowMapper ui18nRowMapper;
    //module
    private final ModuleRowMapper moduleRowMapper;
    private final ModuleActionRowMapper moduleActionRowMapper;
    private final ModuleFlowRowMapper moduleFlowRowMapper;

    //缓存
    private ConcurrentMap<String, MetaDb> dbMap;
    private ConcurrentMap<Integer, MetaDataType> dtMap;
    private ConcurrentMap<String, MetaEnum> eMap;
    private ConcurrentMap<String,String> i18nMap;

    private RedisTemplate<String,MetaObject> metaObjRedisTemplate;
    private RedisTemplate<String,MetaUi> metaUiRedisTemplate;
    private final boolean useRedisCache;

    @Autowired
    public SqlMetadataProvider(DataSource ds, String currDbName, String metadataDbName, RedisConnectionFactory factory){
        this.jdbcTemplate = new JdbcTemplate(ds);
        //metadata
        this.objRowMapper = new MetaObjectRowMapper();
        this.dbRowMapper = new MetaDbRowMapper();
        this.colRowMapper = new MetaColRowMapper();
        this.dtRowMapper = new MetaDataTypeRowMapper();
        this.enumRowMapper = new MetaEnumRowMapper();
        this.relationRowMapper = new MetaRelationRowMapper();
        //metaUi
        this.uiFieldRowMapper = new MetaUiFieldRowMapper();
        this.uiFieldI18ntRowMapper = new MetaUiFieldI18ntRowMapper();
        this.ui18nRowMapper = new MetaUi18nRowMapper();
        //module
        this.moduleRowMapper = new ModuleRowMapper();
        this.moduleActionRowMapper = new ModuleActionRowMapper();
        this.moduleFlowRowMapper = new ModuleFlowRowMapper();

        this.currentDbName = currDbName;
        this.metadataDbName = metadataDbName;
        int underscoreIndex = currDbName.indexOf('_');
        this.dbPrefix = underscoreIndex != -1 ? currDbName.substring(0, underscoreIndex+1) : "";

        this.useRedisCache = (factory != null);
        if(useRedisCache) {
            createMetadataRedisTemplate(factory);
            createMetaUiRedisTemplate(factory);
        }
        initialize();
    }

    public String getDbPrefix(){
        return dbPrefix;
    }

    /**
     * 获取当前数据库名称，例如mmda_crm
     * @return
     */
    public String getCurrentDbName() {
        return currentDbName;
    }

    /**
     * 获取当前数据库元数据
     * @return
     */
    public MetaDb getCurrentMetaDb(){
        return getMetaDb(currentDbName);
    }

    protected void initialize(){
        List<MetaDb> dbs = getMetaDbs();
        dbMap = dbs.stream()
                .collect(Collectors.toConcurrentMap(MetaDb::getDbSchema, Function.identity()));

        List<MetaDataType> dataTypes = getMetadataTypes();
        dtMap = dataTypes.stream()
                .collect(Collectors.toConcurrentMap(MetaDataType::getDataType, Function.identity()));

        List<MetaEnum> enums = getMetaEnums();
        eMap = enums.stream()
                .collect(Collectors.toConcurrentMap(MetaEnum::getEnumClass,Function.identity()));

        List<MetaUi18n> i18ns = getMetaUi18ns();
        i18nMap = i18ns.stream()
                .collect(Collectors.toConcurrentMap(MetaUi18n::getWordsNLocale,MetaUi18n::getTranslation));
    }

    private void createMetadataRedisTemplate(RedisConnectionFactory factory){
        Jackson2JsonRedisSerializer<MetaObject> serializer = new Jackson2JsonRedisSerializer<>(MetaObject.class);
        metaObjRedisTemplate = new RedisTemplate<String, MetaObject>();
        metaObjRedisTemplate.setConnectionFactory(factory);
        metaObjRedisTemplate.setKeySerializer(RedisSerializer.string());
        metaObjRedisTemplate.setValueSerializer(serializer);
        metaObjRedisTemplate.setHashKeySerializer(RedisSerializer.string());
        metaObjRedisTemplate.setHashValueSerializer(serializer);
        metaObjRedisTemplate.afterPropertiesSet();
    }
    private void createMetaUiRedisTemplate(RedisConnectionFactory factory){
        Jackson2JsonRedisSerializer<MetaUi> serializer = new Jackson2JsonRedisSerializer<>(MetaUi.class);
        metaUiRedisTemplate = new RedisTemplate<String, MetaUi>();
        metaUiRedisTemplate.setConnectionFactory(factory);
        metaUiRedisTemplate.setKeySerializer(RedisSerializer.string());
        metaUiRedisTemplate.setValueSerializer(serializer);
        metaUiRedisTemplate.setHashKeySerializer(RedisSerializer.string());
        metaUiRedisTemplate.setHashValueSerializer(serializer);
        metaUiRedisTemplate.afterPropertiesSet();
    }

    /**
     * 获取元数据库
     * @param dbSchema 数据库名称或者简称，例如mmda_base 或者 base
     * @return
     */
    @Override
    public MetaDb getMetaDb(String dbSchema){
        var db = dbMap.get(dbSchema);
        if(db == null) {
            var foundDb = dbMap.values().stream()
                    .filter(it -> it.getDbSchema().equals(dbSchema))
                    .findFirst();
            if(foundDb.isPresent()) {
                db = foundDb.get();
            }
        }
        return db;
    }

    @Override
    public MetaDataType getMetadataType(Integer dataType){
        return dtMap.get(dataType);
    }

    @Override
    public MetaEnum getMetaEnum(String enumClassName){
        return eMap.get(enumClassName);
    }
    @Override
    public abstract String quoteName(String name);

    @Override
    public abstract char getParamPlaceholder();


    protected String selectAll(String from){
        return SELECT_ALL_FROM + metadataDbName + DOT + from;
    }

    private List<MetaDb> getMetaDbs() throws DataAccessException {
        String sql = selectAll("MetaDb");
        return this.jdbcTemplate.query(sql, dbRowMapper);
    }
    private List<MetaDataType> getMetadataTypes() throws DataAccessException {
        String sql = selectAll("MetaDataType");
        return this.jdbcTemplate.query(sql, dtRowMapper);
    }
    public List<MetaEnum> getMetaEnums() throws DataAccessException{
        String sql = selectAll("MetaEnum");
        return this.jdbcTemplate.query(sql, enumRowMapper);
    }

    private List<MetaCol> getMetaCols(String dbSchema, String objName) throws DataAccessException{
        String sql = selectAll("MetaCol") + WHERE + " order by colIdx";
        return this.jdbcTemplate.query(sql,new Object[]{dbSchema,objName}, colRowMapper);
    }

    private List<MetaRelation> getMetaRelations(String dbSchema, String objName) throws DataAccessException{
        String sql = selectAll("MetaRelation") + WHERE + " order by relationIdx";
        return this.jdbcTemplate.query(sql,new Object[]{dbSchema,objName}, relationRowMapper);
    }

//    private final BiMap<Class<?>,MetaObject> objClassMap = Maps.synchronizedBiMap(HashBiMap.create()) ;

    private String getDbSchema(@NonNull final  Class<?> entityClass){
        return this.jdbcTemplate.queryForObject(SELECT_SCHEMA, String.class, entityClass.getPackageName(), entityClass.getSimpleName());
    }

    @Override
    public <T> MetaObject getMetaObject(@NonNull final Class<T> objClass){
        //根据类名和包名识别出数据库对象
        var objName = objClass.getSimpleName();
        var dbSchema = getDbSchema(objClass);
        return getMetaObject(dbSchema, objName);
    }

    /**
     * 获取对象元数据
     * @param objName 对象名称，可以是表名如Country，也可以是全称如 base.Country
     * @return
     * @throws DataAccessException
     */
    @Override
    public MetaObject getMetaObject(String objName) throws DataAccessException {
        Objects.requireNonNull(objName,"objName shouldn't be null");
        if(objName.indexOf(".")!=-1){
            String[] obj = objName.split("\\.");
            return getMetaObject(obj[0],obj[1],false);
        }
        return getMetaObject(currentDbName,objName,false);
    }


    @Override
    public MetaObject getMetaObject(String dbSchema, String objName, boolean reload) throws DataAccessException {
        Objects.requireNonNull(dbSchema,"dbSchema shouldn't be null");
        Objects.requireNonNull(objName,"objName shouldn't be null");

        var metaDb = getMetaDb(dbSchema);
        Objects.requireNonNull(metaDb,"db not found");

        //尝试从缓存中获取
        String cacheKey = String.join(":", "MetaObjects",metaDb.getDbSchema(),objName);//dbSchema.replaceAll("^\\w+_","")
        if(useRedisCache && !reload){
            try{
                if(metaObjRedisTemplate.hasKey(cacheKey).equals(Boolean.TRUE)){
                    MetaObject cachedMetaObj = metaObjRedisTemplate.opsForValue().get(cacheKey);
                    for(MetaCol col : cachedMetaObj.getCols()){
                        if(col.getRelationType()==MetaRelationType.ENUM){
                            MetaEnum metaEnum = getMetaEnum(col.getEnumClass());
                            col.setMetaEnum(metaEnum);
                        }
                    }
                    return cachedMetaObj;
                }
            }
            catch (Exception ex){
                logger.warn("从Redis缓存读取元对象"+objName+"异常。", ex);
            }
        }

        String sql = selectAll("MetaObject") + WHERE;
        MetaObject metaObj = this.jdbcTemplate.queryForObject(sql,new Object[]{metaDb.getDbSchema(),objName}, objRowMapper);

        metaObj.setCols(getMetaCols(dbSchema,objName));
        metaObj.setRelations(getMetaRelations(dbSchema,objName));

        //解析defaultVal && enumSet
        int relationIdx = metaObj.getRelations().size();
        for (MetaCol col : metaObj.getCols()){
            //缺省值
            String defaultVal = col.getDefaultVal();
            if(!"NULL".equals(defaultVal) && BaseUtil.hasText(defaultVal)){
                col.setParsedDefaultVal(MetaDataType.parseDefaultValue(defaultVal,col.getDataType()));
            }
            //枚举设置
            String enumSet = col.getEnumSet();
            if(BaseUtil.isNullOrWhitesapce(enumSet)){
                col.setRelationType(MetaRelationType.NONE);
                continue;
            }

            //使用正则表达式解析
            Matcher matcher = enumSetPattern.matcher(enumSet);
            if(matcher.matches()){
                //只可能HAS_ONE或者REF
                var relationType = MetaRelationType.parse(matcher.group("relationType"));
                String relativeDb = dbSchema;
                String relativeObj = matcher.group("relativeObj");
                String relationName = matcher.group("relationName");//such as partner, sycloud_metadata.MetaObject
                //处理跨数据库引用
                if(relativeObj.indexOf('.')!=-1){
                    String[] dbAndObjName = relativeObj.split("\\.");
                    relativeDb = dbAndObjName[0];
                    relativeObj = dbAndObjName[1];
                }

                if(relationName == null ) relationName = NamingUtil.firstLetterLower(relativeObj);

                MetaRelation relation = new MetaRelation();
                relation.setDbSchema(dbSchema);
                relation.setObjName(objName);
                relation.setRelationType(relationType);
                relation.setRelationName(relationName);
                relation.setDisplayLabel(col.getDisplayLabel());
                relation.setRelationIdx(++relationIdx);
                relation.setRelativeDbSchema(relativeDb);
                relation.setRelativeObjName(relativeObj);

                String[] relativeCols = matcher.group("relativeCols").split(",");
                relation.setValueColName(relativeCols[0]);
                relation.setLabelColName(relativeCols[1]);
                if(relativeCols.length > 2
                        && !relativeCols[2].startsWith("parent")
                        && !relativeCols[2].startsWith("super")
                        && !relativeCols[2].endsWith("ID")
                ){
                    //多个字段拼接成一个文本字段，例如 CONCAT(partnerCode,partnerName) AS partnerCode
                    // 如果有超过3个字段，拼接 relativeCols[2] 及其后面的字段
                    String labelColExtra = String.join(",", Arrays.copyOfRange(relativeCols, 2, relativeCols.length));
                    relation.setLabelColExtra(labelColExtra);
//                    relation.setLabelColExtra(relativeCols[2]);
                }


                relation.setJoinType(col.isNullable()?MetaRelation.LEFT_JOIN:MetaRelation.INNER_JOIN);
                relation.setJoinOn(
                        relativeCols[0]+"=@"+col.getColName()
                    );
                relation.setWhere(matcher.group("where"));
                relation.setGroupColName(matcher.group("groupBy"));
                var shape = matcher.group("shape");
                if(shape != null){
                    relation.setDisplayShape(DisplayShape.valueOf(shape.toUpperCase()));
                    relation.setShapeKey(relation.getGroupColName());
                }
                else{
                    relation.setDisplayShape(DisplayShape.LIST);
                    relation.setShapeKey(null);
                }

                relation.setReadOnly("READONLY".equalsIgnoreCase(matcher.group("readOnly")));
                relation.setOneTime("REF_ONE".equals(matcher.group("relationType")) || "ONETIME".equalsIgnoreCase(matcher.group("oneTime")));
                relation.setRequiredAny(!col.isNullable());

                col.setRelationType(relationType);
                col.setRelationName(relationName);
                metaObj.addRelation(relation);
            }
            else if(enumSet.startsWith("ENUM ")){
                col.setRelationType(MetaRelationType.ENUM);
                String enumClass = enumSet.substring(5);
                col.setEnumClass(enumClass);//用于组装缓存的MetaEnum
                MetaEnum metaEnum = eMap.get(enumClass);
                col.setMetaEnum(metaEnum);
            }
            else if(enumSet.startsWith("ENUMS ")){
                col.setRelationType(MetaRelationType.ENUM);
                String enumClass = enumSet.substring(6);
                col.setEnumClass(enumClass);//用于组装缓存的MetaEnum
                MetaEnum metaEnum = eMap.get(enumClass);
                col.setMetaEnum(metaEnum);
                col.setBitSet(true);
            }
        }

        //加载扩展表
        if(metaObj.getExtendType() == ExtensionType.EXTENDS){
            if(BaseUtil.hasText(metaObj.getSuperName())){
                var superMetaObj = getMetaObject(metaObj.getSuperName());
                metaObj.extend(superMetaObj);
            }
            else{
                logger.error(objName+"未定义扩展基类");
            }
        }

        //放入缓存
        if(useRedisCache) {
            try{
                metaObjRedisTemplate.opsForValue().set(cacheKey,metaObj);
            }
            catch (Exception ex){
                logger.warn("Redis缓存写入元对象"+objName+"数据异常。", ex);
            }
        }

        return metaObj;
    }


    @Override
    public MetaCol findCol(String name, Collection<String> inObjs)  throws DataAccessException{
        for(String objName : inObjs){
            MetaObject metaObj = getMetaObject(objName);
            MetaCol col = metaObj.getCol(name);
            if(col!=null) return col;
        }
        return null;
    }

    protected String countAll(String from){
        return COUNT_ALL_FROM
                + metadataDbName + DOT + from;
    }

    @Override
    public boolean hasExtensions(String dbSchema, String objName) throws DataAccessException{
        String sql = countAll("MetaObject")  +
                " where dbSchema=? and objName <> ? and extendType=2 and superName=?";
        int r = this.jdbcTemplate.queryForObject(sql,Integer.class,dbSchema,objName,objName);
        return r>0;
    }

    @Override
    public String toRowNumSql(Sort sort, String partition){
        String orderBy = sort!=null
                ? SqlQuery.ORDER_BY + sort.toString()
                : SqlQuery.SLOT_SORT;

        return StringUtils.isEmpty(partition)
                ? "ROW_NUMBER() OVER("+ orderBy +") AS rowNum"
                : "ROW_NUMBER() OVER(PARTITION BY "+partition + " " + orderBy +") AS rowNum";
    }

    @Override
    public List<MetaObject> findMetaObjects(Paginator paginator) throws DataAccessException {
        String sql = selectAll("MetaObject")  + toPagerSql(paginator);
        return this.jdbcTemplate.query(sql, objRowMapper);
    }

    @Override
    public List<MetaObject> findMetaObjects(String dbSchema, Paginator paginator) throws DataAccessException {
        String sql = selectAll("MetaObject") + " where dbSchema = ?" + toPagerSql(paginator);
        return this.jdbcTemplate.query(sql, objRowMapper, dbSchema);
    }

    @Override
    public List<MetaObject> searchMetaObjects(Paginator paginator, String searchText) throws DataAccessException{
        String sql = selectAll("MetaObject")
                + " where dbSchema like %?% or objName like %?% or displayLabel like %?% or nameSpace like %?%"
                + toPagerSql(paginator);
        return this.jdbcTemplate.query(sql, objRowMapper, searchText);
    }

    @Override
    public List<MetaRelation> getManyToOneRelations(String dbSchema, String objName) throws DataAccessException{
        String sql = selectAll("MetaRelation") + " where dbSchema=? and relativeObjName=?";
        return this.jdbcTemplate.query(sql, relationRowMapper, dbSchema, objName);
    }


    @Override
    public List<EntityFilterCondition> buildDateFilterConditions(String dateColName){
        return Arrays.stream(DateRangeKind.values())
                .map(dateRangeKind -> new EntityFilterCondition("t."+dateColName+ SqlExpression.EQUAL+ dateRangeKind.name(), dateRangeKind.getText()))
                .collect(Collectors.toList());
    }

    private Map<DateRangeKind, Function<MetaCol,String>> dateRangers = new LinkedHashMap<DateRangeKind, Function<MetaCol,String>>(){
        {
            put(DateRangeKind.TODAY, col->getSqlDateTime().onToday(col));
            put(DateRangeKind.YESTERDAY, col->getSqlDateTime().onYesterday(col));
            put(DateRangeKind.THIS_WEEK, col->getSqlDateTime().inThisWeek(col));
            put(DateRangeKind.LAST_WEEK, col->getSqlDateTime().inLastWeek(col));
            put(DateRangeKind.LAST_7_DAYS, col->getSqlDateTime().inLastNDays(col,7));
            put(DateRangeKind.THIS_MONTH, col->getSqlDateTime().inThisMonth(col));
            put(DateRangeKind.LAST_MONTH, col->getSqlDateTime().inLastMonth(col));
            put(DateRangeKind.LAST_30_DAYS, col->getSqlDateTime().inLastNDays(col,30));
            put(DateRangeKind.THIS_QUARTER, col->getSqlDateTime().inThisQuarter(col));
            put(DateRangeKind.LAST_QUARTER, col->getSqlDateTime().inLastQuarter(col));
            put(DateRangeKind.THIS_YEAR, col->getSqlDateTime().inThisYear(col));
            put(DateRangeKind.LAST_YEAR, col->getSqlDateTime().inLastYear(col));
            put(DateRangeKind.EARLIER, col->getSqlDateTime().inEarlier(col));
        }
    };
    public String translateDateFilterCondition(MetaCol dateCol, DateRangeKind dateRangeKind){
        return dateRangers.get(dateRangeKind).apply(dateCol);
    }

    //region MetaUi

    private static final String SQL_SELECT_FIELDS =
            "SELECT c.colIdx AS fieldIdx, c.groupLabel,c.displayLabel,c.mergeLabel,c.mergePrefix," +
                    "c.colName AS fieldName,f.fieldName AS fieldNameAlias,f.emphasized,f.listSize,f.align,f.sortable,f.renderer,f.formatter,f.suffix," +
                    "f.editor,f.selectOptions,f.validationRules,f.placeholder," +
                    "f.nullDisplayText,f.tooltip,f.dataBinding," +
                    "c.isKey AS primaryKey,c.dataType,c.isUnsigned,c.maxLength,c.numericPrecision,c.numericScale,c.nullable,c.formula,c.defaultVal,c.listed,c.aggregationSet,c.readOnly,c.hidden " +
            "FROM %1$s.MetaCol c INNER JOIN %1$s.MetaUiField f ON c.fieldName=f.fieldName " +
            "WHERE c.dbSchema=? AND c.objName=?";
    private static final String SQL_SELECT_FIELDS_LOCALE =
            "SELECT c.colIdx AS fieldIdx, %2$s(x.translation,c.groupLabel) AS groupLabel,%2$s(n.displayLabel,c.displayLabel) AS displayLabel, %2$s(x.translation,c.mergeLabel) AS mergeLabel,c.mergePrefix," +
                    "c.colName AS fieldName,f.fieldName AS fieldNameAlias,f.emphasized,%2$s(n.listSize,f.listSize) AS listSize,f.align,f.sortable,f.renderer,f.formatter,f.suffix," +
                    "f.editor,%2$s(n.selectOptions,f.selectOptions) AS selectOptions,f.validationRules,%2$s(n.placeholder,f.placeholder) AS placeholder," +
                    "%2$s(n.nullDisplayText,f.nullDisplayText) AS nullDisplayText,%2$s(n.tooltip,f.tooltip) AS tooltip,f.dataBinding," +
                    "c.isKey AS primaryKey,c.dataType,c.isUnsigned,c.maxLength,c.numericPrecision,c.numericScale,c.nullable,c.formula,c.defaultVal,c.listed,c.aggregationSet,c.readOnly,c.hidden " +
            "FROM %1$s.MetaCol c " +
            "   INNER JOIN %1$s.MetaUiField f ON c.fieldName=f.fieldName" +
            "   LEFT OUTER JOIN %1$s.MetaUiFieldI18n n ON f.fieldName=n.fieldName AND n.locale=?" +
            "   LEFT OUTER JOIN %1$s.MetaUi18n x ON c.groupLabel=x.words AND x.locale=?" +
            "WHERE c.dbSchema=? AND c.objName=?";

    private static final String SQL_SELECT_FIELD_TENANCY
            = "SELECT * FROM %1$s.MetaUiFieldI18nt WHERE fieldName in('%2$S') and locale=? and tenantID=?";

    private static final String SQL_SELECT_TRANSLATION =
            "SELECT translation FROM %1$s.MetaUi18n WHERE words=? and locale=?";

    public Optional<MetaUiField> getMetaUiField(MetaCol col){
        String sql = String.format(SQL_SELECT_FIELDS,metadataDbName)+" AND f.fieldName=?";
        List<MetaUiField> fields = this.jdbcTemplate.query(sql,uiFieldRowMapper,col.getDbSchema(),col.getObjName(),col.getFieldName());
        if(fields.isEmpty()) return Optional.empty();
        return Optional.of(fields.get(0));
    }
    /**
     * 获取元界面域（不区分语言区域和租户）
     * @param dbSchema 数据库名称
     * @param objName 对象名称
     * @return
     * @throws DataAccessException
     */
    private List<MetaUiField> getMetaUiFields(final String dbSchema, final String objName) throws DataAccessException{
        String sql = String.format(SQL_SELECT_FIELDS,metadataDbName);
        return this.jdbcTemplate.query(sql,uiFieldRowMapper,dbSchema,objName);
    }
    private List<MetaUiField> getMetaUiFields(final String dbSchema, final String objName, final String locale) throws DataAccessException{
        if(DEFAULT_LOCALE.equals(locale)) return getMetaUiFields(dbSchema,objName);
        String sql = String.format(SQL_SELECT_FIELDS_LOCALE,metadataDbName,isNullFuncName());
        return this.jdbcTemplate.query(sql,uiFieldRowMapper,locale,locale,dbSchema,objName);
    }

    private List<MetaUiFieldI18nt> getMetaUiFieldI18nts(final String locale, int tenantID, final Collection<String> fieldNames) throws DataAccessException{
        String sql = String.format(SQL_SELECT_FIELD_TENANCY,metadataDbName, String.join("','",fieldNames));
        return this.jdbcTemplate.query(sql,uiFieldI18ntRowMapper,locale,tenantID);
    }

    private final String T(final String s, final String locale){
        if(DEFAULT_LOCALE.equals(locale)) return s;
        String k = s+":"+locale;
        return i18nMap.getOrDefault(k,s);
    }

    private List<MetaUi18n> getMetaUi18ns() throws DataAccessException{
        String sql = selectAll("MetaUi18n");
        return this.jdbcTemplate.query(sql,ui18nRowMapper);
    }
    private String getMetaUiTranslation(final String words, final String locale) throws DataAccessException{
        String sql = String.format(SQL_SELECT_TRANSLATION,metadataDbName);
        return this.jdbcTemplate.queryForObject(SQL_SELECT_TRANSLATION,String.class,words,locale);
    }

    /**
     * 获取元界面
     * @param dbSchema 数据库名称，如sycloud_wms
     * @param objName 对象名称，如：Partner
     * @param lang 语言，如zh,en,zh-Hant
     * @return 返回一个元界面数据，格式如下：
     * ui
     * --primaryGroups: [  //
     *      p1.订单信息
     *      ----fields: [
     *              { fieldName: 订单编号, groupLabel: 01.订单信息 },
     *              { findName: 订单日期, groupLabel: 01.订单信息 },
     *              ...
     *          ],
     *      items: { //发货明细
     *          groups: [],
     *          fields: [
     *
     *          ], //子表字段
     *      },
     *   ],
     * --summaryGroups: [
     *      s2.物流安排
     *      s3.概要信息
     *   ]
     * @throws DataAccessException
     */
    @Override
    public MetaUi getMetaUi(final String dbSchema,final String objName, final String lang, boolean reload) throws DataAccessException{
        Objects.requireNonNull(dbSchema,"dbSchema shouldn't be null");
        Objects.requireNonNull(objName,"objName shouldn't be null");
        //尝试从缓存中获取
        String simpleDbName = dbSchema.replaceAll("^\\w+_","");
        String cacheKey = String.join(":", "MetaUis",simpleDbName,objName,lang);
        if(useRedisCache && !reload){
            try{
                if(metaUiRedisTemplate.hasKey(cacheKey) == Boolean.TRUE){
                    MetaUi cachedMetaUi = metaUiRedisTemplate.opsForValue().get(cacheKey);
                    //组装子表（排除自引用）
                    if(!cachedMetaUi.isAssembled() /**&& !cachedMetaUi.getObjName().equals(objName)**/){
                        assembleChildrenGroups(cachedMetaUi,reload);
                    }
                    return cachedMetaUi;
                }
            }
            catch (Exception ex){
                logger.warn("从Redis缓存读取元界面数据"+objName+"异常。",ex);
            }

        }

        MetaObject metaObj = getMetaObject(dbSchema,objName,reload);

        MetaUi metaUi = new MetaUi();
        metaUi.setDbSchema(dbSchema);
        metaUi.setObjName(objName);
        metaUi.setDisplayLabel(T(metaObj.getDisplayLabel(),lang));//translated
        metaUi.setFixedFilter(metaObj.getFixedFilter());
        metaUi.setUniqueKey(metaObj.getUniqueKey());
        metaUi.setLastModified(metaObj.getLastModified());
        //主键
        String primaryKey = metaObj.getKeyCols().stream()
                .map(col -> col.getColName())
                .collect(Collectors.joining(","));
        metaUi.setPrimaryKey(primaryKey);
        //主表字段分组
        List<MetaUiField> fields = getMetaUiFields(dbSchema,objName,lang);//translated
        for(MetaUiField field : fields){
            if(field.getDefaultVal()==null || field.getSelectOptions()==null || !field.getSelectOptions().startsWith("[")) continue;
            MetaCol col = metaObj.getCol(field.getFieldName());
            if(col!=null && col.isEnumType()){
                //有枚举类
                try{
                    String parsedDefVal = col.getEnumMap().get(col.getDefaultVal()).getName();
                    field.setDefaultVal(parsedDefVal);
                }
                catch (Exception ex){
                    field.setDefaultVal(null);
                }
            }
        }
        List<MetaUiGroup> groups = fields.stream()
                .collect(groupingBy(MetaUiField::getGroupLabelOrDefault))//Map<String,List<MetaUiField>>
                .entrySet().stream()
                .map(entry->new MetaUiGroup(T(entry.getKey(),lang),objName,entry.getValue()))
//                .sorted(Comparator.comparing(MetaUiGroup::getGroupName))
                .collect(Collectors.toList());

        //子表
        boolean hasChildren = false;
        if(metaObj.hasRelations()){
            for (MetaRelation relation : metaObj.getRelations()){
                if(relation.getRelationType() != MetaRelationType.HAS_MANY) continue;

                String groupLabel = T(relation.getDisplayLabel(),lang);
                MetaUiGroup relUiGroup = new MetaUiGroup(relation, groupLabel);
                groups.add(relUiGroup);
                hasChildren = true;
            }
        }
        metaUi.setGroups(
                groups.stream()
                        .sorted(Comparator.comparing(MetaUiGroup::getGroupIdx))
                        .collect(Collectors.toList())
        );
        if(!hasChildren) metaUi.setAssembled(true);//没有子表不用组装

        //放入缓存
        if(useRedisCache) {
            try{
                metaUiRedisTemplate.opsForValue().set(cacheKey,metaUi);
            }
            catch (Exception ex){
                logger.warn("Redis缓存写入元界面数据"+objName+"异常。",ex);
            }
        }

        //组装
        if(!metaUi.isAssembled()) assembleChildrenGroups(metaUi,reload);

        return metaUi;
    }


    /**
     * 获取元对象的界面
     * @param objName 对象名称，最好是全称：sycloud_wms.Partner
     * @param lang 语言，如zh,en,zh-Hant
     * @return
     * @throws DataAccessException
     */
    @Override
    public MetaUi getMetaUi(final String objName, final String lang, boolean reload) throws DataAccessException{
        Objects.requireNonNull(objName,"objName shouldn't be null");
        if(objName.indexOf(".")!=-1){
            String[] obj = objName.split("\\.");
            return getMetaUi(obj[0],obj[1],lang,reload);
        }
        return getMetaUi(currentDbName,objName,lang,reload);
    }

    /**
     * 组装子界面组
     * @param metaUi
     */
    private void assembleChildrenGroups(final MetaUi metaUi,boolean reload){
        metaUi.getGroups().stream()
                .filter(group -> group.isMany())
                .forEach(group -> {
                    final boolean reloadChild = reload && !group.getRelObjName().equals(metaUi.getObjName());
                    MetaUi child =group.getRelObjName().equals(metaUi.getObjName())?null:  getMetaUi(metaUi.getDbSchema(),group.getRelObjName(),metaUi.getLocale(), reloadChild);
                    group.setGroupUi(child);
                });
//        for (MetaUiGroup group: metaUi.getGroups()             ) {
//            if(group.isMany()){
//                final boolean reloadChild = reload && !group.getRelObjName().equals(metaUi.getObjName());
//                MetaUi child = getMetaUi(metaUi.getDbSchema(),group.getRelObjName(),metaUi.getLocale(), reloadChild);
//                group.setGroupUi(child);
//            }
//        }
        metaUi.setAssembled(true);
    }

    /**
     *
     * @param tenantID 租户标识
     * @param dbSchema 数据库名称
     * @param objName 对象名称
     * @param lang 语言，如zh,en,zh-Hant
     * @return
     * @throws DataAccessException
     */
    @Override
    public MetaUi getTenancyMetaUi(int tenantID, final String dbSchema, final String objName, String lang, boolean reload) throws DataAccessException{
        MetaUi metaUi = getMetaUi(dbSchema,objName,lang,reload);
        metaUi.setTenantID(tenantID);
        try{
            List<MetaUiField> fields = metaUi.getAllFields();

            List<MetaUiFieldI18nt> i18ntFields = getMetaUiFieldI18nts(metaUi.getLocale(),tenantID,
                    fields.stream().map(MetaUiField::getFieldName)
                            .collect(Collectors.toList())
            );
            if(i18ntFields.isEmpty()) return metaUi;

            //使用租户的配置覆盖
            for(MetaUiFieldI18nt i18ntField : i18ntFields){
                for (MetaUiField field : fields){
                    if(field.trySetI18nt(i18ntField)) break;
                }
            }
        }
        catch (Exception ex){
            String error = String.format("获取租户%1$d相关的%2$s.%3$s元数据%4$s语言失败",tenantID,dbSchema,objName,lang);
            logger.error(error,ex);
        }

        return metaUi;
    }
    //endregion of MetaUi

    //region Module
    private static final String SQL_SELECT_MODULES = "SELECT * FROM %1$s.module WHERE moduleType>=?";
    private static final String SQL_SELECT_MODULES_BY_DB_OBJ = "SELECT * FROM %1$s.module WHERE dbSchema=? AND objName=?";
    //解决 Unknown column 'n.moduleVersion' in 'field list'       Unknown column 'm.allowOp' in 'field list'      Column 'defaultGroupBy' not found.
    private static final String SQL_SELECT_TENANCY_MODULES_LOCALE =
            "SELECT m.moduleCode, %2$s(n.moduleLabel,m.moduleLabel) AS moduleLabel, %2$s(n.shortLabel,m.shortLabel) AS shortLabel, " +
                    "m.moduleType, m.moduleIcon, m.moduleVersion, m.dbSchema, m.objName, m.allowOps, m.moduleUrl, %2$s(n.requiredCreateParam,m.requiredCreateParam) AS requiredCreateParam," +
                    "m.defaultFilter, m.defaultSort, m.status, m.defaultGroupBy, %2$s(n.description,m.description) AS description " +
                    "FROM %1$s.module m " +
                    "   INNER JOIN %1$s.modulei18nt n ON m.moduleCode=n.moduleCode " +
                    "WHERE n.tenantID=? AND n.locale=? AND m.status>0 AND n.licStatus>0";
    private static final String SQL_SELECT_TENANCY_SYSTEM_MODULES_LOCALE =
            "SELECT m.moduleCode, %2$s(n.moduleLabel,m.moduleLabel) AS moduleLabel, %2$s(n.shortLabel,m.shortLabel) AS shortLabel, " +
                    "m.moduleType, m.moduleIcon, m.moduleVersion, m.dbSchema, m.objName, m.allowOps, m.moduleUrl, %2$s(n.requiredCreateParam,m.requiredCreateParam) AS requiredCreateParam," +
                    "m.defaultFilter, m.defaultSort, m.status, m.defaultGroupBy, %2$s(n.description,m.description) AS description " +
                    "FROM %1$s.module m " +
                    "   INNER JOIN %1$s.modulei18nt n ON m.moduleCode=n.moduleCode " +
                    "WHERE n.tenantID=? AND n.locale=? AND m.status>0 AND n.licStatus>0 AND m.moduleType=?";
    private static final String SQL_SELECT_MODULE_ACTIONS_LOCALE =
            "SELECT a.moduleCode, a.actionName, a.actionCode, a.actionType, %2$s(b.displayLabel, a.displayLabel) AS displayLabel, a.displayIcon, a.displayHint, a.ownerOnly, " +
                    "a.incomingTokensRequired,a.executableExpression,a.statusTransition,a.promptType, %2$s(b.description,a.description) AS description " +
                    "FROM %1$s.moduleaction a LEFT JOIN %1$s.moduleactioni18n b ON a.moduleCode=b.moduleCode AND a.actionName=b.actionName " +
                    "WHERE (b.locale=? OR b.locale is null)";
    private static final String SQL_SELECT_MODULE_FLOW_ACTIONS=
            "SELECT * FROM %1$s.moduleflow flow WHERE flow.moduleCode=?";
    private static final String[] supportLocales = new String[]{"zh","zh-Hant","en"};

    //租户多语言模块缓存：tenantID:locale:systemCode => modules
    private ConcurrentMap<String,List<Module>> cachedTenancyModules = new ConcurrentHashMap<>();
    //租户多语言模块缓存字典：tenantID:locale:moduleCode => module
    private ConcurrentMap<String,Module> cachedCodeTenancyModules = new ConcurrentHashMap<>();//locale:Modules:ModuleCode

    private ConcurrentMap<String,List<? extends Module>> cachedModuleAuths = new ConcurrentHashMap<>();

    //租户多语言模块缓存：tenantID:locale => modules
    private ConcurrentMap<String,List<Module>> cachedTenancySystemModules = new ConcurrentHashMap<>();
    @Override
    public void clearCacheOfTenancyModules(int tenantID,final String systemCode){
        for (String locale : supportLocales) {
            String cacheKey = String.join(":",String.valueOf(tenantID),locale,systemCode);
            cachedTenancyModules.remove(cacheKey);
        }
    }
    @Override
    public List<Module> getTenancyModules(int tenantID, final String locale, final String systemCode, boolean reload) throws DataAccessException{
        String cacheKey = String.join(":",String.valueOf(tenantID),locale,systemCode);
        if(cachedTenancyModules.containsKey(cacheKey) && !reload){
            return cachedTenancyModules.get(cacheKey);
        }

        String sql = String.format(SQL_SELECT_TENANCY_MODULES_LOCALE,metadataDbName,isNullFuncName());
        ModuleType resultModuleType = ModuleType.SYSTEM;
        if(BaseUtil.hasText(systemCode)) {
            sql += " AND m.moduleCode LIKE '"+systemCode+".%'";
            resultModuleType = ModuleType.MODULE;
        }
        List<Module> modules = this.jdbcTemplate.query(sql,moduleRowMapper,tenantID,locale);
        assembleModuleActions(modules, locale, systemCode);

        //put into cached map
        String cacheKeyCode = String.join(":",String.valueOf(tenantID),locale);
        for (Module module : modules) {
            cachedCodeTenancyModules.put(cacheKeyCode+":"+module.getModuleCode(),module);
        }

        List<Module> result = resultModuleType == ModuleType.SYSTEM
                ? assembleSystems(modules)
                : assembleModules(modules);
        cachedTenancyModules.put(cacheKey,result);
        return result;
    }
    @Override
    public Module getTenancyModule(int tenantID, final String locale, final String moduleCode){
        String cacheKeyCode = String.join(":",String.valueOf(tenantID),locale,moduleCode);
        Module module = cachedCodeTenancyModules.get(cacheKeyCode);
        if(module == null) {
            getTenancyModules(tenantID,locale,Module.getSystemCode(moduleCode),false);
            module = cachedCodeTenancyModules.get(cacheKeyCode);
        }
        return module;
    }

    @Override
    public List<Module> getModules(final String systemCode, ModuleType moduleType) throws DataAccessException{
        String sql = String.format(SQL_SELECT_MODULES,metadataDbName);
        if(BaseUtil.hasText(systemCode)) {
            sql += " AND moduleCode LIKE '"+systemCode+"%'";
        }
        return this.jdbcTemplate.query(sql,moduleRowMapper, moduleType.getValue());
    }

    @Override
    public Module getModule(final String moduleCode, final String locale) throws DataAccessException{
        Assert.notNull(moduleCode, "moduleCode must not be null");
        String quotedModuleCode = quoteValue(moduleCode);
        String sql = String.format(SQL_SELECT_MODULES,metadataDbName) + " AND moduleCode = "+quotedModuleCode;
        var module = this.jdbcTemplate.queryForObject(sql,moduleRowMapper,ModuleType.SYSTEM.getValue());
        //actions
        String sqlActions = String.format(SQL_SELECT_MODULE_ACTIONS_LOCALE,metadataDbName,isNullFuncName())
                +" AND a.moduleCode = "+quotedModuleCode;
        var actions = this.jdbcTemplate.query(sqlActions,moduleActionRowMapper,supportLocales[0]);
        module.setActions(actions);
        return module;
    }

    @Override
    public Module getModule(final MetaObject metaObject, final String locale) throws DataAccessException{
        Assert.notNull(metaObject, "metaObject must not be null");
        String sql = String.format(SQL_SELECT_MODULES_BY_DB_OBJ,metadataDbName);
        var module = this.jdbcTemplate.queryForObject(sql,moduleRowMapper,metaObject.getDbSchema(),metaObject.getObjName());
        //actions
        String sqlActions = String.format(SQL_SELECT_MODULE_ACTIONS_LOCALE,metadataDbName,isNullFuncName())
                +" AND a.moduleCode = "+quoteValue(module.getModuleCode());
        var actions = this.jdbcTemplate.query(sqlActions,moduleActionRowMapper,supportLocales[0]);
        module.setActions(actions);

        //flowAction
        String sqlflowActions = String.format(SQL_SELECT_MODULE_FLOW_ACTIONS,metadataDbName);
        var flows = this.jdbcTemplate.query(sqlflowActions,moduleFlowRowMapper,module.getModuleCode());
        module.setFlows(flows);
        return module;
    }

    /**
     * 组装所有模块的操作
     * @param modules 模块列表
     * @param locale 语言区域
     * @param systemCode 系统编码
     */
    private void assembleModuleActions(List<? extends Module> modules, final String locale, final String systemCode){
        String sqlActions = String.format(SQL_SELECT_MODULE_ACTIONS_LOCALE,metadataDbName,isNullFuncName());
        if(BaseUtil.hasText(systemCode)) {
            sqlActions += " AND a.moduleCode LIKE '"+systemCode+"%'";
        }
        List<ModuleAction> actions = this.jdbcTemplate.query(sqlActions,moduleActionRowMapper,locale);
        if(actions.isEmpty()) return;

        Map<String,? extends Module> moduleMap = modules.stream().collect(Collectors.toMap(module -> module.getModuleCode(),Function.identity()));
        Map<String,List<ModuleAction>> moduleActionList = actions.stream().collect(Collectors.groupingBy(ModuleAction::getModuleCode));

        for (Map.Entry<String, List<ModuleAction>> action : moduleActionList.entrySet()) {
            Module module = moduleMap.get(action.getKey());
            //解决    Cannot invoke "cloud.mmda.core.metadata.Module.setActions(java.util.List)" because "module" is null
            if (module == null) continue;
            module.setActions(action.getValue());
        }
    }
    /**
     * 组装所有子系统的子模块
     * @param modules 包含子系统的模块列表
     * @return 返回所有子系统
     */
    private List<Module> assembleSystems(List<? extends Module> modules){
        Map<String,Module> moduleMap = new HashMap<>();
        List<Module> result = new ArrayList<>();
        for(Module module : modules){
            if(module.getModuleType() == ModuleType.SYSTEM){
                moduleMap.put(module.getModuleCode(),module);
                result.add(module);
            }
            else  {
                Module parent = moduleMap.get(module.getParentModuleCode());
                if (parent==null) continue;
                parent.addSubModule(module);
                if(module.getModuleType()==ModuleType.MODULE) moduleMap.put(module.getModuleCode(),module);
            }
        }
        return result;
    }

    /**
     * 组装所有1级模块的子项功能
     * @param modules 不包含0级子系统的模块清单
     * @return 一个子系统下所有模块
     */
    private List<Module> assembleModules(List<? extends Module> modules){
        Map<String,Module> moduleMap = new HashMap<>();
        List<Module> result = new ArrayList<>();
        for(Module module : modules){
            //if(module.getModuleType() == ModuleType.SYSTEM) continue;
            if(module.getModuleType() == ModuleType.MODULE){
                moduleMap.put(module.getModuleCode(),module);
                result.add(module);
            }
            else{
                Module parent = moduleMap.get(module.getParentModuleCode());
                if(parent != null)
                parent.addSubModule(module);
            }
        }
        return result;
    }
    //endregion of Module

    @Override
    public List<Module> getTenancySystemModules(int tenantID, final String locale, boolean reload) throws DataAccessException{
        String cacheKey = String.join(":",String.valueOf(tenantID),locale);
        if(cachedTenancySystemModules.containsKey(cacheKey) && !reload){
            return cachedTenancySystemModules.get(cacheKey);
        }

        String sql = String.format(SQL_SELECT_TENANCY_SYSTEM_MODULES_LOCALE,metadataDbName,isNullFuncName());

        List<Module> modules = this.jdbcTemplate.query(sql,moduleRowMapper,tenantID,locale,ModuleType.SYSTEM_VAL);

//        List<Module> result = assembleSystems(modules);
        cachedTenancySystemModules.put(cacheKey,modules);
        return modules;
    }

    //region 约束和索引元数据

    @Override
    public Collection<MetaForeignKey> getForeignKeys(String dbSchema) {
        return List.of();
    }

    @Override
    public Collection<MetaForeignKey> getForeignKeys(MetaObject metaObject) {
        return List.of();
    }

    @Override
    public Collection<MetaIndex> getIndexes(MetaObject metaObject) {
        return List.of();
    }

    //endregion
}
