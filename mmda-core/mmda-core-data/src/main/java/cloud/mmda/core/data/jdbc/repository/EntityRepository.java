package cloud.mmda.core.data.jdbc.repository;

import cloud.mmda.core.data.jdbc.mappers.RowMapperPagedResultSetExtractor;
import cloud.mmda.core.data.pagination.*;
import cloud.mmda.core.data.jdbc.metadata.DbMetadataProvider;
import cloud.mmda.core.data.sql.SqlExpression;
import cloud.mmda.core.data.sql.SqlQuery;
import cloud.mmda.core.entities.*;
import cloud.mmda.core.enums.EnumBitSet;
import cloud.mmda.core.enums.EnumValue;
import cloud.mmda.core.enums.MetaRelationType;
import cloud.mmda.core.metadata.*;

import cloud.mmda.core.metadata.Module;
import cloud.mmda.core.models.ChangeLog;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.NameValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import jakarta.validation.constraints.NotNull;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * 数据仓储抽象基类 T实体类，K主键类
 * 子类继承后，可避免直接写Sql语句
 *
 * 2020.3.28 支持多租户数据分区
 * 2020.11.29 反射性能优化<see>https://www.jianshu.com/p/6bc74577bfa6</see>
 *
 * @author roshion
 * <hr>
 * 客户程序必须提供实现MetadataProvider接口的Bean和SqlBuilder接口的Bean，例如在Application类中提供：
 * <code>
 *  @Bean public MetadataProvider mySqlMetadataProvider(DataSource ds){
		return new MySqlMetadataProvider(ds);
	}
 * </code>
 *
 */
public abstract class EntityRepository<T extends Entity<K>, K> implements Repository<T, K> {
	private static final Log logger = LogFactory.getLog(EntityRepository.class);
	private static final String LAST_MODIFIED = "lastModified";

	protected EntityFactory entityFactory;
	protected DbMetadataProvider metadataProvider;
	/**
	 * 元数据提供者
	 */
	public DbMetadataProvider getMetadataProvider() {
		return metadataProvider;
	}

	/**
	 * 设置元数据提供者
	 * @param metadataProvider 元数据提供者
	 *
	 * @remark 抽象类不支持构造函数Autowired，成员必须是final setter，
	 * <a href="www.baeldung.com/spring-autowired-abstract-class">参考</a>
	 */
	@Autowired
	public final void setMetadataProvider(final DbMetadataProvider metadataProvider) {
		this.metadataProvider = metadataProvider;
	}

	//region 用于根据名称获取数据源
//	@Autowired
//	protected  ConfigurableListableBeanFactory beanFactory;
//	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException{
//		EntityRepository.beanFactory = beanFactory;
//	}
	@Autowired
	private ApplicationContext applicationContext;

	//endregion
	public Module getModule(final String locale){
		return metadataProvider.getModule(getMetaObject(),locale);
	}
	protected final Date today(){
		return Date.valueOf(LocalDate.now());
	}
	protected final Timestamp now(){
		return Timestamp.valueOf(LocalDateTime.now());
	}
	/**
	 * SqlExpression表达式构建器，避免直接写Sql条件查询子句，使用此构建
	 * 不能使用成员变量，有并发问题
	 */
	public SqlExpression.SqlExpressionBuilder expressionBuilder() {
		return SqlExpression.builder(metaObj);
	}

	protected final JdbcTemplate jdbcTemplate;
	/**
	 * 实体T的class
	 */
	protected Class<T> tClass;
	protected Class<K> kClass;

	@Override
	public Class<T> getTClass(){
		return tClass;
	}
	@Override
	public Class<K> getKClass(){
		return kClass;
	}

	protected int keyColNum;
	protected String tClassName;
	protected String tDbName;//default null means currDbName of DataSource
	@Override
	public String getTClassName(){ return  tClassName; }
	/**
	 * 元数据对象
	 */
	protected MetaObject metaObj;
	protected MetaCol uniqueKeyCol;
	protected String defaultSort;

	private  List<MetaCol> enumCols;
	private boolean hasEnumCols;

	private ConcurrentMap<MetaCol,MetaRelation> refColRelations;

//	protected List<PropertyDescriptor> keyPropertyDescriptors;
//	protected ConcurrentHashMap<String,PropertyDescriptor> propertyDescriptors;

	protected MetaObjectAccess objAccess;

	/**
	 * 行映射器，子类尽量不直接使用
	 */
	protected RowMapper<T> rowMapper;
	protected RowMapper<K> keyMapper;
	
	//SqlQuery缓存
	private SqlExpression keyCondition;
	private ConcurrentHashMap<String, SqlQuery> cachedQueries;
	private final static char BY = '/';
	private final static String BY_KEY = BY+"KEY";
	private final static String BY_KEY_AND = BY_KEY+" AND ";
	/**
	 * 构造函数
	 * @param ds 数据源
	 */
//	@Autowired
	public EntityRepository(DataSource ds) {
		this.jdbcTemplate = new JdbcTemplate(ds);
	}

	/**
	 * 根据实体类命名空间推测其数据库名称，未获取元数据对象前需要
	 * @return 实体类所属的数据库名
	 */
	private String supposeTDbName(){
		String[] names = tClass.getName().split("\\.");
		if("core".equals(names[2]))
			return metadataProvider.getCurrentDbName();
		else
			return metadataProvider.getDbPrefix() +names[2];
	}
	/**
	 * 初始化函数，子类必须赋值keyGetter=(t)->t.key
	 */
	@PostConstruct
	protected void initialize() {
		ParameterizedType pt = (ParameterizedType)getClass().getGenericSuperclass();
		tClass = (Class<T>)pt.getActualTypeArguments()[0];
		tDbName = supposeTDbName();
		tClassName = tClass.getSimpleName();
		kClass= (Class<K>)pt.getActualTypeArguments()[1];

		try {
			metaObj = StringUtils.isEmpty(tDbName)
					? metadataProvider.getMetaObject(tClassName)
					: metadataProvider.getMetaObject(tDbName,tClassName);
			refColRelations = metaObj.buildColRelationMap(MetaRelationType.REF);
		}catch (EmptyResultDataAccessException e){
			throw e;
		}

		if(BaseUtil.hasText(metaObj.getUniqueKey())) {
			uniqueKeyCol = metaObj.getCol(metaObj.getUniqueKey());
		}

		enumCols = metaObj.getCols().stream()
				.filter(col->col.getRelationType()==MetaRelationType.ENUM)
				.collect(Collectors.toList());
		hasEnumCols = !enumCols.isEmpty();
		//默认排序方式
		if(metaObj.hasCol(LAST_MODIFIED)){
			defaultSort = LAST_MODIFIED+" DESC";
		}
		else{
			defaultSort = metaObj.getKeyCols().stream()
					.map(col -> "t."+col.getColName())
					.collect(Collectors.joining(","));
		}
		rowMapper = createRowMapper();
		keyCondition = SqlExpression.ofAllEqual(metaObj.getKeyCols());
		cachedQueries = new ConcurrentHashMap<String,SqlQuery>();

		//实体
//		BeanWrapperImpl beanWrapper = new BeanWrapperImpl(tClass);
//		propertyDescriptors = new ConcurrentHashMap<>();
//		for (MetaCol col : metaObj.getCols()){
//			PropertyDescriptor propertyDescriptor = beanWrapper.getPropertyDescriptor(col.getColName());
//			propertyDescriptors.put(col.getColName(),propertyDescriptor);
//		}

		//改为字节码，性能优化
		objAccess = new MetaObjectAccess(metaObj,tClass,kClass);

		//主键
		keyColNum = metaObj.getKeyCols().size();
		if(keyColNum==0){
			if(logger.isErrorEnabled()) logger.error("实体"+tClassName+"未定义数据表主键");
		}

		//复合主键
		if(keyColNum>1){
//			beanWrapper = new BeanWrapperImpl(kClass);
//			keyPropertyDescriptors = new ArrayList<>();
//			for (MetaCol keyCol: metaObj.getKeyCols()) {
//				PropertyDescriptor propertyDescriptor = beanWrapper.getPropertyDescriptor(keyCol.getColName());
//				keyPropertyDescriptors.add(propertyDescriptor);
//			}

			keyMapper = new RowMapper<K>() {
				@Override
				public K mapRow(ResultSet rs, int rowNum) throws SQLException {
					K k = null;
					try{
						k = kClass.newInstance();
						List<MetaCol> keyCols = metaObj.getKeyCols();
						for(int i=0; i< keyColNum; i++){
							MetaCol col = keyCols.get(i);
							int columnIndex=i+1;
							Object value = rs.getObject(columnIndex);
							// 自动处理 BigInteger 转 Long
							if (value instanceof BigInteger) {
								value = ((BigInteger) value).longValue();
							}
							objAccess.setKeyProperty(k, col.getColName(), value);
//							objAccess.setKeyProperty(k, col.getColName(),rs.getObject(columnIndex));
//							keyPropertyDescriptors.get(i)
//									.getWriteMethod()
//									.invoke(k, rs.getObject(i));
						}
					}
					catch (Exception ex){
						logger.error(ex);
						//log
					}
					return k;
				}
			};
		}
	}

