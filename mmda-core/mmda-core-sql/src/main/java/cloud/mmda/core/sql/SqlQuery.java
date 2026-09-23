package cloud.mmda.core.sql;

import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.lamda.LambdaGetter;
import cloud.mmda.core.lamda.LambdaUtil;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.sql.dialects.SqlDialect;
import cloud.mmda.core.sql.expressions.SqlCriteriaExp;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.CollectionUtil;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

import static cloud.mmda.core.sql.SqlConsts.*;
/**
 * Sql查询
 * <p>
 * 查询基于元数据构建，未编译{@link #compile(SqlDialect)}之前与方言{@link SqlDialect}无关，可编译为
 * 不同的数据库方言SQL语句，用于执行查询。
 * 
 * <p>
 *     通过{@link Builder}创建一个查询，调用静态函数{@link SqlQuery#create(MetadataProvider)}
 *     <pre>
 *         {@code
 *           var oracleDialect = new OracleDialect();
 *           var q = SqlQuery.create(metadataProvider)
 *              .from(Order.class, Order::getPartner) //Eager 模式加载Partner关联对象
 *              //构建条件表达式，参考SqlCriteriaExp
 *              .where(SqlExp.criteria(Partner::getPartnerRoles).eq(PartnerRoles.SUPPLIER))
 *              .orderBy(Partner::getPartnerCode)
 *              .selectAll();
 *           var script = q.compile(oracleDialect); //编译查询为Oracle方言
 *           //获取查询语句
 *           var sql = script.getSql();
 *           var sqlCount = script.getCountSql();
 *           //获取参数列表
 *           var argList = q.getArgList();
 *           //你可以将查询缓存，以备后用...
 *           var pagedQuery = script.page(10, 1); //分页查询
 *           var orderedQuery = script.orderBy(“t.partnerID”);//改变排序方式
 *           //然后你可以
 *         }
 *     </pre>
 * </p>
 * 你可以通过{@link SqlDialect#orderByClause(Sort, MetaContext)} 实例化不同排序规则的查询脚本，避免每次重新编译整个SQL查询，
 * 你可以通过{@link #page(SqlDialect, int, int)}获得分页查询语句，避免重新编译SQL查询。
 */
public class SqlQuery implements SqlMetaContext {

//    @Getter
//    private String name;

    private String queryType; //SELECT,COUNT_ALL,COUNT_ALL_DISTINCT

    private MetaObject metaObj;

    @Getter
    private List<SqlJoin> joins;
    @Getter
    private LinkedHashMap<MetaRelation,MetaObject> relatives;

    private boolean withoutRowNum = false;
    private LinkedHashMap<String, MetaCol> namedCols;//alias => col
    @Getter
    private SqlCriteriaExp condition;
    private Sort sort;

    private SqlQueryable sqlQueryable; //最后一次编译后的结果
    private HashMap<String, SqlQueryable> sqlQueryableMap;

    private SqlQuery() {
        this.namedCols = new LinkedHashMap<>();
        this.joins = new ArrayList<>();
        this.relatives = new LinkedHashMap<>();
    }

    @Override
    public MetaObject get() {
        return metaObj;
    }

    public boolean isCompiled(){
        return sqlQueryable != null;
    }
    public boolean isCompiled(final String dialectName){
        if(!isCompiled() || sqlQueryableMap == null || sqlQueryableMap.isEmpty()) return false;
        return sqlQueryableMap.containsKey(dialectName);
    }

    public SqlQueryable getSqlQueryable() {
        if(!isCompiled()) throw new IllegalStateException("SqlQuery is not compiled");
        return sqlQueryable;
    }
    public SqlQueryable getSqlQueryable(final SqlDialect dialect) {
        return compile(dialect);
    }


    /**
     * 获取查询编译后的参数列表
     * @return 参数列表
     * @throws IllegalStateException 查询未编译
     */
    public List<SqlArg> getArgList() throws IllegalStateException {
        if(!isCompiled()) throw new IllegalStateException("You must compile this query before you can get arguments");
        if(!sqlQueryable.hasWhereClause()) return Collections.emptyList();
        return condition.getParametricExpression().argList();
    }

