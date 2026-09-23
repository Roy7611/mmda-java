package cloud.mmda.core.data.sql;


import cloud.mmda.core.data.exceptions.SqlQueryGrammarException;
import cloud.mmda.core.data.exceptions.SqlQueryUnresolvedException;
import cloud.mmda.core.data.jdbc.metadata.DbMetadataProvider;
import cloud.mmda.core.Tenancy;
import cloud.mmda.core.enums.ExtensionType;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.utils.BaseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import jakarta.validation.constraints.NotNull;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlQuery {
	private final static Log logger = LogFactory.getLog(SqlQuery.class);

	public static final char QM = '?';//Question Mark
	public final static String SELECT = "SELECT ";
	public final static String INSERT = "INSERT ";
	public final static String UPDATE = "UPDATE ";
	public final static String DELETE = "DELETE ";
	
	public final static String LIST = "* ";
	public final static String COUNT = "COUNT(*) ";
	public final static String COUNT_DISTINCT = "COUNT(DISTINCT *) ";

	public static final String INNER_JOIN = " INNER JOIN ";
	public static final String LEFT_JOIN = " LEFT JOIN ";
	public static final String RIGHT_JOIN = " RIGHT JOIN ";
	public static final String CROSS_JOIN = " CROSS JOIN ";

	public static final String FROM = " FROM ";

	public static final String SLOT_PARTITION = " _PARTITION_ ";
	public static final String SLOT_SORT= " _SORT_ ";

	public final static String WHERE = " WHERE ";
	public final static String ORDER_BY = " ORDER BY ";
	public final static String GROUP_BY = " GROUP BY ";
	public final static String HAVING = " HAVING ";

	private static final char[] ALPHABET = "tbcdefghijklmnopqrsuvwxyz".toCharArray();

	private String queryType;
	private MetaObject metaObj;
	private String currObjName;
	private String defaultSort;

	private Map<String, String> aliasMap;
	private Function<String,String> quoteFunc;
	private SqlExpression updateExpr;
	private SqlExpression conditionExpr;
	//查询结果类型
	private String selectType;

	//是否包含行号及其他SLOT 2020.4.4
	private boolean withRowNum;
	//是否指定表分区查询
	private boolean withNamedPartitionSlot;
	//是否需解决行号的Order By子句
	private boolean withSortSlot;

	//选择的列
	private List<MetaCol> selectedCols;
	//private Map<String,String> aggregates;

	//连接表清单
	private List<SqlJoin> joins;

	//构建结果
	private String sql;
	private String countSql;//构建后的计数语句
	private String select;//构建后的选择字段列表
	private String updateSet;
	private String deleteFrom;
	private String from;//构建后的查询源
	private String where;//构建后的条件语句
	private Sort sort;//不分页的排序
	private List<Integer> argTypes;
	private List<Integer> argJdbcTypes;
	private List<String> argGeneratedKeys;

	public SqlQuery(){
		this.aliasMap = new HashMap<String, String>();
		this.joins = new ArrayList<>();
		this.withRowNum = true;
	}

	public void setQueryType(String t) {
		this.queryType = t;
	}

	public MetaObject getMetaObject() {
		return this.metaObj;
	}

	public void setMetaObject(MetaObject metaObj) {
		setMetaObject(metaObj, String.valueOf(ALPHABET[0]));
	}

	public void setMetaObject(MetaObject metaObj, String alias) {
		this.metaObj = metaObj;
		this.currObjName = metaObj.getObjName();
		this.defaultSort = metaObj.getKeyCols().stream()
				.map(col -> alias + '.' + col.getColName())
				.collect(Collectors.joining(","));
		if(this.queryType == INSERT){
			this.argGeneratedKeys = metaObj.getGeneratedKeyCols()
										   .stream()
										   .map(c->c.getColName())
										   .collect(Collectors.toList());
		}

		//有扩展基类
		if(metaObj.getExtendType() == ExtensionType.EXTENDS){
			aliasMap.put(metaObj.getSuperName(), alias+"b");//if this is t then tb
		}
	}

	public String getAliasName(String name) {
		return aliasMap.get(name);
	}

	public void setAliasName(String alias) {
		if (currObjName == null)
			return;
		aliasMap.put(currObjName, alias);
	}

	public void setAliasName(String name, String alias) {
		aliasMap.put(name, alias);
	}

	public String getAliasDotName(String objName, String colName) {
		String alias = aliasMap.get(objName);
		return alias == null ? colName : alias + '.' + this.quoteFunc.apply(colName);
	}
	public String getAliasDotName(final MetaCol col) {
		return getAliasDotName(col.getObjName(),col.getColName());
	}

	public final boolean isWithRowNum(){ return withRowNum;}
	public final boolean hasJoins() {
		return !joins.isEmpty();
	}

	public void addJoin(final String joinType, final MetaObject joinTo,
						final String alias, final SqlExpression joinOn, final MetaCol labelCol) {
		MetaCol joinToCol = joinOn.getRightCol();
		this.currObjName = joinTo.getObjName();
		this.joins.add(new SqlJoin(joinType,joinTo,alias,joinOn,labelCol));
	}
	
	public List<String> getJoinedObjNames() {
		return joins.stream()
				.map(j->j.joinTo.getObjName())
				.collect(Collectors.toList());
	}

	public SqlExpression getUpdateExpression() {
		return this.updateExpr;
	}

	public void setUpdateExpression(SqlExpression e) {
		this.updateExpr = e;
	}

	public SqlExpression getConditionExpression() {
		return this.conditionExpr;
	}

	public void setConditionExpression(SqlExpression e) {
		this.conditionExpr = e;
	}
	public void setSort(Sort sort){
		this.sort = sort;
	}
	public void setSelectType(String selectType) {
		this.selectType = selectType;
	}

	public List<MetaCol> getSelectedCols() {
		return selectedCols;
	}
	public void setSelectedCols(List<MetaCol> cols){
		selectedCols = cols;
		selectType = LIST;
	}
	public void addSelectedCols(List<MetaCol> cols) {
		if (selectedCols == null)
			selectedCols = new ArrayList<MetaCol>(cols);
		else
			selectedCols.addAll(cols);
		if(selectType==null) selectType = LIST;
	}
	public void addSelectedCol(MetaCol col) {
		if (selectedCols == null) selectedCols = new ArrayList<MetaCol>();
		selectedCols.add(col);
		if(selectType==null) selectType = LIST;
	}

	//以下函数只做字符串拼接，并不做语法检查

	private void requireNoWhere(){
		if(BaseUtil.hasText(where)){
			throw new SqlQueryGrammarException("不能重复条件语句");
		}
	}

	public final String getBuiltSelect(){return select;}
	public final String getBuiltFrom(){return from;}
	public final String getBuiltCondition(){return where;}

	public String getSql() {
		if(withSortSlot || withNamedPartitionSlot)
			throw new SqlQueryUnresolvedException(withSortSlot,withNamedPartitionSlot);
		return sql;
	}
	public String getCountSql(){
		if(withNamedPartitionSlot)
			throw new SqlQueryUnresolvedException(false,withNamedPartitionSlot);
		return countSql;
	}
	public String getTenancySql(int tenantID, DbMetadataProvider metadataProvider) {
		String newSql = withSortSlot ? sql.replace(SLOT_SORT,ORDER_BY+defaultSort) : sql;
		return resolveSqlPartition(newSql,tenantID,metadataProvider);
	}
	public String getTenancyCountSql(int tenantID, DbMetadataProvider metadataProvider) {
		return resolveSqlPartition(countSql,tenantID,metadataProvider);
	}

	/**
	 * 限于命名分区更新或插入
	 * @param tenantID 租户标识
	 * @param metadataProvider
	 * @return
	 */
	public String onNamedPartition(int tenantID, DbMetadataProvider metadataProvider) {
		return resolveSqlPartition(sql,tenantID,metadataProvider);
	}


	/**
	 * 给sql语句添加条件condition，确保你的查询语句只有from字句
	 * @param condition
	 * @return
	 */
	public String getSqlWith(final String condition) {
		BaseUtil.requireNonBlank(condition,"condition");
		if(BaseUtil.hasText(where)){
			return SELECT + select +
					FROM + from +
					WHERE + "("+where+") AND ("+condition+")";
		}
		return getSql() + WHERE + condition;
	}
	public String getCountSqlWith(final String condition) {
		BaseUtil.requireNonBlank(condition,"condition");
		if(BaseUtil.hasText(where)){
			return SELECT + COUNT +
					FROM + from +
					WHERE + "("+where+") AND ("+condition+")";
		}
		return getCountSql() + WHERE + condition;
	}

	/**
	 * 构建好的sql连接条件子句，并根据租户标识解决命名分区
	 * @param tenantID 租户标识
	 * @param condition 条件
	 * @param metadataProvider
	 * @param count 是否计数
	 * @return
	 */
	private String concatTenancySqlWith(int tenantID, final String condition, DbMetadataProvider metadataProvider, boolean count) {
		BaseUtil.requireNonBlank(condition,"condition");
		requireNoWhere();
		String suffix = WHERE + condition;
		String newSql = count
				? countSql + suffix
				: withSortSlot ? sql.replace(SLOT_SORT,suffix) : sql + suffix;
		return resolveSqlPartition(newSql, tenantID, metadataProvider);
	}
	public String getTenancySqlWith(int tenantID, final String condition, DbMetadataProvider metadataProvider) {
		return concatTenancySqlWith(tenantID,condition,metadataProvider,false);
	}
	public String getTenancyCountSqlWith(int tenantID, final String condition, DbMetadataProvider metadataProvider) {
		return concatTenancySqlWith(tenantID,condition,metadataProvider,true);
	}

	/**
	 * 构建好的sql连接条件、排序子句
	 * @param condition 条件
	 * @param sort 排序
	 * @param count 是否计数
	 * @return
	 */
	private String concatSqlWith(final String condition, final Sort sort, boolean count){
		BaseUtil.requireNonBlank(condition,"condition");
		Objects.requireNonNull(sort,"sort");
		requireNoWhere();
		if(withNamedPartitionSlot)
			throw new SqlQueryUnresolvedException(false,withNamedPartitionSlot);

		String suffix = WHERE + condition + ORDER_BY + sort;
		return count
				? countSql + WHERE + condition
				: withSortSlot ? sql.replace(SLOT_SORT,suffix) : sql+suffix;
	}
	public String getSqlWith(final String condition, final Sort sort){
		return concatSqlWith(condition,sort,false);
	}
	public String getCountSqlWith(final String condition, final Sort sort){
		return concatSqlWith(condition,sort,true);
	}

	/**
	 * 构建好的sql连接条件、排序子句，并根据租户标识解决命名分区
	 * @param tenantID 租户标识
	 * @param condition 查询条件
	 * @param sort 排序
	 * @param metadataProvider
	 * @param count 是否计数
	 * @return
	 */
	private String concatTenancySqlWith(int tenantID, final String condition, final Sort sort, DbMetadataProvider metadataProvider, boolean count){
		BaseUtil.requireNonBlank(condition,"condition");
		Objects.requireNonNull(sort,"sort");
		requireNoWhere();

		String suffix = WHERE + condition + ORDER_BY + sort;
		String newSql = count
				? countSql + WHERE + condition
				: withSortSlot ? sql.replace(SLOT_SORT,suffix) : sql+suffix;
		return resolveSqlPartition(newSql, tenantID, metadataProvider);
	}
	public String getTenancySqlWith(int tenantID, final String condition, final Sort sort, DbMetadataProvider metadataProvider){
		return concatTenancySqlWith(tenantID,condition,sort,metadataProvider,false);
	}
	public String getTenancyCountSqlWith(int tenantID, final String condition, final Sort sort, DbMetadataProvider metadataProvider){
		return concatTenancySqlWith(tenantID,condition,sort,metadataProvider,true);
	}

	public String getSqlWith(final Sort sort){
		Objects.requireNonNull(sort,"sort");
		if(withNamedPartitionSlot)
			throw new SqlQueryUnresolvedException(false,withNamedPartitionSlot);
		String suffix = ORDER_BY + sort;
		return withSortSlot ? sql.replace(SLOT_SORT,suffix) : sql + suffix;
	}
	public String getTenancySqlWith(int tenantID, final Sort sort, DbMetadataProvider metadataProvider){
		Objects.requireNonNull(sort,"sort");
		String suffix = ORDER_BY + sort;
		String newSql = withSortSlot ? sql.replace(SLOT_SORT,suffix) : sql + suffix;
		return resolveSqlPartition(newSql,tenantID,metadataProvider);
	}
	public String getSqlWith(final Paginator paginator, DbMetadataProvider metadataProvider, final String prependCondition) {
		Objects.requireNonNull(paginator,"pager");
		if(withNamedPartitionSlot)
			throw new SqlQueryUnresolvedException(false,withNamedPartitionSlot);
		String newSql = sql;
		if(BaseUtil.hasText(prependCondition)){
			//添加前置条件
			newSql = withSortSlot
				? sql.replace(WHERE,WHERE+"("+prependCondition+") AND (")
					.replace(SLOT_SORT,")"+SLOT_SORT)
				: sql.replace(WHERE,WHERE+"("+prependCondition+") AND (")
					+")";
		}
		String suffix = metadataProvider.toPagerSql(paginator);
		return withSortSlot ? newSql.replace(SLOT_SORT,suffix) : newSql+suffix;
	}
	public String getSqlWith(final Paginator paginator, DbMetadataProvider metadataProvider) {
		return getSqlWith(paginator,metadataProvider,null);
	}
	public String getTenancySqlWith(int tenantID, final Paginator paginator, DbMetadataProvider metadataProvider) {
		Objects.requireNonNull(paginator,"pager");
		return withSortSlot
			? resolveSql(sql,tenantID, paginator,metadataProvider)
			: resolveSqlPartition(sql,tenantID,metadataProvider) + metadataProvider.toPagerSql(paginator);
	}

	public String getSqlWith(final String condition, final Paginator paginator, DbMetadataProvider metadataProvider) {
		Objects.requireNonNull(condition,"condition");
		Objects.requireNonNull(paginator,"pager");
		requireNoWhere();
		if(withNamedPartitionSlot)
			throw new SqlQueryUnresolvedException(false,withNamedPartitionSlot);

		String suffix = WHERE + condition + metadataProvider.toPagerSql(paginator);
		return withSortSlot ? sql.replace(SLOT_SORT,suffix) : sql + suffix;
	}
	public String getTenancySqlWith(int tenantID, final String condition, final Paginator paginator, DbMetadataProvider metadataProvider) {
		Objects.requireNonNull(condition,"condition");
		Objects.requireNonNull(paginator,"pager");
		requireNoWhere();

		String suffix = WHERE + condition + metadataProvider.toPagerSql(paginator);
		String newSql = withSortSlot ? sql.replace(SLOT_SORT,suffix) : sql + suffix;
		return resolveSqlPartition(newSql,tenantID,metadataProvider);
	}


	public String getSqlLimit(int limit, final String sort, DbMetadataProvider metadataProvider) {
		if(withNamedPartitionSlot)
			throw new SqlQueryUnresolvedException(false,withNamedPartitionSlot);

		String suffix = metadataProvider.toPagerSql(limitPager(limit,sort));
		return withSortSlot ? sql.replace(SLOT_SORT,suffix) : sql + suffix;
	}
	public String getSqlLimit(int limit, DbMetadataProvider metadataProvider) {
		return getSqlLimit(limit, defaultSort, metadataProvider);
	}
	public String getTenancySqlLimit(int tenantID, int limit, final String sort, DbMetadataProvider metadataProvider) {
		String suffix = metadataProvider.toPagerSql(limitPager(limit,sort));
		return resolveSqlPartition(
				withSortSlot ? sql.replace(SLOT_SORT,suffix) : sql + suffix,
				tenantID,
				metadataProvider);
	}
	public String getTenancySqlLimit(int tenantID, int limit, DbMetadataProvider metadataProvider) {
		return getTenancySqlLimit(tenantID, limit, defaultSort, metadataProvider);
	}

	public String getSqlWithLimit(final String condition, int limit, final String sort, DbMetadataProvider metadataProvider) {
		Objects.requireNonNull(condition,"condition");
		requireNoWhere();
		if(withNamedPartitionSlot)
			throw new SqlQueryUnresolvedException(false,withNamedPartitionSlot);

		String suffix = WHERE + condition + metadataProvider.toPagerSql(limitPager(limit,sort));
		return withSortSlot ? sql.replace(SLOT_SORT,suffix) : sql + suffix;
	}
	public String getSqlWithLimit(final String condition, int limit, DbMetadataProvider metadataProvider) {
		return getSqlWithLimit(condition,limit,defaultSort,metadataProvider);
	}
	public String getTenancySqlWithLimit(int tenantID, final String condition, int limit, DbMetadataProvider metadataProvider) {
		return getTenancySqlWithLimit(tenantID, condition, limit, defaultSort, metadataProvider);
	}
	public String getTenancySqlWithLimit(int tenantID, final String condition, int limit, final String sort, DbMetadataProvider metadataProvider) {
		Objects.requireNonNull(condition,"condition");
		requireNoWhere();

		String suffix = WHERE + condition + metadataProvider.toPagerSql(limitPager(limit,sort));
		return resolveSqlPartition(
				withSortSlot ? sql.replace(SLOT_SORT,suffix) : sql + suffix,
				tenantID,
				metadataProvider);
	}

	///////////////////////////////////////////////////////////////////////////
	// static build sql methods
	// used by ComplexRepository

	public static String resolveSqlSort(String sql, String sort){
		return sql.replace(SLOT_SORT, ORDER_BY + sort);
	}
	public static String resolveSqlSort(String sql, Sort sort){
		return resolveSqlSort(sql, sort.toString());
	}

	public static String resolveSqlPartition(String sql, int tenantID, DbMetadataProvider metadataProvider){
		return sql.replace(SLOT_PARTITION, metadataProvider.getPartitionName(tenantID));
	}
	public static String resolveSql(String sql, int tenantID, Paginator paginator, DbMetadataProvider metadataProvider){
		return resolveSqlPartition(sql+metadataProvider.toPagerSql(paginator), tenantID, metadataProvider);
	}
	public static String resolveSql(String sql, int tenantID, String sort, DbMetadataProvider metadataProvider){
		return resolveSqlSort(resolveSqlPartition(sql, tenantID, metadataProvider), sort);
	}
	public static String resolveSql(String sql, int tenantID, Sort sort, DbMetadataProvider metadataProvider){
		return resolveSqlSort(resolveSqlPartition(sql, tenantID, metadataProvider), sort);
	}

	public static String buildSqlToCount(String select) {
		return select.replace(SqlQuery.LIST, SqlQuery.COUNT);
	}
	public static String buildSqlToCount(String select, String condition) {
		return buildSqlWith(select, condition).replace(SqlQuery.LIST, SqlQuery.COUNT);
	}

	public static String buildSqlWith(String select, String condition) {
		return select + WHERE + condition;
	}

	public static String buildSqlWith(String select, Paginator paginator, DbMetadataProvider metadataProvider){
		return select + metadataProvider.toPagerSql(paginator);
	}


	public static String buildSqlWith(String select, int limit, String sort, DbMetadataProvider metadataProvider) {
		return buildSqlWith(select, limitPager(limit,sort),metadataProvider);
	}

	public static Paginator limitPager(int limit){
		return new Paginator(limit,1);
	}
	public static Paginator limitPager(int limit, @NotNull String sort){
		return new Paginator(limit,1, sort);
	}

	public static String buildSqlWith(String select, String condition, Paginator paginator, DbMetadataProvider metadataProvider) {
		return select + WHERE + condition + metadataProvider.toPagerSql(paginator);
	}

	public static String buildSqlWith(String select, String condition, int limit, String sort, DbMetadataProvider metadataProvider) {
		return select + WHERE + condition + metadataProvider.toPagerSql(limitPager(limit,sort));
	}

	public static String buildSqlWith(String select, String condition,Sort sort){
		return select + WHERE + condition + ORDER_BY + sort.toString();
	}
	public static String buildSqlWith(String select, Sort sort){
		return select + ORDER_BY + sort.toString();
	}

	///////////////////////////////////////////////////////////////////////////


	public List<Integer> getArgTypes() {
		return argTypes;
	}
	
	public Byte[] getArgTypeArray() {
		return argTypes.toArray(new Byte[0]);
	}
	public List<Integer> getArgJdbcTypes(){
		return argJdbcTypes;
	}
	public int[] getArgJdbcTypeArray() {
		int[] types = new int[argJdbcTypes.size()];
		Integer[] args=argJdbcTypes.toArray(new Integer[0]);
		for(int i=0;i<args.length;i++){
			types[i]=args[i];
		};
		return types;
	}

	// endregion

	public List<String> getGeneratedKeys() {
		return argGeneratedKeys;
	}

	/**
	 * Sql联接
	 */
	static class SqlJoin{
		public final String joinType;
		public final MetaObject joinTo;
		public String alias;
		public final SqlExpression joinOn;
		public final MetaCol labelCol;
		public SqlJoin(final String joinType, final MetaObject joinTo, final String alias,
					   final SqlExpression joinOn, final MetaCol labelCol){
			this.joinType=joinType;
			this.joinTo=joinTo;
			this.alias=alias;
			this.joinOn=joinOn;
			this.labelCol=labelCol;
		}

		public String getAliasDotName(MetaCol col){
			String a = col.getObjName().equals(joinTo.getObjName()) ? alias : "t";
			return a+"."+col.getColName();
		}
		public String buildExpression(){
			return joinOn.buildExpression(col -> getAliasDotName(col),SqlExpression.ALIAS_DOT_NAME);
		}
	}
	////////////////////////////////////
	// Builder模式
	///////////////////////////////////
	
	/**
	 * SQL语言构建器
	 * @author roshion
	 * <p></p>
	 * 使用例子{@code 
	 * 	from(metaObj).as("a")
	 *   .join(INNER_JOIN, metaObj2).as("b", expression)
	 *   .join(metaRelation1).as("c")
	 *   .where(expression)
	 *   .select(colList)
	 *   .build();
	 * }
	 */
	public static SqlQueryBuilder builder(DbMetadataProvider provider){
		return new SqlQueryBuilder(provider);
	}
	

	public interface IUpdate { //update 后
		//只能set
		IWhere set(SqlExpression attribures);
	}
	public interface IInsert { //insert 后
		//只能提供values
		IBuildable values(SqlExpression attribures);
	}
	public interface IQuery {
		IQueryWhere count();
		IQueryWhere countDistinct();
		IQueryWhere select(List<MetaCol> cols);
		IQueryWhere select(MetaObject obj);
		IQueryWhere select(MetaObject... objs);
		IQueryWhere select(MetaCol col);
		IQueryWhere select();
		IBuildable searchAll(boolean onlyString);
	}

	public interface IFrom extends IQuery,IJoin{
		
	}
	/**
	 * where后可以调用接口，由于可以略过where()函数，也可直接build
	 * @author roshion
	 *
	 */
	public interface IWhere {
		IBuildable where(SqlExpression condition);
		SqlQuery build();//短路接口，可以不使用条件
	}
	
	public interface IQueryWhere {
		IQueryWhere withRowNum();
		IQueryWhere withoutRowNum();
		IBuildable where(SqlExpression condition);
		SqlQuery build();
	}
	
	public interface IJoin{
		IAlias as(String alias);
		IJoin join(MetaRelation r,boolean addCols);
//		IJoin join(String joinType, MetaObject metaObj, SqlExpression on);
//		IJoin join(String joinType, SqlExpression on);
		IQuery joinAll(List<MetaRelation> relations,boolean addCols);
		IQuery query();
	}
	
	public interface IAlias{
		IJoin join(MetaRelation r,boolean addCols);
//		IJoin join(String joinType, MetaObject metaObj, SqlExpression on);
//		IJoin join(String joinType, SqlExpression on);
		IQuery query();
		
	}
	
	/**
	 * 可以build的接口，
	 * 供insert/update/delete使用
	 * @author roshion
	 *
	 */
	public interface IBuildable{
		IBuildable orderBy(Sort sort);
		SqlQuery build();
	}

	/**
	 * SqlQuery构建器
	 */
	public static class SqlQueryBuilder implements IFrom,IAlias,IInsert,IUpdate,IWhere,IQueryWhere,IJoin, IQuery,IBuildable  {
		private final SqlQuery currQuery = new SqlQuery();
		private DbMetadataProvider metadataProvider;


		public SqlQueryBuilder(DbMetadataProvider metadataProvider){
			this.metadataProvider=metadataProvider;
			this.currQuery.quoteFunc =(name)-> metadataProvider.quoteName(name);
		}
	
		public IFrom from(MetaObject metaObj) {
			currQuery.setQueryType(SqlQuery.SELECT);
			currQuery.setMetaObject(metaObj);
			return this;
		}
		
		public IWhere delete(MetaObject metaObj) {
			currQuery.setQueryType(SqlQuery.DELETE);
			currQuery.setMetaObject(metaObj);
			return this;
		}
		public IUpdate update(MetaObject metaObj) {
			currQuery.setQueryType(SqlQuery.UPDATE);
			currQuery.setMetaObject(metaObj);
			return this;
		}
		public IInsert insert(MetaObject metaObj) {
			currQuery.setQueryType(SqlQuery.INSERT);
			currQuery.setMetaObject(metaObj);
			return this;
		}

		

		//////////////////////////////////////////////////////////////////////////
		// count
		//////////////////////////////////////////////////////////////////////////

		@Override
		public IQueryWhere count() {
			currQuery.setSelectType(SqlQuery.COUNT);
			return this;
		}
		@Override
		public IQueryWhere countDistinct() {
			currQuery.setSelectType(SqlQuery.COUNT_DISTINCT);
			return this;
		}
		

		//////////////////////////////////////////////////////////////////////////
		// select
		//////////////////////////////////////////////////////////////////////////
		@Override
		public IQueryWhere select(List<MetaCol> cols) {
			currQuery.addSelectedCols(cols);
			return this;
		}
		@Override
		public IQueryWhere select(MetaObject obj) {
			currQuery.addSelectedCols(obj.getCols());
			return this;
		}
		@Override
		public IQueryWhere select(MetaObject... objs) {
			for(MetaObject obj : objs){
				currQuery.addSelectedCols(obj.getCols());
			}
			return this;
		}
		@Override
		public IQueryWhere select(MetaCol col) {
			currQuery.addSelectedCol(col);
			return this;
		}
		@Override
		public IQueryWhere select() {
			currQuery.setSelectedCols(null);
			return this;
		}
		@Override
		public IQueryWhere withRowNum(){
			currQuery.withRowNum = true;
			return this;
		}
		@Override
		public IQueryWhere withoutRowNum(){
			currQuery.withRowNum = false;
			return this;
		}
		@Override
		public IBuildable searchAll(boolean onlyString) {
			currQuery.setSelectedCols(null);
			List<MetaCol> cols = currQuery.selectedCols;
			if(cols == null) cols = currQuery.metaObj.getCols();
			if(onlyString){//!word.matches("-?\\d+(\\.\\d+)?")
				cols = cols.stream().filter(col-> col.getDataType().isString())
						.collect(Collectors.toList());
			}
			if(currQuery.hasJoins()){
				cols = Stream.concat(cols.stream(),currQuery.joins.stream().map(j->j.labelCol).filter(col -> col!=null))
						.collect(Collectors.toList());
			}

			SqlExpression condition = SqlExpression.searchAll(cols);
			currQuery.setConditionExpression(condition);
			return this;
		}
		@Override
		public IAlias as(String alias) {
//			currQuery.setAliasName(alias);
			return this;
		}
		@Override
		public IJoin join(MetaRelation r,boolean addCols) {
			String jo = r.getJoinOn();
			SqlExpression joinOn=null;
			
			//找关联元对象
			MetaObject joinTo =  metadataProvider.getMetaObject(r.getRelativeDbSchema(),r.getRelativeObjName());

			//关系通常是外键，只支持与连接
			if(jo.indexOf(SqlExpression.AND)!=-1){
				String[] jos = jo.split(SqlExpression.AND);
				joinOn = buildJoinExpression(joinTo,jos[0]);
				for(int i = 1; i<jos.length; i++){
					SqlExpression je = buildJoinExpression(joinTo,jos[i]);
					joinOn.and(je);
				}
			}
			else{
				joinOn = buildJoinExpression(joinTo,r.getJoinOn());
			}
			String joinType = joinOn.getCol().isNullable()?SqlQuery.LEFT_JOIN:SqlQuery.INNER_JOIN;
			
			currQuery.addJoin(joinType, joinTo, r.getRelationName(), joinOn, joinTo.getCol(r.getLabelColName()));

			return this;
		}

		@Override
		public IQuery joinAll(List<MetaRelation> relations,boolean addCols){
			if(!relations.isEmpty()){
				for(MetaRelation relation : relations){
					//自关联或者父子关联忽略，目前不支持
					if(relation.getRelativeObjName().equals(relation.getObjName())) continue;
					join(relation,addCols);
				}
			}
			return  this;
		}

//		@Override
//		public IJoin join(String joinType, MetaObject metaObj, SqlExpression joinOn){
//			currQuery.addJoinExpression(joinType, joinOn);
//			return this;
//		}
		
		@Override
		public IQuery query() {
			return this;
		}
		@Override
		public IBuildable where(SqlExpression condition) {
			currQuery.setConditionExpression(condition);
			return this;
		}
		@Override
		public IBuildable orderBy(Sort sort){
			currQuery.setSort(sort);
			return this;
		}
		@Override
		public SqlQuery build() {
			// 参数列表
			if (currQuery.updateExpr != null) {
				currQuery.updateExpr.buildArgs(col -> currQuery.getAliasDotName(col),QM,false);
//				currQuery.argValues = currQuery.updateExpr.getArgValues();
				currQuery.argTypes = currQuery.updateExpr.getArgTypes();
			} else {
//				currQuery.argValues = new ArrayList<Object>();
				currQuery.argTypes = new ArrayList<Integer>();
			}
			if (currQuery.conditionExpr != null) {
				currQuery.conditionExpr.buildArgs(col -> currQuery.getAliasDotName(col),QM,false);
//				currQuery.argValues.addAll(currQuery.conditionExpr.getArgValues());
				currQuery.argTypes.addAll(currQuery.conditionExpr.getArgTypes());
				
			}
			//jdbc types
			currQuery.argJdbcTypes = currQuery.argTypes.stream()
					.map(a->metadataProvider.getJdbcType(a))
					.collect(Collectors.toList());
			// sql语句
			switch (currQuery.queryType) {
			case SELECT:
				buildSelectSql();
				break;
			case INSERT:
				buildInsertSql();
				break;
			case UPDATE:
				buildUpdateSql();
				break;
			case DELETE:
				buildDeleteSql();
				break;
			default:
				buildSelectAllSql();
				break;
			}
			return currQuery;
		}

		@Override
		public IWhere set(SqlExpression attribures) {
			currQuery.setUpdateExpression(attribures);
			return this;
		}
		@Override
		public IBuildable values(SqlExpression attribures) {
			currQuery.setUpdateExpression(attribures);
			return this;
		}
		private MetaCol findColInQuery(String name){
			MetaCol col = currQuery.metaObj.getCol(name);
			if(col==null){
				col = metadataProvider.findCol(name, currQuery.getJoinedObjNames());
			}
			return col;
		}
		/**
		 * 构建连接表达式
		 * @param rightObj 要连接的右边元对象
		 * @param joinOn 连接表达式，例如：msgID=@msgID
		 * @return SqlExpression
		 */
		private SqlExpression buildJoinExpression(MetaObject rightObj, String joinOn){
			String[] operands = joinOn.split("=");
			MetaCol hisCol = rightObj.getCol(operands[0]);
			MetaCol myCol = findColInQuery(operands[1].substring(1));//@name
			return SqlExpression.ofEqual(myCol,hisCol);
		}


		private void buildSelectSql() {
			StringBuilder sb = new StringBuilder();
			boolean withRowNumVar = false;
			var currMetaObj = currQuery.metaObj;
			// select *,fld1,
			sb.append(currQuery.queryType);
			int start = sb.length();
			String colNames = "t." + LIST;
			if(currQuery.selectType.equals(LIST)){
				//rowNum
				if(currQuery.withRowNum){
					String rowNumCol = metadataProvider.toRowNumSql(currQuery.sort);
					withRowNumVar = rowNumCol.indexOf("@ROW_NUMBER")!=-1;//For MySql Only
					//others with sort slot to be resolved when not define sort
					currQuery.withSortSlot = !withRowNumVar && currQuery.sort == null;
					sb.append(rowNumCol).append(",");
				}
				//list
				if (currQuery.selectedCols == null && currMetaObj.getExtendType() != ExtensionType.EXTENDS) {
					//扩展类型不能用 t.*
					sb.append(colNames);
				} else {
					List<String> colList = currQuery.selectedCols.stream().map(c -> currQuery.getAliasDotName(c)).collect(Collectors.toList());
					colNames = String.join(",", colList);
					sb.append(colNames);
				}
			}
			else if(currQuery.selectType.equalsIgnoreCase(COUNT_DISTINCT)){
				//sql server只支持一个字段，mysql允许多个字段组合
				if (currQuery.selectedCols == null) {
					sb.append(colNames);
				} else {
					List<String> colList = currQuery.selectedCols.stream().map(c -> currQuery.getAliasDotName(c)).collect(Collectors.toList());
					colNames = String.join(",", colList);
					sb.append(currQuery.selectType.replace("*",colNames));
				}
			}
			else{
				sb.append(currQuery.selectType);
			}
			currQuery.select = sb.substring(start);

			// from account
			sb.append(FROM);
			start = sb.length();
			String objName=currMetaObj.getObjName();
			sb.append(metadataProvider.getFullObjName(currMetaObj));

			//partition used
			if(currMetaObj.isPartitioned()){
				//_PARTITION_ as a slot which to be replaced according to tenantID
				sb.append(SLOT_PARTITION);
				currQuery.withNamedPartitionSlot = true;
			}
			// as t
			String alias = String.valueOf(ALPHABET[0]);
			currQuery.setAliasName(objName,alias);
			sb.append(" AS ").append(alias);
			if(currMetaObj.isSubObject()){
				var superAlias = alias + "b";
				currQuery.setAliasName(currMetaObj.getSuperName(),superAlias);
				sb.append(INNER_JOIN)
						.append(metadataProvider.quoteName(currMetaObj.getSuperName()))
						.append(" AS ").append(superAlias)
						.append(" ON ");
				var joins = new ArrayList<String>();
				for(var key : currMetaObj.getKeyCols()){
					var keyName = metadataProvider.quoteName(key.getColName());
					var join = alias + "." + keyName + "=" + superAlias + "." + keyName;
					joins.add(join);
				}
				sb.append(String.join(" AND ", joins));
			}

			// join
			if (currQuery.hasJoins()) {
				int i = 1;
				for(SqlJoin join : currQuery.joins){
					join.alias = String.valueOf(ALPHABET[i++]);
					String joinToObjName=join.joinTo.getObjName();
					currQuery.setAliasName(joinToObjName,join.alias);
					sb.append(join.joinType);
					if (!join.joinTo.getDbSchema().equals(currQuery.getMetaObject().getDbSchema()))
						sb.append(join.joinTo.getDbSchema()).append(".");
					sb.append(metadataProvider.quoteName(joinToObjName))
							// named patition
							.append(" AS ").append(join.alias).append(" ON ")
							.append(join.buildExpression());
				}
			}

			currQuery.from = sb.substring(start);
			currQuery.countSql = SELECT + COUNT + FROM + currQuery.from;

			if(withRowNumVar){
				//mysql 8.0以下需要行号的时候
				String rowNumFrom = ",(SELECT @ROW_NUMBER:=0) AS a";
				sb.append(rowNumFrom);
				currQuery.from += rowNumFrom;
			}

			// where
			if (currQuery.conditionExpr != null) {
				currQuery.where = currQuery.conditionExpr.buildExpression(c -> currQuery.getAliasDotName(c), QM);
				sb.append(WHERE).append(currQuery.where);
				currQuery.countSql += WHERE;
				currQuery.countSql += currQuery.where;
			}

			//order by
			// keep for with pager together?
			if(currQuery.sort != null){
				sb.append(SLOT_SORT);
				currQuery.withSortSlot = true;
			}
			currQuery.sql = sb.toString();
		}
		private void buildInsertSql() {
			StringBuilder sb = new StringBuilder();
			//insert into xxx
			sb.append(currQuery.queryType).append(" INTO ")
					.append(metadataProvider.getFullObjName(currQuery.metaObj));
			//partition used
			if(currQuery.metaObj.isPartitioned()){
				//$_PARTITION as a slot which to be replaced according to tenantID
				sb.append(SLOT_PARTITION);
			}
			//(col list)
			currQuery.updateExpr.buildArgs(c -> currQuery.getAliasDotName(c),QM,false);
			String colList = currQuery.updateExpr.getArgNames()
					.stream()
					.map(argName->currQuery.quoteFunc.apply(argName))
					.collect(Collectors.joining(","));
			sb.append('(').append(colList).append(')');
			//values(?,?,...)
			List<String> paramSymbols = currQuery.updateExpr.getArgSymbols();
			sb.append(" VALUES(").append(String.join(",", paramSymbols)).append(')');
			currQuery.sql = sb.toString();
		}
		
		private void buildUpdateSql() {
			StringBuilder sb = new StringBuilder();
			//update xxx
			sb.append(currQuery.queryType).append(metadataProvider.getFullObjName(currQuery.metaObj));
			//partition used
			if(currQuery.metaObj.isPartitioned()){
				//$_PARTITION as a slot which to be replaced according to tenantID
				sb.append(SLOT_PARTITION);
			}
			// as t
			String alias = null;
			if (currQuery.hasJoins()) {
				alias = String.valueOf(ALPHABET[0]);
				sb.append(" AS ").append(alias);
			}

			//set
			sb.append(" SET ");
			currQuery.updateExpr.buildArgs(c -> currQuery.getAliasDotName(c),QM,false);
			List<String> argNames = currQuery.updateExpr.getArgNames();
			List<String> argSymbols = currQuery.updateExpr.getArgSymbols();
			
			List<String> sets = new ArrayList<String>();
			for(int i=0; i<currQuery.updateExpr.getArgNames().size(); i++){
				sets.add(currQuery.quoteFunc.apply(argNames.get(i)) + SqlExpression.EQUAL + argSymbols.get(i));
			}
			sb.append(String.join(",", sets));
			currQuery.updateSet = sb.toString();

			// where
			if (currQuery.conditionExpr != null) {
				currQuery.where = currQuery.conditionExpr.buildExpression(c -> currQuery.getAliasDotName(c), QM);
				sb.append(WHERE).append(currQuery.where);
			}

			currQuery.sql = sb.toString();
		}

		private void buildDeleteSql() {
			StringBuilder sb = new StringBuilder();
			String alias = null;
			if (currQuery.hasJoins()) {
				alias = String.valueOf(ALPHABET[0]);
			}
			//delete
			sb.append(currQuery.queryType)
					.append(alias != null ? alias : "")
					.append(FROM)
					.append(metadataProvider.getFullObjName(currQuery.metaObj));

			//partition used
			if(currQuery.metaObj.isPartitioned()){
				//$_PARTITION as a slot which to be replaced according to tenantID
				sb.append(SLOT_PARTITION);
			}

			// as t
			if (alias != null)
				sb.append(" AS ").append(alias);

			currQuery.deleteFrom = sb.toString();

			// where
			if (currQuery.conditionExpr != null) {
				currQuery.where = currQuery.conditionExpr.buildExpression(c -> currQuery.getAliasDotName(c), QM);
				sb.append(WHERE).append(currQuery.where);
			}

			currQuery.sql = sb.toString();
		}

		private void buildSelectAllSql(){
			StringBuilder sb = new StringBuilder();
			var currMetaObj = currQuery.metaObj;

			//exec
			sb.append(SELECT).append(LIST).append(FROM);
			int start = sb.length();
			sb.append(metadataProvider.getFullObjName(currMetaObj));
			//partition used
			if(currMetaObj.isPartitioned()){
				//$_PARTITION as a slot which to be replaced according to tenantID
				sb.append(SLOT_PARTITION);
				currQuery.withNamedPartitionSlot = true;
			}
			// as t
			String alias = String.valueOf(ALPHABET[0]);
			currQuery.setAliasName(currMetaObj.getObjName(),alias);
			sb.append(" AS ").append(alias);
			if(currMetaObj.isSubObject()){
				var superAlias = alias + "b";
				currQuery.setAliasName(currMetaObj.getSuperName(),superAlias);
				sb.append(INNER_JOIN)
						.append(metadataProvider.quoteName(currMetaObj.getSuperName()))
						.append(" AS ").append(superAlias)
						.append(" ON ");
				var joins = new ArrayList<String>();
				for(var key : currMetaObj.getKeyCols()){
					var keyName = metadataProvider.quoteName(key.getColName());
					var join = alias + "." + keyName + "=" + superAlias + "." + keyName;
					joins.add(join);
				}
				sb.append(String.join(" AND ", joins));
			}


			// join
			if (currQuery.hasJoins()) {
				int i = 1;
				for(SqlJoin join : currQuery.joins){
					join.alias = String.valueOf(ALPHABET[i++]);
					sb.append(join.joinType).append(metadataProvider.quoteName(join.joinTo.getObjName()))
							.append(" AS ").append(ALPHABET[i]).append(" ON ")
							.append(join.buildExpression());
				}
			}
			int end = sb.length();
			String from = sb.substring(start, end);
			currQuery.countSql = SELECT + COUNT + FROM + from;

			// where
			if (currQuery.conditionExpr != null) {
				currQuery.where = currQuery.conditionExpr.buildExpression(c -> currQuery.getAliasDotName(c), QM);
				sb.append(WHERE).append(currQuery.where);
				currQuery.countSql += WHERE;
				currQuery.countSql += currQuery.where;
			}
			//order by
			if(currQuery.sort != null){
				sb.append(SLOT_SORT);
				currQuery.withSortSlot = true;
			}
			currQuery.sql = sb.toString();
		}
	}

	////////////////////////////////////
	// Appender
	///////////////////////////////////
	public SqlQueryExecutor executor(){
		return new SqlQueryExecutor(this);
	}
	/**
	 * 基于SqlQuery添加额外的条件、分页、排序构建新的查询
	 */
	public static class SqlQueryExecutor {
		final SqlQuery query;
		String where;
		String orderBy;

		boolean limited;

		public SqlQueryExecutor(final SqlQuery query){
			this.query = query;
			this.where = query.getBuiltCondition();
			this.orderBy = "";
			this.limited = false;
		}

		public SqlQueryExecutor and(final String condition){
			if(BaseUtil.hasText(where))
				where = String.format("(%1$s) AND (%2$s)",where,condition);
			else
				where = condition;
			return this;
		}
		public SqlQueryExecutor preAnd(final String condition){
			if(BaseUtil.hasText(where))
				where = String.format("(%1$s) AND (%2$s)",condition,where);
			else
				where = condition;
			return this;
		}
		public SqlQueryExecutor or(final String condition){
			if(BaseUtil.hasText(where))
				where = String.format("(%1$s) OR (%2$s)",where,condition);
			else
				where = condition;
			return this;
		}

		public SqlQueryExecutor searchWord(final String word){
//			if(BaseUtils.hasText(where)) {
//				where = where.replaceAll(SqlExpression.SEARCH_WORD, "'%" + word + "%'");
//			}
			if(BaseUtil.hasText(where)) {
				StringBuilder sb = new StringBuilder(where);
				int index = sb.indexOf(SqlExpression.SEARCH_WORD);
				while (index != -1) {
					sb.replace(index, index + SqlExpression.SEARCH_WORD.length(), "'%" + word + "%'");
					index = sb.indexOf(SqlExpression.SEARCH_WORD);
				}
				where = sb.toString();
			}
			return this;
		}

		public SqlQueryExecutor orderBy(final String sort){
			Objects.requireNonNull(sort);
			this.orderBy = ORDER_BY + sort;
			return this;
		}
		public SqlQueryExecutor orderBy(final Sort sort){
			return orderBy(sort.toString());
		}

		private SqlQueryExecutor addPartitionCondition(int tenantID, final DbMetadataProvider metadataProvider){
			StringBuilder sb = new StringBuilder();
			MetaObject metaObj = query.metaObj;
			String partitionCond = sb.append("t.").append(metaObj.getPartitionKey())
					.append(SqlOperator.BETWEEN.getOp())
					.append(Tenancy.buildEntityID(tenantID, BaseUtil.getValueOrZero(metaObj.getMinID())))
					.append(SqlOperator.AND.getOp())
					.append(Tenancy.buildEntityID(tenantID, BaseUtil.getValueOrDefault(metaObj.getMaxID(), Tenancy.MAX_REAL_ID)))
					.toString();
			return preAnd(partitionCond);
		}
		private String resolveNamedPartition(final String from, int tenantID, final DbMetadataProvider metadataProvider){
			return from.replaceAll(SLOT_PARTITION, metadataProvider.getPartitionName(tenantID));
		}


		//region select
		public String select(){
			return SELECT + query.getBuiltSelect() +
					FROM + query.getBuiltFrom() +
					(BaseUtil.hasText(where) ? WHERE + where : "") +
					orderBy;
		}

		public String select(int tenantID, final DbMetadataProvider metadataProvider){
			if(query.withNamedPartitionSlot)
				return resolveNamedPartition(select(),tenantID,metadataProvider);
			return addPartitionCondition(tenantID,metadataProvider).select();
		}
		public String select(final Paginator paginator, final DbMetadataProvider metadataProvider, final JdbcTemplate jdbcTemplate){
			if(BaseUtil.isNullOrWhitesapce(paginator.getOrderBy())){
				if(BaseUtil.hasText(orderBy)) paginator.setOrderBy(orderBy.replace(ORDER_BY,""));
				else paginator.setOrderBy(query.defaultSort);
			}
			orderBy = metadataProvider.toPagerSql(paginator);

			//count
			int rc = jdbcTemplate.queryForObject(count(), Integer.class);
			paginator.setRecordCount(rc);

			return select();
		}

		/**
		 * 支持命名分区的时候使用，否则自行添加租户隔离条件
		 * @param tenantID
		 * @param paginator
		 * @param metadataProvider
		 * @return
		 */
		public String select(int tenantID, final Paginator paginator, final DbMetadataProvider metadataProvider, final JdbcTemplate jdbcTemplate){
			if(query.withNamedPartitionSlot)
				return resolveNamedPartition(select(paginator,metadataProvider,jdbcTemplate),tenantID,metadataProvider);
			return addPartitionCondition(tenantID,metadataProvider).select(paginator,metadataProvider,jdbcTemplate);
		}
		//endregion of select

		//region count
		public String count(){
			return SELECT + COUNT +
					FROM + query.getBuiltFrom() +
					(BaseUtil.hasText(where) ? WHERE + where : "");
		}
		//endregion of count

		//region update
		public String update(){
			return query.updateSet + (BaseUtil.hasText(where) ? WHERE + where : "");
		}
		public String update(int tenantID, final DbMetadataProvider metadataProvider){
			if(query.withNamedPartitionSlot)
				return resolveNamedPartition(update(),tenantID,metadataProvider);
			return addPartitionCondition(tenantID,metadataProvider).update();
		}
		//endregion of update

		//region delete
		public String delete(){
			return query.deleteFrom + (BaseUtil.hasText(where) ? WHERE + where : "");
		}
		public String delete(int tenantID, final DbMetadataProvider metadataProvider){
			if(query.withNamedPartitionSlot)
				return resolveNamedPartition(delete(),tenantID,metadataProvider);
			return addPartitionCondition(tenantID,metadataProvider).delete();
		}
		//endregion of delete
	}
}