	public  ConcurrentMap<MetaCol,MetaRelation> getRefColRelations(){
		return refColRelations;
	}
	/**
	 * 是否有唯一键字段
	 * @return
	 */
	public final boolean hasUniqueKeyCol(){
		//解决一直返回false的问题
		//return uniqueKeyCol!=null;
		return getUniqueKeyCol()!=null;
	}
	public MetaCol getUniqueKeyCol(){
		return uniqueKeyCol;
	}
	@Override
	public Object getColValue(final T t, MetaCol col){
		return objAccess.getProperty(t,col);
	}
	@Override
	public void setColValue(final T t, MetaCol col, Object v){
		objAccess.setProperty(t,col,v);
	}
	@Override
	public Object getUniqueKeyValue(final T t){
		if(uniqueKeyCol==null) return null;
		return objAccess.getProperty(t,uniqueKeyCol);
	}
	@Override
	public void setUniqueKeyValue(final T t, final Object v){
		if(uniqueKeyCol==null) return;
		objAccess.setProperty(t,uniqueKeyCol,v);
	}
	@Override
	public Object getRelativeValue(T t, MetaRelation rel){
//		return objAccess.getProperty(t,rel.getRelationName());
		return objAccess.getProperty(t,rel);
	}
	@Override
	public void setRelativeValue(T t, MetaRelation rel, Object v){
//		objAccess.setProperty(t,rel.getRelationName(),v);
		objAccess.setProperty(t,rel,v);
	}

	@Override
	public Object getPropertyValue(T t, String propName){
		try{
			return objAccess.getProperty(t,propName);
		}
		catch (Exception e){
			logger.error(e);
			return null;
		}
	}
	@Override
	public void setPropertyValue(T t, String propName, Object v){
		try{
			objAccess.setProperty(t,propName,v);
		}
		catch (Exception e){
			logger.error(e);
		}
	}
	@Override
	public Object getColText(T t, MetaCol col) throws IllegalAccessException, InvocationTargetException{
		Object value = null;
		switch (col.getRelationType()){
			case MetaRelationType.HAS_MANY:
				break;
			case MetaRelationType.HAS_ONE:
				MetaRelation rel = metaObj.getRelation(col.getRelationName());
				if(rel!=null) {
					//不得不使用反射
					Object relativeObj = getRelativeValue(t,rel);
					PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(relativeObj.getClass(),rel.getLabelColName());
					value = propertyDescriptor.getReadMethod().invoke(relativeObj);
					//TODO 如果有额外的显示字段，拼接一下
				}
				break;
			case MetaRelationType.ENUM:
			case MetaRelationType.REF:
				value = t.getRefProperty(col.getColName());
				break;
			default:
				value = getColValue(t,col); //getPropertyValue(t,col.getColName());
				break;
		}
		return value==null? null : value;
	}
	@Override
	public Object getColText(T t, String colName) throws IllegalAccessException, InvocationTargetException{
		Objects.requireNonNull(colName);
		MetaCol col = metaObj.getCol(colName);
		Objects.requireNonNull(col,String.format("MetaCol %s not exists",colName));
		return getColText(t,col);
	}

	@Override
	public T clone(T source, boolean includeRelation) {
		T target = create();
		try{
//			metaObj.copy(source,target,includeRelation);
		}
		catch (Exception ex){
			//ignore
		}
		return target;
	}
	@Override
	public void copy(MetaObject fromMetaObj, Object from, T to, boolean includeRelation){
		try{
//			metaObj.copy(fromMetaObj,from,to,includeRelation);
		}
		catch (Exception ex){
			//ignore
		}
	}

	@Override
	public T assembleEnumProperties(T t){
		Objects.requireNonNull(t);

		//若没有枚举属性或者已经组装过了，不再组装
		if(!hasEnumCols || t.isAssembled(Entity.ASSEMBLE_ENUM)) return t;

		for(MetaCol col : enumCols){
			try{
				Object value = getColValue(t,col); //getPropertyValue(t,col.getColName());
				if(value==null) continue;

				if(value instanceof EnumValue){
					String v = ((EnumValue)value).getValue().toString();
					MetaEnum metaEnum = col.getMetaEnum();
					NameValue<String,String> nameValue = metaEnum.getParsedEnumMap().get(((EnumValue) value).getValue().toString());
					if(nameValue!=null){
						t.setRefProperty(col.getColName(),nameValue.getValue());
					}
				}
			}
			catch (Exception e){
				logger.error(tClassName+"设置列"+col.getColName()+"枚举引用属性异常",e);
			}
		}
		t.setAssembled(Entity.ASSEMBLE_ENUM);
		return t;
	}

	
	/**
	 * 子类必须实现的行映射器
	 * @return
	 */
	protected abstract RowMapper<T> createRowMapper();

	/**
	 * 根据SqlQuery查询对象创建Jdbc底层的PreparedStatement
	 * @param sqlQuery 查询
	 * @param argValues 参数列表
	 * @return
	 */
	private PreparedStatementCreator createStatementCreator(SqlQuery sqlQuery, List<Object> argValues){
		return new PreparedStatementCreator(){

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				List<String> generatedKeys = sqlQuery.getGeneratedKeys();

				PreparedStatement ps = generatedKeys.isEmpty()
						?con.prepareStatement(sqlQuery.getSql())
						:con.prepareStatement(sqlQuery.getSql(),generatedKeys.toArray(new String[0]));
				List<Integer> argTypes = sqlQuery.getArgJdbcTypes();
				for(int i=0; i<argTypes.size(); i++){
					Object argValue = argValues.get(i);
					int argType = argTypes.get(i);
					if(argValue==null)
						ps.setNull(i+1, argType);
					else
						ps.setObject(i+1, argValue, argTypes.get(i));
				}

				return ps;
			}

		};
	}

	private String emptyIfNull(String s){
		return s==null?"":s;
	}
	private void putSqlQueryToCache(String name, SqlQuery q){
		cachedQueries.put(name, q);
	}

	/**
	 * 获取元对象
	 * @return
	 * @remarks 不能使用final修饰，否则会影响Service层Proxy
	 */
	@Override
	public MetaObject getMetaObject(){return metaObj;}
	@Override
	public MetaObject getMetaObject(String objName){
		return metadataProvider.getMetaObject(objName);
	}
	@Override
	public MetaObject getMetaObject(String dbSchema,String objName){
		return metadataProvider.getMetaObject(dbSchema, objName);
	}
	@Override
	public MetaObjectAccess<T,K> getMetaObjectAccess(){
		return objAccess;
	}
	public Sort getDefaultSort(){
		return Sort.parse(defaultSort);
	}


	protected void beforeInsert(final T t){
		//如果是可计算的实体，存入数据库之前计算
		if(t instanceof Computable){
			((Computable)t).compute();
		}
		//留给子类实现其他逻辑
	}

	protected SqlQuery getInsertQuery(T t){
		SqlQuery insertQuery = cachedQueries.get(SqlQuery.INSERT);
		if(insertQuery==null){
			SqlExpression attributes = buildExpression(metaObj.getInsertableCols(),t);
			insertQuery = SqlQuery.builder(metadataProvider)
					.insert(metaObj)
					.values(attributes)
					.build();
			putSqlQueryToCache(SqlQuery.INSERT, insertQuery);
		}
		return insertQuery;
	}
	@Override
	public int insert(final T t) throws DataAccessException {
		beforeInsert(t);

		//insert
		SqlQuery insertQuery = getInsertQuery(t);
		List<Object> argValues = toArgList(t, metaObj.getInsertableCols());
		
		int r = 0;
		List<String> generatedKeys = insertQuery.getGeneratedKeys();
		if (!generatedKeys.isEmpty()) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			r = jdbcTemplate.update(createStatementCreator(insertQuery,argValues), keyHolder);
			if (r > 0) {
				try{
					if(generatedKeys.size()>1){
						for(String key : generatedKeys){
							setPropertyValue(t,key,keyHolder.getKeys().get(key));
						}
					}
					else{
						String key = generatedKeys.get(0);
						MetaCol keyCol = metaObj.getCol(key);
						if(keyCol.getDataType().isInteger())
							setPropertyValue(t,key,keyHolder.getKey());
						else
							setPropertyValue(t,key,MetaDataType.parseDefaultValue(keyHolder.getKey().toString(),keyCol.getDataType()));
					}
				}
				catch (Exception ex){
					logger.error("插入"+tClassName+"数据后自增ID赋值错误", ex);
				}
				/* 有高并发问题
				beanWrapper.setBeanInstance(t);
				for(String key : generatedKeys){
					beanWrapper.setPropertyValue(key, keyHolder.getKey());
				}
				*/
			}
		}
		else{
			r = jdbcTemplate.update(createStatementCreator(insertQuery,argValues));
		}
		if(r==0){
			throw new IncorrectResultSizeDataAccessException(1,r);
		}
		t.setEntityState(EntityState.DEFAULT);
		return r;
	}


	@Override
	public int[] insertMany(final Collection<T> tCollection) throws DataAccessException{
		BaseUtil.requireNoneEmpty(tCollection, "tCollection");

		Optional<T> first = tCollection.stream().findFirst();
		SqlQuery insertQuery = getInsertQuery(first.get());

		if(insertQuery.getGeneratedKeys().isEmpty()){
			List<Object[]> batchArgs = buildBatchInsertArgs(tCollection);
			return this.jdbcTemplate.batchUpdate(insertQuery.getSql(), batchArgs, insertQuery.getArgJdbcTypeArray());
		}
		else{
			return tCollection.stream()
					.mapToInt(t -> insert(t))
					.toArray();
		}
	}


	protected Object toArgValue(Object arg){
		if (arg instanceof EnumValue enumValue) {
			return enumValue.getValue();
		} else if (arg instanceof EnumBitSet enumBitSet) {
			return enumBitSet.getValue();
		} else {
			return arg;
		}
	}

	/**
	 * 将属性转化为对象数组
	 * 
	 * @param o
	 *            主键值或者实体对象
	 * @return
	 */
	protected Object[] toKeyArgArray(Object o) {
		Object[] argArray = new Object[keyColNum];
		return  keyColNum == 1
				?objAccess.argArrayOfSingleKey(o)
				:objAccess.argArrayOfCompositeKey(o);

	}


	protected List<Object> toKeyArgList(Object o) {
		List<Object> args = new ArrayList<Object>();
		List<MetaCol> keyCols = metaObj.getKeyCols();
		try{
			// 如果单主键
			if (keyColNum == 1) {
				Object keyVal = o;
				if(tClass.isInstance(o)){
					//传入整个实体对象，要取得单个主键
					keyVal = getPropertyValue((T)o,keyCols.get(0).getColName());
				}
				args.add(keyVal);
			}
			else{
				// 组合键
				for (int i=0; i<keyColNum; i++) {
					MetaCol col = keyCols.get(i);
					Object keyVal = objAccess.getKeyProperty(o,col.getColName());
					args.add(toArgValue(keyVal));
				}
			}
		}
		catch (Exception ex){
			if(logger.isErrorEnabled()) logger.error(tClassName+"主键转为参数列表出错", ex);
		}


		return args;
	}
	/**
	 * 将对象属性转化为对象列表
	 * @param o
	 *            实体对象
	 * @param cols
	 *            字段列表
	 * @return
	 */
	protected List<Object> toArgList(T o, List<MetaCol> cols) {
		List<Object> args = new ArrayList<Object>();
		try{
			for (MetaCol col : cols) {
				Object val = getColValue(o,col);
				args.add(toArgValue(val));
			}
		}
		catch (Exception ex){
			if(logger.isErrorEnabled()) logger.error(tClassName+"对象转为参数列表出错", ex);
		}

		return args;
	}
	