    /**
     * 编译查询为{@code dialect}方言的脚本，你还可以通过
     * <pre>
     *     {@code
     *     //获取编译后的Sql脚本，若未曾调用此函数则会抛出异常
     *     SqlQuery.getSqlScript();
     *
     *     // 获取方言 dialect 编译后的Sql脚本，若未编译会自动调用此函数
     *     SqlQuery.getSqlScript(dialect);
     *     }
     * </pre>
     * @param dialect SQL方言
     * @return SQL查询脚本，包含 SELECT & COUNT 语句
     */
    public SqlQueryable compile(final SqlDialect dialect) {
        //不要重复编译
        if(isCompiled(dialect.name())) return sqlQueryableMap.get(dialect.name());


        //select
        String selectClause;
        if(queryType.startsWith(COUNT)) {
            selectClause = queryType;
        }
        else{
            if (namedCols != null && !namedCols.isEmpty()) {
                selectClause = namedCols.entrySet().stream()
                        .map(e -> dialect.quoteCol(e.getValue(), this, e.getKey()))
                        .collect(Collectors.joining(COMMA));
            } else {
                selectClause = ASTERISK;
            }
        }

        // from
        var fromClause = dialect.fromClause(this);

        // where
        String whereClause = EMPTY;
        List<SqlArg> argList = new ArrayList<>();
        if (condition != null) {
            var compiledExp = condition.compile(dialect, this, true);
            whereClause = compiledExp.expression();
            argList = compiledExp.argList();
            if(BaseUtil.hasText(metaObj.getPartitionKey())){
                whereClause = dialect.quoteObject("t", metaObj.getPartitionKey())
                        + BETWEEN + "? AND ?" + AND
                        + whereClause;
            }
        }
        else{
            if(BaseUtil.hasText(metaObj.getPartitionKey())){
                whereClause = dialect.quoteObject("t", metaObj.getPartitionKey())
                        + BETWEEN + "? AND ?";
            }
        }

        // order by & row num
        List<MetaRelation> relations = CollectionUtil.hasAny(joins)
                ? joins.stream().map(j -> j.getRelation()).collect(Collectors.toList())
                : Collections.emptyList();
        var orderByClause = dialect.orderByClause(sort, this);

        //query sql
        sqlQueryable = new SqlQueryable(dialect,selectClause,fromClause,whereClause,orderByClause, argList);
        if(sqlQueryableMap == null) sqlQueryableMap = new HashMap<>();
        sqlQueryableMap.put(dialect.name(), sqlQueryable);

        return sqlQueryable;
    }

    private void addJoin(SqlJoin join) {
        if(relatives.containsKey(join.getRelation())) return;
        if(joins.stream().anyMatch(j -> j.getName().equals(join.getName()))) {
            throw new IllegalStateException("relation " + join.getName() + " duplicated");
        }

        joins.add(join);
        relatives.put(join.getRelation(),join.getRelative());
    }
    private SqlQuery addSort(Sort sort){
        if(sort == null) return this;

        if(this.sort == null) this.sort = sort;
        else this.sort.thenBy(sort);
        return this;
    }
    /**
     * 浅拷贝一个Sql查询，之后你可以修改排序方式、分页
     * @return 新的SQL查询对象
     */
    public SqlQuery shallowCopy(){
        SqlQuery copy = new SqlQuery();
        copy.queryType = queryType;
        copy.metaObj = metaObj;
        copy.joins = joins;
        copy.withoutRowNum = withoutRowNum;
        copy.namedCols = new LinkedHashMap<>(namedCols);
        copy.condition = condition;
        copy.sort = sort;

        copy.sqlQueryable = sqlQueryable;
        return copy;
    }

    /**
     * 获取分页查询语句，不会修改也不会多次编译源查询
     * @param dialect SQL 方言
     * @param pageSize 页面大小，即每页几条数据
     * @param pageNo 页序号从1开始
     * @return 新的分页SQL查询语句
     */
    public final String page(final SqlDialect dialect, int pageSize, int pageNo) {
        return compile(dialect).sql() + dialect.limitClause(pageSize, pageNo);
    }


    /**
     * 创建一个查询构建器，这是你创建新查询的唯一入口
     * @param metadataProvider 元数据提供者，负责提供{@link MetaObject}等元数据
     * @return 一个查询构建器
     */
    public static IQuerySource create(MetadataProvider metadataProvider) {
        return new Builder(metadataProvider);
    }

    //region 查询接口 Fluent API
    public interface IQuerySource {
        IQueryWhere from(MetaObject o);
        <T> IQueryWhere from(Class<T> entityClass);
    }

    public interface IQueryWhere extends IQuerySortable{
        IQueryWhere withAll(Collection<MetaRelation> relations);
        IQueryWhere withAll();
        IQueryWhere with(MetaRelation relation);
        IQueryWhere with(String relationName);
        <T> IQueryWhere with(LambdaGetter<T,?> relative);

        IQuerySortable where(SqlCriteriaExp w);
    }
    public interface IQuerySortable extends IQuerySelectable{
        IQuerySelectable orderBy(Sort sort);
        IQuerySelectable orderBy(String...fields);
        IQuerySelectable orderByDesc(String...fields);
        <T> IQuerySelectable orderBy(final LambdaGetter<T,?>...fields);
        <T> IQuerySelectable orderByDesc(LambdaGetter<T,?>...fields);
        IQuerySelectable orderByDefault();
    }

    public interface IQuerySelectable {
        SqlQuery select(String... colNames);
        <T> SqlQuery select(LambdaGetter<T,?>... fields);
        SqlQuery selectAll();
        SqlQuery count();
        SqlQuery countDistinct();
    }
    //endregion