//	@Override
//	public K findKey(SqlExpression condition) throws DataAccessException {
//		List<MetaCol> keyCols = metaObj.getKeyCols();
//		SqlQuery findQuery = sqlBuilder.from(metaObj)
//				.where(condition)
//				.select(keyCols)
//				.build();
//		return (K)jdbcTemplate.queryForObject(findQuery.getSql(), findQuery.getArgValueArray(),kclazz);
//	}

	protected SqlQuery getFindByKeyQuery(K k){
		String queryName=SqlQuery.SELECT+BY_KEY;
		SqlQuery findQuery = cachedQueries.get(queryName);
		if(findQuery==null){
			findQuery = SqlQuery.builder(metadataProvider)
					.from(metaObj)
					.select().withoutRowNum()
					.where(keyCondition)
					.build();

			putSqlQueryToCache(queryName, findQuery);
		}
		return findQuery;
	}

	@Override
	public T find(K k) throws DataAccessException {
		SqlQuery findQuery = getFindByKeyQuery(k);
		String sql = findQuery.getSql();
		if(logger.isDebugEnabled()) logger.debug(sql);
		return jdbcTemplate.queryForObject(sql, toKeyArgArray(k), rowMapper);
	}

	protected SqlQuery getFindByUniqueKeyQuery(){
		String queryName=SqlQuery.SELECT+BY+metaObj.getUniqueKey();
		SqlQuery findQuery = cachedQueries.get(queryName);
		if(findQuery==null){
			findQuery = SqlQuery.builder(metadataProvider)
					.from(metaObj)
					.select().withoutRowNum()
					.where(SqlExpression.ofEqual(uniqueKeyCol))
					.build();

			putSqlQueryToCache(queryName, findQuery);
		}
		return findQuery;
	}

	@Override
	public T findByUniqueKey(String uniqueKey) throws DataAccessException {
		if(uniqueKeyCol == null) throw new InvalidDataAccessResourceUsageException("没有定义唯一索引列");
		SqlQuery findQuery = getFindByUniqueKeyQuery();
		String sql = findQuery.getSql();
		if(logger.isDebugEnabled()) logger.debug(sql);
		return jdbcTemplate.queryForObject(sql, rowMapper, uniqueKey);
	}

	@Override
	public SqlExpression buildExpression(Map<String, Object> attributes) {
		List<MetaCol> cols = new ArrayList<>();
		List<Object> values = new ArrayList<>();

		attributes.forEach((name, value) -> {
			MetaCol col = metaObj.getCol(name);
			if(col!=null){
				cols.add(col);
				values.add(value);
			}
		});
		return SqlExpression.ofAllEqual(cols,values);
	}
	/**
	 * 将实体T对象构建为属性name=value的表达式
	 */
	public SqlExpression buildExpression(T t){
		return buildExpression(metaObj.getCols(),t);
	}
	/**
	 * 将列cols构建为属性name=value的表达式，value从实体T对象中获取
	 * @param cols 列集合
	 * @param t 实体对象
	 * @return
	 */
	protected SqlExpression buildExpression(List<MetaCol> cols, T t){
		List<Object> values = new ArrayList<>();
		try{
			for(MetaCol col : cols){
				Object value = getColValue(t,col);//getPropertyValue(t, col.getColName());
				values.add(value);
			}
		}
		catch (Exception ex){
			if(logger.isErrorEnabled()) logger.error(tClassName+"构建Sql表达式错误", ex);
		}
		return SqlExpression.ofAllEqual(cols,values);
	}

	@Override
	public String buildCondition(SqlExpression condition, char placeholder) {
		if(placeholder == '?') placeholder = metadataProvider.getParamPlaceholder();
		return condition.buildExpression(col -> "t."+col.getColName(),placeholder);
	}

	//	/**
//	 * 根据cols列集合构建一个values列表，value从实体T对象中获取
//	 * @param cols 列集合
//	 * @param t 实体对象
//	 * @return
//	 */
//	protected List<Object> buildValues(List<MetaCol> cols, T t){
//		List<Object> values = new ArrayList<Object>();
//		beanWrapper.setBeanInstance(t);
//		for(MetaCol col : cols){
//			Object value = beanWrapper.getPropertyValue(col.getColName());
//			values.add(value);
//		}
//		return values;
//	}
	

	protected SqlQuery getFindFirstQuery(SqlExpression condition){
		SqlQuery sqlQuery;
		if(condition.isFlat()){
			condition.buildFlatConditions();
			String queryName = SqlQuery.SELECT+BY+condition.getCondIds();
			sqlQuery = cachedQueries.get(queryName);
			if(sqlQuery==null){
				sqlQuery = SqlQuery.builder(metadataProvider)
						.from(metaObj)
						.select().withoutRowNum()
						.where(condition)
						.build();
				putSqlQueryToCache(queryName, sqlQuery);
			}
		}
		else{
			sqlQuery = SqlQuery.builder(metadataProvider)
					.from(metaObj)
					.select().withoutRowNum()
					.where(condition)
					.build();
		}
		return sqlQuery;
	}
	@Override
	public T findFirst(SqlExpression condition) throws DataAccessException {
		SqlQuery sqlQuery = getFindFirstQuery(condition);
		String sql = sqlQuery.getSqlLimit(1, metadataProvider);
		if(logger.isDebugEnabled()) logger.debug(sql);
		return jdbcTemplate.queryForObject(sql, condition.getArgValues().toArray(), rowMapper);
	}
	@Override
	public T findFirst(SqlExpression condition, Sort sort) throws DataAccessException {
		SqlQuery sqlQuery = getFindFirstQuery(condition);

		String sql = sqlQuery.getSqlLimit(1, sort.toString(), metadataProvider);
		if(logger.isDebugEnabled()) logger.debug(sql);
		return jdbcTemplate.queryForObject(sql, condition.getArgValues().toArray(), rowMapper);
	}
	@Override
	public T findFirst(String condition, Object...args) throws DataAccessException{
		SqlQuery findAllQuery = getFindAllQuery();
		String sql = findAllQuery.getSqlWithLimit(condition,1, metadataProvider);
		if(logger.isDebugEnabled()) logger.debug(sql);
		return jdbcTemplate.queryForObject(sql, args, rowMapper);
	}
	@Override
	public T findFirst(String condition, Sort sort, Object...args) throws DataAccessException{
		SqlQuery findAllQuery = getFindAllQuery();
		String sql = findAllQuery.getSqlWithLimit(condition,1, sort.toString(), metadataProvider);
		if(logger.isDebugEnabled()) logger.debug(sql);
		return jdbcTemplate.queryForObject(sql, args, rowMapper);
	}
	protected SqlQuery getFindScalarQuery(String colName, SqlExpression condition){
		SqlQuery findQuery;
		if(condition.isFlat()){
			condition.buildFlatConditions();
			String queryName = SqlQuery.SELECT+colName+BY+condition.getCondIds();
			findQuery = cachedQueries.get(queryName);
			if(findQuery==null){
				findQuery = SqlQuery.builder(metadataProvider)
						.from(metaObj)
						.select(metaObj.getCol(colName)).withoutRowNum()
						.where(condition)
						.build();
				putSqlQueryToCache(queryName, findQuery);
			}
		}
		else{
			findQuery = SqlQuery.builder(metadataProvider)
					.from(metaObj)
					.select(metaObj.getCol(colName)).withoutRowNum()
					.where(condition)
					.build();
		}
		return findQuery;
	}

	@Override
	public <U> U findScalar(String colName, SqlExpression condition, Class<U> requiredType) throws DataAccessException {
		SqlQuery findQuery = getFindScalarQuery(colName, condition);
		String sql = findQuery.getSqlLimit(1, metadataProvider);
		if(logger.isDebugEnabled()) logger.debug(sql);
		return jdbcTemplate.queryForObject(sql, condition.getArgValues().toArray(), requiredType);
	}

	/**
	 * 获取所有记录的SqlQuery对象缓存
	 * select * from T
	 * @return SqlQuery
	 */
	protected SqlQuery getFindAllQuery(){
		String queryName=SqlQuery.SELECT;
		SqlQuery findAllQuery = cachedQueries.get(queryName);
		if(findAllQuery==null){
			findAllQuery = SqlQuery.builder(metadataProvider)
					.from(metaObj)
					.select()
					.build();
			putSqlQueryToCache(queryName, findAllQuery);
		}
		return findAllQuery;
	}
		
	@Override
	public List<T> findAll() throws DataAccessException {
		SqlQuery findAllQuery = getFindAllQuery();
		String sql = findAllQuery.getSql();
		if(logger.isDebugEnabled()) logger.debug(sql);
		if(findAllQuery.isWithRowNum())
			return jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<>(rowMapper));
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public List<T> findAll(Sort sort) throws DataAccessException {
		SqlQuery findAllQuery = getFindAllQuery();
		String sql = findAllQuery.getSqlWith(sort);
		if(logger.isDebugEnabled()) logger.debug(sql);
		if(findAllQuery.isWithRowNum())
			return jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<>(rowMapper));
		return jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public List<T> findAllIn(final String field,final String list,boolean ordered) throws DataAccessException{
		SqlQuery findAllQuery = getFindAllQuery();
		String fieldName = findAllQuery.getAliasDotName(metaObj.getObjName(),field);
		String condition = fieldName + " IN(" + list +")";
		if(ordered && metadataProvider.supportOrderByField()) condition += " ORDER BY FIELD("+fieldName+"," + list +")";
		String sql = findAllQuery.getSqlWith(condition);
		if(logger.isDebugEnabled()) logger.debug(sql);
		if(findAllQuery.isWithRowNum())
			return jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<>(rowMapper));
		return  jdbcTemplate.query(sql,rowMapper);
	}
	@Override
	public List<T> findAllIn(final String field,final String list,boolean ordered, final String condition, Object... args) throws DataAccessException{
		SqlQuery findAllQuery = getFindAllQuery();
		String fieldName = findAllQuery.getAliasDotName(metaObj.getObjName(),field);
		String cond = SqlExpression.and(condition, fieldName + " IN(" + list +")");
		if(ordered && metadataProvider.supportOrderByField()) cond += " ORDER BY FIELD("+fieldName+"," + list +")";
		String sql = findAllQuery.getSqlWith(cond);
		if(logger.isDebugEnabled()) logger.debug(sql);
		if(findAllQuery.isWithRowNum())
			return jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<>(rowMapper),args);
		return  jdbcTemplate.query(sql,rowMapper,args);
	}

	@Override
	public PagedList<T> findAllNotIn(final Paginator paginator, final String field, final String list) throws DataAccessException{
		SqlQuery findAllQuery = getFindAllQuery();
		String fieldName = findAllQuery.getAliasDotName(metaObj.getObjName(),field);

		String condition = fieldName + " NOT IN(" + list +")";

		int rc = count(condition);
		paginator.setRecordCount(rc);

		String sql = findAllQuery.getSqlWith(condition, paginator,metadataProvider);
		if(logger.isDebugEnabled()) logger.debug(sql);
		List<T> data = jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<>(rowMapper, paginator));
		return new PagedList<T>(data, paginator);
	}
	@Override
	public PagedList<T> findAllNotIn(final Paginator paginator, final String field, final String list, final String condition, Object... args) throws DataAccessException{
		SqlQuery findAllQuery = getFindAllQuery();
		String fieldName = findAllQuery.getAliasDotName(metaObj.getObjName(),field);

		String cond = SqlExpression.and(condition, fieldName + " NOT IN(" + list +")");

		int rc = count(cond,args);
		paginator.setRecordCount(rc);

		String sql = findAllQuery.getSqlWith(cond, paginator,metadataProvider);
		if(logger.isDebugEnabled()) logger.debug(sql);
		List<T> data = jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<>(rowMapper, paginator), args);
		return new PagedList<T>(data, paginator);
	}
	@Override
	public PagedList<T> findAll(Paginator paginator) throws DataAccessException {
		int rc = count();
		paginator.setRecordCount(rc);
		SqlQuery findAllQuery = getFindAllQuery();
		String sql = findAllQuery.getSqlWith(paginator, metadataProvider);
		if(logger.isDebugEnabled()) logger.debug(sql);
		List<T> data = jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<T>(rowMapper, paginator));
		return new PagedList<T>(data, paginator);
	}

	protected SqlQuery getFindAllByQuery(SqlExpression condition){
		SqlQuery sqlQuery;
		if(condition.isFlat()){//可缓存
			condition.buildFlatConditions();
			String queryName = SqlQuery.SELECT+BY+condition.getCondIds();
			sqlQuery = cachedQueries.get(queryName);
			if(sqlQuery==null){
				sqlQuery = SqlQuery.builder(metadataProvider)
						.from(metaObj)
						.select()
						.where(condition)
						.build();
				putSqlQueryToCache(queryName, sqlQuery);
			}
		}
		else{//嵌套条件临时计算
			sqlQuery = SqlQuery.builder(metadataProvider)
					.from(metaObj)
					.select()
					.where(condition)
					.build();
		}
		return sqlQuery;
	}
	@Override
	public List<T> findAllBy(SqlExpression condition) throws DataAccessException {
		SqlQuery sqlQuery = getFindAllByQuery(condition);//TODO 与getFindFirstQuery统一
		String sql = sqlQuery.getSql();
		if(logger.isDebugEnabled()) logger.debug(sql);
		if(sqlQuery.isWithRowNum())
			return jdbcTemplate.query(sql, condition.getArgValues().toArray(), new RowMapperPagedResultSetExtractor<>(rowMapper));
		return jdbcTemplate.query(sql, condition.getArgValues().toArray(), rowMapper);
	}

	@Override
	public List<T> findAllBy(SqlExpression condition, @NotNull Sort sort) throws DataAccessException {
		BaseUtil.requireNonNull(sort, "sort");

		SqlQuery sqlQuery = getFindAllByQuery(condition);
		String sql = sqlQuery.getSqlWith(sort);
		if(logger.isDebugEnabled()) logger.debug(sql);
		if(sqlQuery.isWithRowNum())
			return jdbcTemplate.query(sql, condition.getArgValues().toArray(), new RowMapperPagedResultSetExtractor<>(rowMapper));
		return jdbcTemplate.query(sql, condition.getArgValues().toArray(), rowMapper);
	}

	@Override
	public List<T> findAllBy(String condition, Object...args) throws DataAccessException{
		SqlQuery sqlQuery = getFindAllQuery();
		String sql = sqlQuery.getSqlWith(condition);
		if(logger.isDebugEnabled()) logger.debug(sql);
		if(sqlQuery.isWithRowNum())
			return jdbcTemplate.query(sql, args, new RowMapperPagedResultSetExtractor<>(rowMapper));
		return jdbcTemplate.query(sql, args, rowMapper);
	}
	@Override
	public List<T> findAllBy(final String condition, final Sort sort, Object...args) throws DataAccessException{
		BaseUtil.requireNonNull(condition,"condition");
		BaseUtil.requireNonNull(sort,"sort");

		SqlQuery sqlQuery = getFindAllQuery();
		String sql = sqlQuery.getSqlWith(condition, sort);
		if(logger.isDebugEnabled()) logger.debug(sql);
		if(sqlQuery.isWithRowNum())
			return jdbcTemplate.query(sql, args, new RowMapperPagedResultSetExtractor<>(rowMapper));
		return jdbcTemplate.query(sql, args, rowMapper);
	}

	@Override
	public PagedList<T> findAllBy(final Paginator paginator, final SqlExpression condition) throws DataAccessException {
		BaseUtil.requireNonNull(paginator,"pager");
		BaseUtil.requireNonNull(condition,"condition");

		SqlQuery sqlQuery = getFindAllByQuery(condition);

		int rc = count(condition);
		paginator.setRecordCount(rc);

		String sql = sqlQuery.getSqlWith(paginator, metadataProvider);
		Object[] argValueArray = condition.getArgValues().toArray();
		if(logger.isDebugEnabled()) logger.debug(sql);
		List<T> data = jdbcTemplate.query(sql, argValueArray, new RowMapperPagedResultSetExtractor<>(rowMapper, paginator));
		return new PagedList<T>(data, paginator);
	}
	

	@Override
	public PagedList<T> findAllBy(final Paginator paginator, final String condition, Object...args) throws DataAccessException {
		BaseUtil.requireNonNull(paginator,"pager");
		BaseUtil.requireNonNull(condition,"condition");

		int rc = count(condition, args);
		paginator.setRecordCount(rc);

		SqlQuery sqlQuery = getFindAllQuery();
		String sql = sqlQuery.getSqlWith(condition, paginator, metadataProvider);
		if(logger.isDebugEnabled()) logger.debug(sql);
		List<T> data = jdbcTemplate.query(sql, args, new RowMapperPagedResultSetExtractor<>(rowMapper, paginator));
		return new PagedList<T>(data, paginator);
	}

	protected SqlQuery getFindAllKeysQuery(){
		String queryName=SqlQuery.SELECT+"KEYS ";
		SqlQuery findAllKeysQuery = cachedQueries.get(queryName);
		if(findAllKeysQuery==null){
			findAllKeysQuery = SqlQuery.builder(metadataProvider)
					.from(metaObj)
					.select(metaObj.getKeyCols())
					.withoutRowNum()
					.build();
			putSqlQueryToCache(queryName, findAllKeysQuery);
		}
		return findAllKeysQuery;
	}


	@Override
	public List<K> findAllKeysBy(String condition, Object...args) throws DataAccessException{
		SqlQuery sqlQuery = getFindAllKeysQuery();
		String sql = sqlQuery.getSqlWith(condition);
		if(logger.isDebugEnabled()) logger.debug(sql);
		if(keyColNum>1)
			return jdbcTemplate.query(sql, args, keyMapper);
		return jdbcTemplate.queryForList(sql,args,kClass);
	}
	@Override
	public List<K> findAllKeysBy(SqlExpression condition) throws DataAccessException{
		SqlQuery sqlQuery = getFindAllKeysQuery();
		String where = SqlExpression.buildExpression(condition, c -> sqlQuery.getAliasDotName(c), metadataProvider.getParamPlaceholder());
		String sql = sqlQuery.getSqlWith(where);
		if(logger.isDebugEnabled()) logger.debug(sql);
		condition.buildArgs(c -> sqlQuery.getAliasDotName(c), SqlQuery.QM,false);
		Object[] args = condition.getArgValues().toArray();
		if(keyColNum>1){
			int[] argJdbcTypes = condition.getArgTypes().stream().mapToInt(metadataProvider::getJdbcType).toArray();
			return jdbcTemplate.query(sql, args, argJdbcTypes, keyMapper);
		}
		return jdbcTemplate.queryForList(sql,args,kClass);
	}


	protected SqlQuery getSearchAllQuery(boolean onlyString){
		String queryName="SEARCH ALL"+ (onlyString?" STRING":"");
		SqlQuery searchAllQuery = cachedQueries.get(queryName);
		if(searchAllQuery==null){
			List<MetaRelation> oneRelations = metaObj.getRelations().stream()
					.filter(relation -> relation.getRelationType()== MetaRelationType.HAS_ONE)
					.collect(Collectors.toList());
			searchAllQuery = SqlQuery.builder(metadataProvider)
					.from(metaObj)
					.joinAll(oneRelations,true)
					.searchAll(onlyString)
					.build();
			putSqlQueryToCache(queryName, searchAllQuery);
		}
		return searchAllQuery;
	}

	/**
	 * 根据dbSchema获取数据源
	 * @param dbSchema 例如mmda_crm
	 * @return 返回crmDataSource
	 */
	protected final DataSource getDataSource(String dbSchema){
//		BeanFactoryDataSourceLookup dataSourceLookup = new BeanFactoryDataSourceLookup();
//		var dataSource = dataSourceLookup.getDataSource(relativeDbName);
		var metaDb = metadataProvider.getMetaDb(dbSchema);
		var dataSourceName = metaDb.getDbSchema()+"DataSource";
		try{
			return  applicationContext.getBean(dataSourceName,DataSource.class);
		}
		catch(Exception e){
			logger.error("error while getting datasource", e);
			return jdbcTemplate.getDataSource();
		}
	}
	@Override
	public List<MetaEnumMember> findAllRefMap(final MetaRelation refRelation){
		BaseUtil.requireNonNull(refRelation, "refRelation");
		String sql = SqlQuery.SELECT+refRelation.getValueColName() + "," + refRelation.getLabelColExp()
				+ SqlQuery.FROM + metadataProvider.getFrom(refRelation.getFullRelativeObjName());
		//尽管设置了WHERE条件，载入全部数据
//		if(BaseUtils.hasText(refRelation.getWhere())){
//			sql+=SqlQuery.WHERE+ refRelation.getWhere();
//		}
		refRelation.setSql(sql);
		String relativeDbName = refRelation.getRelativeDbSchema();
		if(relativeDbName==null) relativeDbName = refRelation.getDbSchema();
		MetaObject relativeMetaObj = getMetaObject(relativeDbName,refRelation.getRelativeObjName());

		//跨数据库访问视图，需要切换数据源
		if(!relativeDbName.equals(tDbName) && relativeMetaObj.getObjType().startsWith("V")){
			var dataSource = getDataSource(relativeDbName);
			var relJdbcTemplate = new JdbcTemplate(dataSource);
			return relJdbcTemplate.query(sql, new EnumItemResultSetExtractor());
		}
		return jdbcTemplate.query(sql, new EnumItemResultSetExtractor());
	}

	@Override
	public List<Map<String, Object>> findAllRefItems(MetaRelation refRelation){
		BaseUtil.requireNonNull(refRelation, "refRelation");
		String sql = "SELECT * FROM "
				+ metadataProvider.getFrom(refRelation.getFullRelativeObjName());

		refRelation.setSql(sql);
		String relativeDbName = refRelation.getRelativeDbSchema();
		if(relativeDbName==null) relativeDbName = refRelation.getDbSchema();
		MetaObject relativeMetaObj = getMetaObject(relativeDbName,refRelation.getRelativeObjName());

		//跨数据库访问视图，需要切换数据源
		if(!relativeDbName.equals(tDbName) && relativeMetaObj.getObjType().startsWith("V")){
			var dataSource = getDataSource(relativeDbName);
			var relJdbcTemplate = new JdbcTemplate(dataSource);
			return relJdbcTemplate.queryForList(sql);
		}
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public String findRefTextByValue(final MetaRelation refRelation, final Object value){
		BaseUtil.requireNonNull(refRelation, "refRelation");
		String refValue = value instanceof String
				? metadataProvider.quoteValue((String) value)
				: value.toString();

		String sql = SqlQuery.SELECT + refRelation.getLabelColExp() +
				SqlQuery.FROM + metadataProvider.getFrom(refRelation.getFullRelativeObjName()) +
				SqlQuery.WHERE + refRelation.getValueColName()+SqlExpression.EQUAL + refValue;
		return jdbcTemplate.queryForObject(sql, String.class);
	}

	//搜索词是否纯数字测试
	private static final String numPattern = "-?\\d+(\\.\\d+)?";
	protected boolean isNotNumber(final String word){
		return !word.matches(numPattern);
	}
	/**
	 * 全表模糊搜索
	 * @param paginator
	 * @param word 关键字
	 * @return
	 * @throws DataAccessException
	 */
	@Override
	public PagedList<T> searchAll(final Paginator paginator, final String word) throws DataAccessException {
		boolean onlyString = isNotNumber(word);
		SqlQuery sqlQuery = getSearchAllQuery(onlyString);
		String sql = sqlQuery.executor()
				.searchWord(word)
				.select(paginator,metadataProvider,jdbcTemplate);
		if(logger.isDebugEnabled()) logger.debug(sql);

		List<T> data = jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<>(rowMapper, paginator));
		return new PagedList<T>(data, paginator);
	}

	@Override
	public PagedList<T> searchAll(final Paginator paginator, final String word, final String condition, Object...args) throws DataAccessException {
		BaseUtil.requireNonNull(paginator, "pager");
		BaseUtil.requireNonNull(word, "word");
		BaseUtil.requireNonNull(condition, "condition");

		boolean onlyString = isNotNumber(word);
		SqlQuery sqlQuery = getSearchAllQuery(onlyString);

		String sql = sqlQuery.executor()
				.searchWord(word)
				.and(condition)
				.select(paginator, metadataProvider,jdbcTemplate);

		List<T> data = jdbcTemplate.query(sql, args, new RowMapperPagedResultSetExtractor<>(rowMapper, paginator));
		return new PagedList<T>(data, paginator);
	}
	@Override
	public List<T> searchAll(final String word, Sort sort) throws DataAccessException {
		BaseUtil.requireNonNull(word, "word");
		BaseUtil.requireNonNull(sort, "sort");

		boolean onlyString = isNotNumber(word);
		SqlQuery sqlQuery = getSearchAllQuery(onlyString);
		String sql = sqlQuery.executor()
				.searchWord(word)
				.orderBy(sort)
				.select();

		return jdbcTemplate.query(sql, new RowMapperPagedResultSetExtractor<>(rowMapper));
	}
	@Override
	public List<T> searchAll(final String word, Sort sort,final String condition, Object...args) throws DataAccessException {
		BaseUtil.requireNonNull(word, "word");
		BaseUtil.requireNonNull(condition, "condition");
		BaseUtil.requireNonNull(sort, "sort");

		boolean onlyString = isNotNumber(word);
		SqlQuery sqlQuery = getSearchAllQuery(onlyString);
		String sql = sqlQuery.executor()
				.searchWord(word)
				.and(condition)
				.orderBy(sort)
				.select();
		return jdbcTemplate.query(sql, args, new RowMapperPagedResultSetExtractor<>(rowMapper));
	}
	/**
	 * 获取元字段
	 * @param colName 字段名称
	 * @return
	 */
	@Override
	public MetaCol col(final String colName){
		BaseUtil.requireNonNull(colName,"colName");
		MetaCol col = metaObj.getCol(colName);
		Objects.requireNonNull(col, colName+"字段不存在");
		return col;
	}


	protected static final String FILTER = "Filter";
	protected static final String NULL_LABEL = "空值";
	protected static final String TRUE_LABEL = "是";
	protected static final String FALSE_LABEL = "否";

	/**
	 * 构建布尔过滤器
	 * @param col 字段名称
	 * @param trueLabel true时显示文本，默认为是
	 * @param falseLabel false时显示文本，默认为否
	 * @return
	 */
	@Override
	public EntityFilter buildBoolFilter(final MetaCol col, String trueLabel, String falseLabel){
		BaseUtil.requireNonNull(col,"col");
		String colName = col.getColName();
		EntityFilter filter = new EntityFilter(colName+FILTER, col.getDisplayLabel());

		if(trueLabel == null) trueLabel = TRUE_LABEL;
		if(falseLabel ==null) falseLabel= FALSE_LABEL;

		Object defVal = col.getParsedDefaultVal();
		String fallbackText = defVal!=null ? defVal.toString() : "0";

		filter.addFilterCondition("t."+colName+"=0", falseLabel, fallbackText.equals("0"));
		filter.addFilterCondition("t."+colName+"=1", trueLabel, fallbackText.equals("1"));
		if(col.isNullable() && BaseUtil.isNullOrWhitesapce(col.getDefaultVal()))
			filter.addFilterCondition("t."+colName+SqlExpression.IS_NULL, NULL_LABEL);
		return filter;
	}

	@Override
	public EntityFilter buildBoolFilter(final MetaCol col){
		return buildBoolFilter(col,TRUE_LABEL,FALSE_LABEL);
	}

	@Override
	public EntityFilter buildEnumFilter(final MetaCol col){
		BaseUtil.requireNonNull(col,"col");

		String colName = col.getColName();
		EntityFilter filter = new EntityFilter(colName+FILTER, col.getDisplayLabel());

		try{
			if(col.getRelationType()!=MetaRelationType.ENUM) throw new IllegalArgumentException(colName+"字段不是枚举类型");
			col.getMetaEnum().getParsedEnumMap().entrySet().stream()
					.forEach((e)->{
						//0->{NONE,无}
						String cond = "t."+colName+"="+metadataProvider.quoteColValue(col,e.getKey());
						String text = e.getValue().getValue();//无
						Object defVal = col.getParsedDefaultVal();
						boolean fallback =e.getKey().equals(defVal!=null ? defVal.toString() : "_");
						filter.addFilterCondition(cond,text,fallback);
					});
		}
		catch (Exception ex){
			logger.error("字段"+col.getObjName()+"."+col.getColName()+"无法创建枚举过滤器。", ex);
		}

		if(col.isNullable() && BaseUtil.isNullOrWhitesapce(col.getDefaultVal()))
			filter.addFilterCondition("t."+colName+SqlExpression.IS_NULL, NULL_LABEL);
		return filter;
	}

	@Override
	public EntityFilter buildDateFilter(final MetaCol col){
		BaseUtil.requireNonNull(col,"col");

		String colName = col.getColName();
		if(!col.getDataType().hasDatePart()) throw new IllegalArgumentException(colName+"字段不是日期类型");
		EntityFilter filter = new EntityFilter(colName+FILTER, col.getDisplayLabel());
		List<EntityFilterCondition> filterConditions = metadataProvider.buildDateFilterConditions(colName);
		filter.setFilterConditions(filterConditions);

		if(col.isNullable() && BaseUtil.isNullOrWhitesapce(col.getDefaultVal()))
			filter.addFilterCondition(colName+SqlExpression.IS_NULL, NULL_LABEL);

		return filter;
	}

	@Override
	public EntityFilter buildRefFilter(final MetaCol col){
		BaseUtil.requireNonNull(col,"col");

		String colName = col.getColName();
		if(col.getRelationType() != MetaRelationType.REF) throw new IllegalArgumentException(colName+"字段不是REF类型");
		EntityFilter filter = new EntityFilter(colName+FILTER, col.getDisplayLabel());

		MetaRelation relation = metaObj.getRelation(col.getRelationName());
		List<MetaEnumMember> enumItems = findAllRefMap(relation);
		enumItems.stream().forEach((e)->{
					String cond = "t."+colName+"="+metadataProvider.quoteColValue(col,e.getValue());
					String text = e.getText();
					filter.addFilterCondition(cond,text);
				});

		if(col.isNullable() && BaseUtil.isNullOrWhitesapce(col.getDefaultVal()))
			filter.addFilterCondition(colName+SqlExpression.IS_NULL, NULL_LABEL);

		return filter;
	}

	/**
	 * 获取计数查询，支持缓存
	 * @return
	 */
	protected SqlQuery getCountAllQuery(){
		String queryName=SqlQuery.COUNT;
		SqlQuery countAllQuery = cachedQueries.get(queryName);
		if(countAllQuery==null){
			countAllQuery = SqlQuery.builder(metadataProvider)
					.from(metaObj)
					.count()
					.build();
			putSqlQueryToCache(queryName, countAllQuery);
		}
		return countAllQuery;
	}


	@Override
	public int count()  throws DataAccessException{
		SqlQuery sqlQuery = getCountAllQuery();
		String sql = sqlQuery.getSql();
		if(logger.isDebugEnabled()) logger.debug(sql);
		return this.jdbcTemplate.queryForObject(sql, Integer.class);
	}

	protected SqlQuery getCountByQuery(SqlExpression condition){
		SqlQuery sqlQuery;
		if(condition.isFlat()){
			condition.buildFlatConditions();
			String queryName=SqlQuery.COUNT+BY+condition.getCondIds();
			sqlQuery = cachedQueries.get(queryName);
			if(sqlQuery==null){
				sqlQuery = SqlQuery.builder(metadataProvider)
						.from(metaObj)
						.count()
						.where(condition)
						.build();
				putSqlQueryToCache(queryName, sqlQuery);
			}
		}
		else{
			sqlQuery = SqlQuery.builder(metadataProvider)
					.from(metaObj)
					.count()
					.where(condition)
					.build();
		}
		return sqlQuery;
	}
	@Override
	public int count(SqlExpression condition)  throws DataAccessException{
		SqlQuery sqlQuery = getCountByQuery(condition);
		String sql = sqlQuery.getSql();
		if(logger.isDebugEnabled()) logger.debug(sql);
		return this.jdbcTemplate.queryForObject(sql, condition.getArgValues().toArray(), Integer.class);
	}


	@Override
	public int count(String condition, Object...args) throws DataAccessException{
		SqlQuery sqlQuery = getCountAllQuery();
		String sql = sqlQuery.getSqlWith(condition);
		if(logger.isDebugEnabled()) logger.debug(sql);
		return this.jdbcTemplate.queryForObject(sql, args, Integer.class);
	}
	@Override
	public int count(String word,String  condition, Object...args) throws DataAccessException {
		boolean onlyString = isNotNumber(word);
		SqlQuery sqlQuery = getSearchAllQuery(onlyString);
		String sql = sqlQuery.executor()
				.searchWord(word)
				.and(condition)
				.count();
		if(logger.isDebugEnabled()) logger.debug(sql);

		return this.jdbcTemplate.queryForObject(sql, args, Integer.class);
	}
	@Override
	public int countBySearchWord(String word) throws DataAccessException {
		boolean onlyString = isNotNumber(word);
		SqlQuery sqlQuery = getSearchAllQuery(onlyString);
		String sql = sqlQuery.executor()
				.searchWord(word)
				.count();
		if(logger.isDebugEnabled()) logger.debug(sql);

		return this.jdbcTemplate.queryForObject(sql, Integer.class);
	}
	protected SqlQuery getCountByKeyQuery(){
		String queryName=SqlQuery.COUNT+BY_KEY;
		SqlQuery countByKeyQuery = cachedQueries.get(queryName);
		if(countByKeyQuery==null){
			countByKeyQuery = getCountByQuery(keyCondition);
		}
		return countByKeyQuery;
	}
	@Override
	public boolean exists(K k)  throws DataAccessException{
		SqlQuery countByKeyQuery = getCountByKeyQuery();
		String sql = countByKeyQuery.getSql();
		if(logger.isDebugEnabled()) logger.debug(sql);
		return jdbcTemplate.queryForObject(sql, toKeyArgArray(k), Integer.class) > 0;
	}

	@Override
	public int execute(SqlQuery sqlQuery, List<Object> args) throws DataAccessException{
		String sql = sqlQuery.getSql();
		if(logger.isDebugEnabled()) logger.debug(sql);
		return jdbcTemplate.update(sql, args.toArray(), sqlQuery.getArgJdbcTypeArray());
	}

	protected void beforeUpdate(T t){
		//如果是可计算的实体，存入数据库之前计算
		if(t instanceof Computable){
			((Computable)t).compute();
		}
		//留给子类实现其他逻辑

	}

	protected SqlQuery getUpdateQuery(){
		SqlQuery updateQuery = cachedQueries.get(SqlQuery.UPDATE);
		if(updateQuery==null){
			List<MetaCol> updateCols = metaObj.getUpdatableCols();
			SqlExpression updateExpr = SqlExpression.ofAllEqual(updateCols);
			updateQuery = SqlQuery.builder(metadataProvider)
					.update(metaObj)
					.set(updateExpr)
					.where(keyCondition)
					.build();
			putSqlQueryToCache(SqlQuery.UPDATE, updateQuery);
		}
		return updateQuery;
	}
	@Override
	public int update(T t)  throws DataAccessException{
		beforeUpdate(t);

		List<Object> keyArgs = toKeyArgList(t);
		List<Object> updateArgs = toArgList(t,metaObj.getUpdatableCols());
		updateArgs.addAll(keyArgs);
		
		SqlQuery updateQuery = getUpdateQuery();

		int r = execute(updateQuery, updateArgs);
		if(r>0) t.setEntityState(EntityState.DEFAULT);
		return r;
	}

	protected SqlQuery getUpdateQuery(SqlExpression attributes){
		attributes.buildAttributes();

		String queryName=SqlQuery.UPDATE+attributes.getAttrIds();
		SqlQuery updateQuery = cachedQueries.get(queryName);
		if(updateQuery==null){
			updateQuery = SqlQuery.builder(metadataProvider)
					.update(metaObj)
					.set(attributes)
					.where(keyCondition)
					.build();
			putSqlQueryToCache(queryName, updateQuery);
		}
		return updateQuery;
	}
	@Override
	public int update(K k, SqlExpression attributes)  throws DataAccessException{
		SqlQuery updateQuery = getUpdateQuery(attributes);

		List<Object> argValues = attributes.getAttrValues();
		List<Object> keyArgs = toKeyArgList(k);
		argValues.addAll(keyArgs);
		
		return execute(updateQuery, argValues);
	}
	@Override
	public int update(K k, SqlExpression attributes, SqlExpression conditions) throws DataAccessException {
		SqlQuery updateQuery;
		List<MetaCol> keyCols = metaObj.getKeyCols();
		List<Object> keyArgs = toKeyArgList(k);
		
		if(conditions.isFlat()){//平面型条件可缓存
			attributes.buildAttributes();
			conditions.buildFlatConditions();
			
			String queryName = SqlQuery.UPDATE+attributes.getAttrIds()+BY_KEY_AND+conditions.getCondIds();
			updateQuery = cachedQueries.get(queryName);
			if(updateQuery==null){
				SqlExpression cond = SqlExpression.ofAllEqual(keyCols).and(conditions);
				updateQuery = SqlQuery.builder(metadataProvider)
						.update(metaObj)
						.set(attributes)
						.where(cond)
						.build();
				putSqlQueryToCache(queryName, updateQuery);
			}
		}
		else{ //嵌套查询条件表达式暂时不支持缓存计算
			SqlExpression cond = SqlExpression.ofAllEqual(keyCols).and(conditions);
			updateQuery = SqlQuery.builder(metadataProvider)
					.update(metaObj).set(attributes).where(cond).build();
		}
		
		//参数列表
		List<Object> argValues = attributes.getAttrValues();
		argValues.addAll(keyArgs);
		argValues.addAll(conditions.getArgValues());
		
		return execute(updateQuery,argValues);
	}


	protected SqlQuery getUpdateByUniqueKeyQuery(SqlExpression attributes){
		String queryName=SqlQuery.UPDATE+BY+metaObj.getUniqueKey()+attributes.getAttrIds();
		SqlQuery updateQuery = cachedQueries.get(queryName);
		if(updateQuery==null){
			updateQuery = SqlQuery.builder(metadataProvider)
					.update(metaObj)
					.set(attributes)
					.where(SqlExpression.ofEqual(uniqueKeyCol))
					.build();

			putSqlQueryToCache(queryName, updateQuery);
		}
		return updateQuery;
	}

	@Override
	public int updateByUniqueKey(String codeNo, SqlExpression attributes)  throws DataAccessException{
		if(BaseUtil.isNullOrWhitesapce(metaObj.getUniqueKey()))
			throw new InvalidDataAccessResourceUsageException(tClassName+"元数据未定义唯一索引。");

		SqlQuery updateQuery = getUpdateByUniqueKeyQuery(attributes);

		List<Object> argValues = attributes.getAttrValues();
		argValues.add(codeNo);
		return execute(updateQuery, argValues);
	}
	protected SqlQuery getUpdateByUniqueKeyAndQuery(SqlExpression attributes, SqlExpression condition){
		String queryName=SqlQuery.UPDATE+attributes.getAttrIds()+BY+metaObj.getUniqueKey();
		SqlQuery updateQuery = cachedQueries.get(queryName);
		if(updateQuery==null){
			updateQuery = SqlQuery.builder(metadataProvider)
					.update(metaObj)
					.set(attributes)
					.where(SqlExpression.ofEqual(uniqueKeyCol))
					.build();

			putSqlQueryToCache(queryName, updateQuery);
		}
		return updateQuery;
	}

	@Override
	public int updateByUniqueKey(String codeNo, SqlExpression attributes, SqlExpression condition)  throws DataAccessException{
		if(BaseUtil.isNullOrWhitesapce(metaObj.getUniqueKey()))
			throw new InvalidDataAccessResourceUsageException(tClassName+"元数据未定义唯一索引。");

		SqlQuery updateQuery;
		if(condition.isFlat()){
			attributes.buildAttributes();
			condition.buildFlatConditions();
			String queryName=SqlQuery.UPDATE+attributes.getAttrIds()
					+BY+metaObj.getUniqueKey()+" AND "+condition.getCondIds();
			updateQuery = cachedQueries.get(queryName);
			if(updateQuery==null){
				SqlExpression cond = SqlExpression.ofEqual(uniqueKeyCol).and(condition);
				updateQuery = SqlQuery.builder(metadataProvider)
						.update(metaObj)
						.set(attributes)
						.where(cond)
						.build();
				putSqlQueryToCache(queryName, updateQuery);
			}
		} else{ //嵌套查询条件表达式暂时不支持缓存计算
			SqlExpression cond = SqlExpression.ofEqual(uniqueKeyCol).and(condition);
			updateQuery = SqlQuery.builder(metadataProvider)
					.update(metaObj).set(attributes).where(cond).build();
		}
		//参数列表
		List<Object> argValues = attributes.getAttrValues();
		argValues.add(codeNo);
		argValues.addAll(condition.getArgValues());

		return execute(updateQuery, argValues);
	}
	@Override
	public int updateAll(SqlExpression attributes, SqlExpression conditions) throws DataAccessException {
		SqlQuery updateQuery;
		if(conditions.isFlat()){//平面型条件可缓存
			attributes.buildAttributes();
			conditions.buildFlatConditions();
			
			String queryName = SqlQuery.UPDATE+attributes.getAttrIds()+BY+conditions.getCondIds();
			updateQuery = cachedQueries.get(queryName);
			if(updateQuery==null){
				updateQuery = SqlQuery.builder(metadataProvider)
						.update(metaObj).set(attributes).where(conditions).build();
				putSqlQueryToCache(queryName, updateQuery);
			}
		}
		else{ //嵌套查询条件表达式暂时不支持缓存计算
			updateQuery = SqlQuery.builder(metadataProvider)
					.update(metaObj).set(attributes).where(conditions).build();
		}
		//参数列表
		List<Object> argValues = attributes.getAttrValues();
		argValues.addAll(conditions.getArgValues());
		
		return execute(updateQuery,argValues);
	}
	protected List<Object[]> buildBatchInsertArgs(Collection<T> tList){
		List<Object[]> batchArgs = new ArrayList<>();
		for(T t : tList){
			beforeInsert(t);//Compute before update
			List<Object> insertArgs = toArgList(t, metaObj.getInsertableCols());
			batchArgs.add(insertArgs.toArray());
		}
		return batchArgs;
	}
	protected List<Object[]> buildBatchUpdateArgs(Collection<T> tList){
		List<Object[]> batchArgs = new ArrayList<>();
		for(T t : tList){
			beforeUpdate(t);//Compute before update
			List<Object> keyArgs = toKeyArgList(t);
			List<Object> updateArgs = toArgList(t,metaObj.getUpdatableCols());
			updateArgs.addAll(keyArgs);
			batchArgs.add(updateArgs.toArray());
		}
		return batchArgs;
	}
	@Override
	public int[] updateAll(Collection<T> tCollection) throws DataAccessException {
		BaseUtil.requireNoneEmpty(tCollection,"tCollection");
		SqlQuery updateQuery = getUpdateQuery();
		//构建参数
		List<Object[]> batchArgs = buildBatchUpdateArgs(tCollection);
		return this.jdbcTemplate.batchUpdate(updateQuery.getSql(),batchArgs, updateQuery.getArgJdbcTypeArray());
	}

	protected K popKey(final Map<String,Object> kaMap) throws IllegalArgumentException {
		try{
			if(keyColNum == 1){
				String keyColName = metaObj.getKeyCols().get(0).getColName();
				Object keyVal = kaMap.remove(keyColName);
				if(kClass == Long.class){
					return (K)Long.valueOf(keyVal.toString());
				}
				return (K)(BaseUtil.requireNonNull(keyVal,keyColName));
			}
			else{
				K k = kClass.newInstance();
				for (MetaCol keyCol : metaObj.getKeyCols()) {
					String keyColName = keyCol.getColName();
					Object keyVal = kaMap.remove(keyColName);
					objAccess.setKeyProperty(k,keyColName, BaseUtil.requireNonNull(keyVal,keyColName));
				}
				return k;
			}
		}
		catch (NullPointerException ex){
			throw new IllegalArgumentException(tClassName+"字典中未提供主键值或者提供了空值");
		}
		catch (InstantiationException ex){
			throw new IllegalArgumentException(tClassName+".Key必须有无参构造函数");
		}
		catch (IllegalAccessException ex){
			throw new IllegalArgumentException(tClassName+".Key非法访问属性设置");
		}
	}

	private int updateBy(final Map<String,Object> keyAndAttributes) throws IllegalArgumentException, DataAccessException{
		K k = popKey(keyAndAttributes);
		if(keyAndAttributes.isEmpty()) return 0;
		return update(k, keyAndAttributes);
	}
	@Override
	public int[] updateAll(final Collection<Map<String,Object>> listOfKeyAndAttributes, final List<K> updatedKeys){
		return listOfKeyAndAttributes.stream().mapToInt(
				keyAndAttributes -> {
					try{
						K k = popKey(keyAndAttributes);
						if(keyAndAttributes.isEmpty()) return 0;
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

	protected SqlQuery getDeleteQuery(){
		String queryName=SqlQuery.DELETE+BY_KEY;
		SqlQuery sqlQuery = cachedQueries.get(queryName);

		if(sqlQuery==null){
			sqlQuery = SqlQuery.builder(metadataProvider)
					.delete(metaObj)
					.where(keyCondition)
					.build();
			putSqlQueryToCache(queryName, sqlQuery);
		}
		return sqlQuery;
	}
	@Override
	public int delete(K k) throws DataAccessException {
		SqlQuery sqlQuery = getDeleteQuery();
		List<Object> keyArgs = toKeyArgList(k);
		return execute(sqlQuery, keyArgs);
	}

	protected SqlQuery getDeleteByKeyAndCondQuery(K k, SqlExpression conditions){
		SqlQuery sqlQuery;
		if(conditions.isFlat()){
			conditions.buildFlatConditions();
			String queryName=SqlQuery.DELETE+BY_KEY_AND+conditions.getCondIds();
			sqlQuery = cachedQueries.get(queryName);
			if(sqlQuery==null){
				SqlExpression cond = SqlExpression.ofAllEqual(metaObj.getKeyCols()).and(conditions);
				sqlQuery = SqlQuery.builder(metadataProvider)
						.delete(metaObj).where(cond).build();
				putSqlQueryToCache(queryName, sqlQuery);
			}
		}
		else{
			SqlExpression cond = SqlExpression.ofAllEqual(metaObj.getKeyCols()).and(conditions);
			sqlQuery = SqlQuery.builder(metadataProvider)
					.delete(metaObj).where(cond).build();
		}
		return sqlQuery;
	}
	@Override
	public int delete(K k, SqlExpression conditions) throws DataAccessException {
		SqlQuery sqlQuery = getDeleteByKeyAndCondQuery(k,conditions);
		List<Object> argValues = toKeyArgList(k);
		argValues.addAll(conditions.getArgValues());
		
		return execute(sqlQuery, argValues);
	}


	protected SqlQuery getDeleteAllByQuery(SqlExpression conditions){
		SqlQuery sqlQuery;
		if(conditions.isFlat()){
			conditions.buildFlatConditions();
			String queryName=SqlQuery.DELETE+BY+conditions.getCondIds();
			sqlQuery = cachedQueries.get(queryName);
			if(sqlQuery==null){
				sqlQuery = SqlQuery.builder(metadataProvider)
						.delete(metaObj).where(conditions).build();
				putSqlQueryToCache(queryName, sqlQuery);
			}
		}
		else{
			sqlQuery = SqlQuery.builder(metadataProvider)
					.delete(metaObj).where(conditions).build();
		}
		return sqlQuery;
	}
	@Override
	public int deleteAll(SqlExpression conditions) throws DataAccessException{
		SqlQuery sqlQuery = getDeleteAllByQuery(conditions);
		return execute(sqlQuery,conditions.getArgValues());
	}
	
	/**
	 * 获取删除所有记录的查询
	 * @return
	 */
	protected SqlQuery getDeleteAllQuery(){
		String queryName=SqlQuery.DELETE;
		SqlQuery deleteAllQuery = cachedQueries.get(queryName);
		if(deleteAllQuery==null){
			deleteAllQuery = SqlQuery.builder(metadataProvider)
					.delete(metaObj).build();
			putSqlQueryToCache(queryName, deleteAllQuery);
		}
		return deleteAllQuery;
	}
	
	@Override
	public int deleteAll(String condition, Object...args) throws DataAccessException{
		SqlQuery sqlQuery = getDeleteAllQuery();
		String sql = sqlQuery.getSqlWith(condition);
		if(logger.isDebugEnabled()) logger.debug(sql);
		return this.jdbcTemplate.update(sql, args);
	}

	@Override
	public int[] deleteAll(final Collection<K> keys, final String andCondition) throws DataAccessException{
		SqlQuery sqlQuery = getDeleteQuery();
		List<Object[]> batchArgs = keys.stream()
				.map(k -> toKeyArgArray(k))
				.collect(Collectors.toList());
		String sql = BaseUtil.hasText(andCondition)
				? sqlQuery.getSqlWith(andCondition)
				: sqlQuery.getSql();
		return this.jdbcTemplate.batchUpdate(sql,batchArgs);
	}

	@Override
	public T save(T t){
		T result = t;
		int r = 0;
		switch (t.getEntityState()){
			case EntityState.CREATED:
			case EntityState.NEW_MODIFIED:
				r = insert(t);
				break;
			case EntityState.MODIFIED:
				r = update(t);
				break;
			case EntityState.DELETED:
				r = delete(t.getId());
				break;
			default:
				break;
		}
		if(r>0) result.setEntityState(EntityState.DEFAULT);
		return result;
	}
	
	@Override
	public int execute(String callable, Object...args) throws DataAccessException {
		String sql = metadataProvider.call(callable, args.length);
		if(logger.isDebugEnabled()) logger.debug(sql);
		return this.jdbcTemplate.queryForObject(sql, args, Integer.class);
	}

	@Override
	public T executeForObject(String callable, Object...args) throws DataAccessException {
		String sql = metadataProvider.call(callable, args.length);
		if(logger.isDebugEnabled()) logger.debug(sql);
		return this.jdbcTemplate.queryForObject(sql, rowMapper, args);
	}

	@Override
	public List<T> executeForList(String callable, Object...args) throws DataAccessException {
		String sql = metadataProvider.call(callable, args.length);
		if(logger.isDebugEnabled()) logger.debug(sql);
		return this.jdbcTemplate.query(sql, rowMapper, args);
	}

	@Override
	public void call(String callable, Object...args) throws DataAccessException {
		String sql = metadataProvider.call(callable, args.length);
		if(logger.isDebugEnabled()) logger.debug(sql);
		this.jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				//do nothing
			}
		}, args);
	}
	@Override
	public MetaUi getMetaUi(int tenantID, String lang, boolean reload){
		return metadataProvider.getTenancyMetaUi(tenantID,metaObj.getDbSchema(),metaObj.getObjName(),lang, reload);
	}

	/**
	 * 计算两个实体差异
	 * @param o 原值
	 * @param n 新值
	 * @return 属性修改
	 */
	public ChangeLog.ChangeData differ(T o, T n){
		Assert.notNull(o, "Old entity can't be null");
		Assert.notNull(n, "New entity can't be null");
		var changes = new HashMap<String, ChangeLog.ChangeData>();
		for(var metaCol : metaObj.getCols()){
			var oldValue = getColValue(o,metaCol);
			var newValue = getColValue(n,metaCol);

			if(!areValuesEqual(metaCol,oldValue,newValue)){
				changes.put(metaCol.getColName(), ChangeLog.changed(oldValue,newValue));
			}
		}
		return changes.isEmpty()
				? ChangeLog.unchanged()
				: ChangeLog.changed(o,changes);
	}

	private boolean areValuesEqual(MetaCol col,Object oldVal, Object newVal) {
		if (oldVal == null &&  newVal == null) return true;
		if (oldVal == null || newVal == null) return false;
		// 如果都是数字（或字符串形式的数字），进行数值比较
		if (col.getDataType().isDecimal()) {
			try {
				return new BigDecimal(oldVal.toString())
						.compareTo(new BigDecimal(newVal.toString())) == 0;
			} catch (NumberFormatException e) {
				// fallback
				return false;
			}
		}
		// 默认比较
		return Objects.equals(oldVal, newVal);
	}



}