    /**
     * Sql 查询构建器支持你使用 Fluent API 方式创建 {@link SqlQuery}。
     * <pre>
     *     {@code
     *           var q = SqlQuery.create(queryName, metadataProvider)
     *              .from(Order.class, Order::getPartner) //Eager 模式加载Partner关联对象
     *              //构建条件表达式，参考SqlCriteriaExp
     *              .where(SqlExp.criteria(Partner::getPartnerRoles).eq(PartnerRoles.SUPPLIER))
     *              .orderBy(Partner::getPartnerCode)
     *              .selectAll();
     *     }
     * </pre>
     */
    public static class Builder implements IQuerySource, IQueryWhere {
        private final MetadataProvider metadataProvider;
        private final SqlQuery query;

        public Builder(MetadataProvider metadataProvider) {
            this.query = new SqlQuery();
            this.metadataProvider = metadataProvider;
        }

        public final <T> IQueryWhere from(Class<T> entityClass) {
            query.metaObj = metadataProvider.getMetaObject(entityClass);
            return this;
        }

        public IQueryWhere from(MetaObject o) {
            Objects.requireNonNull(o, "Meta object must not be null");
            query.metaObj = o;
            return this;
        }

        public IQueryWhere with(final MetaRelation relation) {
            Objects.requireNonNull(relation, "Meta relation cannot be null");
            var join = SqlJoin.of(query.metaObj, relation, metadataProvider.getRelativeMetaObject(relation));
            query.addJoin(join);
            return this;
        }
        public IQueryWhere withAll(final Collection<MetaRelation> relations) {
            if(CollectionUtil.isNullOrEmpty(relations)) return this;
            relations.stream()
                    .filter(r -> r.hasOneOrMany())
                    .map(relation -> SqlJoin.of(query.metaObj, relation, metadataProvider.getRelativeMetaObject(relation)))
                    .forEach(join -> query.addJoin(join));
            return this;
        }
        public IQueryWhere withAll() {
            return withAll(query.metaObj.getRelations());
        }
        public IQueryWhere with(String relationName){
            var relation = query.metaObj.getRelation(relationName);
            if(relation == null) throw new IllegalArgumentException(relationName + " relation not found in" + query.metaObj.getObjName());
            var join = SqlJoin.of(query.metaObj, relation, metadataProvider.getRelativeMetaObject(relation));
            query.addJoin(join);
            return this;
        }
        public <R> IQueryWhere with(LambdaGetter<R,?> relative) {
            var relationName = LambdaUtil.getFieldName(relative);
            return with(relationName);
        }

        public IQuerySortable where(SqlCriteriaExp w) {
            query.condition = w;
            return this;
        }
        public IQuerySelectable orderBy(Sort sort){
            query.addSort(sort);
            return this;
        }
        public IQuerySelectable orderByDefault(){
            query.sort = Sort.byKey(query.metaObj);
            return this;
        }
        public IQuerySelectable orderBy(String...fields){
            for(var field : fields){
                query.addSort(Sort.of(field));
            }
            return this;
        }
        public IQuerySelectable orderByDesc(String...fields){
            for(var field : fields){
                query.addSort(Sort.of(field, Sort.Order.DESC));
            }
            return this;
        }

        @SafeVarargs
        public final <T> IQuerySelectable orderBy(LambdaGetter<T,?>...fields){
            for(var field : fields){
                var fieldName = LambdaUtil.getFieldName(field);
                query.addSort(Sort.of(fieldName));
            }
            return this;
        }
        @SafeVarargs
        public final <T> IQuerySelectable orderByDesc(LambdaGetter<T,?>...fields){
            for(var field : fields){
                var fieldName = LambdaUtil.getFieldName(field);
                query.addSort(Sort.of(fieldName, Sort.Order.DESC));
            }
            return this;
        }

        public final SqlQuery select(String... colNames) {
            query.queryType = ASTERISK;
            if (query.namedCols == null) {
                query.namedCols = new LinkedHashMap<>();
            }
            if (query.metaObj != null) {
                for (String colName : colNames) {
                    MetaCol col = query.metaObj.getCol(colName);
                    if (col != null) {
                        query.namedCols.put(colName, col);
                    }
                }
            }
            return query;
        }

        @SafeVarargs
        public final <T> SqlQuery select(LambdaGetter<T,?>... fields) {
            query.queryType = ASTERISK;
            if (query.namedCols == null) {
                query.namedCols = new LinkedHashMap<>();
            }
            for (var field : fields) {
                var colName = LambdaUtil.getFieldName(field);
                var col = query.metaObj.getCol(colName);
                if (col != null) {
                    query.namedCols.put(colName, col);
                }
            }
            return query;
        }


        public SqlQuery selectAll() {
            query.queryType = ASTERISK;
            query.namedCols.clear();
            return query;
        }

        public SqlQuery count() {
            query.queryType = COUNT_ALL;
            return query;
        }

        public SqlQuery countDistinct() {
            query.queryType = COUNT_ALL_DISTINCT;
            return query;
        }
    }
}
