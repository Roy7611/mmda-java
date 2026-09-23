package cloud.mmda.core.sql.dialects;

import cloud.mmda.core.Tenancy;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.enums.MetaForeignKeyAction;
import cloud.mmda.core.lamda.LambdaGetter;
import cloud.mmda.core.lamda.LambdaUtil;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.sql.*;
import cloud.mmda.core.sql.expressions.SqlArithmeticExp;
import cloud.mmda.core.sql.expressions.SqlCriteriaExp;
import cloud.mmda.core.sql.expressions.SqlExp;
import cloud.mmda.core.sql.expressions.SqlOp;
import cloud.mmda.core.utils.BaseUtil;
import cloud.mmda.core.utils.CollectionUtil;
import lombok.Getter;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cloud.mmda.core.sql.SqlConsts.*;

public abstract class AnsiSqlDialect implements SqlDialect{
    @Getter
    private final MetadataProvider metadataProvider;

    public AnsiSqlDialect(MetadataProvider metadataProvider) {
        this.metadataProvider = metadataProvider;
    }

    public abstract String name();

    @Override
    public String quote(String identifier) {
        return "\""+identifier+"\"";
    }
    @Override
    public String unquote(String identifier) {
        return identifier.replace("\"","");
    }
    @Override
    public String fromClause(final MetaContext context) {
        var sb = new StringBuilder();
        var source = context.get();
        sb.append(quoteObject(source)).append(AS).append(T);
        //如果是子类对象
        if(source.isSubObject()){
            var superAlias = source.getObjectAlias(source.getSuperName());
            var superNames = source.getSuperName().split("\\.");
            if(superNames.length > 1){
                sb.append(INNER_JOIN).append(quoteObject(superNames[0], superNames[1]));
            }
            else {
                sb.append(INNER_JOIN).append(quoteObject(source.getDbSchema(), superNames[0]));
            }
            var joinExp = source.getKeyCols().stream()
                    .map(keyCol -> T_DOT + quote(keyCol) + EQUALS + superAlias + DOT + quote(keyCol))
                    .collect(Collectors.joining(AND));
            sb.append(AS).append(superAlias)
                    .append(ON).append(joinExp);
        }
        //如果有加载关联关系
        if(context instanceof SqlMetaContext sqlMetaContext){
            var joins = sqlMetaContext.getJoins();
            if(BaseUtil.hasAny(joins)){
                for(var join : joins){
                    var r = join.getRelation();
                    sb.append(join.getJoinType())
                            .append(quoteObject(r.getRelativeDbSchema(), r.getRelativeObjName()))
                            .append(AS).append(r.getRelationName())
                            .append(ON).append(join.compile(this));
                }
            }
        }
        else{
            if(CollectionUtil.hasAny(context.getRelatives())){
                context.getRelatives().keySet().stream()
                        .forEach(relation -> {
                            sb.append(relation.getJoinType())
                                    .append(quoteObject(relation.getDbSchema(), relation.getRelativeObjName()))
                                    .append(AS).append(relation.getRelationName())
                                    .append(ON).append(quoteJoinExp(relation));
                        });
            }
        }

        if(supportNamedPartition() && source.isPartitioned()){
            sb.append(" PARTITION(p?)");//指定租户分区
        }
        return sb.toString();
    }


    /**
     * 给定元对象上下文{@code context}，给{@code sort}排序设置加引号
     * @param sort 排序
     * @param context 元对象上下文
     * @return 排序SQL语句，例如：{@code t.`lastModified` DESC, t.`amount` ASC}
     */
    private String quoteSort(final Sort sort, final MetaContext context) {
        String sortFld;
        if(sort.getSortBy() instanceof MetaCol col){
            //字段排序
            var alias = context.getObjectAlias(col.getObjName());
            sortFld = alias + DOT + quoteCol(col) ;
        }
        else if(sort.getSortBy() instanceof LambdaGetter<?,?> getter){
            var entityClass = LambdaUtil.getImplClass(getter);
            var alias = context.getObjectAlias(entityClass.getSimpleName());
            var colName = LambdaUtil.getFieldName(getter);
            sortFld = alias + DOT + quote(colName);
        }
        else if(sort.getSortBy() instanceof String colName){
            //字符串尝试识别为字段，例如t.orderNo
            var col = context.findCol(colName);
            if(col.isPresent()) sortFld = quoteCol(col.get(), context, null);
            else sortFld = colName;
        }
        else if(sort.getSortBy() instanceof SqlExp exp){
            //支持表达式排序，慎用
            sortFld = exp.toSql(this, context);
        }
        else{
            sortFld = sort.getSortBy().toString();
        }
        return sortFld + SPACE + sort.getSortOrder().name();
    }

    @Override
    public String orderByClause(@Nullable final Sort sort, MetaContext context){
        if(sort == null) return quoteKeyCols(context.get());
        return sort.stringify((s) -> quoteSort(s, context));
    }



    protected String whereClause(MetaContext context, SqlCriteriaExp whereExp, List<SqlArg> argList, boolean useContextAlias){
        var condition = EMPTY;
        var target = context.get();
        if(whereExp != null){
            var expResult = whereExp.compile(this,context, useContextAlias);
            condition = expResult.expression();
            if(BaseUtil.hasText(target.getPartitionKey())){
                condition = addPartitionArgs(target, argList,useContextAlias) + AND + condition;
            }
            argList.addAll(expResult.argList());
        }
        else {
            if(BaseUtil.hasText(target.getPartitionKey())){
                condition = addPartitionArgs(target, argList,useContextAlias);
            }
        }
        return condition;
    }

    private String addPartitionArgs(MetaObject target, List<SqlArg> argList, boolean useContextAlias){
        Function<Integer,Long> getMinId = (tenantId) -> Tenancy.getMinEntityID(tenantId, target);
        Function<Integer,Long> getMaxId = (tenantId) -> Tenancy.getMaxEntityID(tenantId, target);
        argList.add(new SqlArg(MIN_ID, DataType.INT64, getMinId));
        argList.add(new SqlArg(MAX_ID, DataType.INT64, getMaxId));
        return "(" + (useContextAlias ? quoteObject(T, target.getPartitionKey()) : quote(target.getPartitionKey()))
                + BETWEEN + "? AND ?)";
    }
    //region SELECT 查询

    @Override
    public SqlQueryable select(final MetaObject source,
                               final List<MetaRelation> withRelations,
                               final SqlCriteriaExp condition,
                               final Sort orderBy){
        Objects.requireNonNull(source, "query source cannot be null");
        var query = SqlQuery.create(metadataProvider)
                .from(source)
                .withAll(withRelations)
                .where(condition)
                .orderBy(orderBy)
                .selectAll();
        return query.compile(this);
    }

    //endregion of select

    //region INSERT
    @Override
    public SqlCmd insert(final MetaObject metaObj, boolean namedParameter){
        var sb = new StringBuilder();
        var argList = new ArrayList<SqlArg>();

        // INSERT INTO table_name (
        sb.append(INSERT_INTO).append(quoteObject(metaObj)).append(PARENTHESES_START);
        // col1, col2, ……
        var colsJoiner = new StringJoiner(COMMA);
        var valuesJoiner = new StringJoiner(COMMA);
        if(namedParameter){
            //命名参数 :colName
            for(var col : metaObj.getInsertableCols()){
                colsJoiner.add(quote(col.getColName()));
                argList.add(SqlArg.of(col));
                valuesJoiner.add(quote(COLON+col.getColName()));
            }
        }
        else{
            //占位符 ?
            for(var col : metaObj.getInsertableCols()){
                colsJoiner.add(quote(col.getColName()));
                argList.add(SqlArg.of(col));
                valuesJoiner.add(PLACEHOLDER);
            }
        }
        sb.append(colsJoiner).append(PARENTHESES_END);
        //) VALUES (
        sb.append(VALUES).append(PARENTHESES_START)
                .append(valuesJoiner)
                .append(PARENTHESES_END);
        //)
        return SqlCmd.insert(sb.toString(), argList);
    }
    //endregion of INSERT

    //region UPDATE

    @Override
    public SqlCmd update(final MetaObject target, final LinkedHashMap<MetaCol,Object> assignments, final SqlCriteriaExp condition, boolean namedParameter) {
        Objects.requireNonNull(target, "target must not be null");
        if(CollectionUtil.isNullOrEmpty(assignments)) throw new IllegalArgumentException("assignments must not be null or empty");
        var argList = new ArrayList<SqlArg>();

        // UPDATE table SET
        var sb = new StringBuilder(UPDATE)
                .append(quoteObject(target))
                .append(SET);

        // col1 = ?, col2 = ?
        var sj = new StringJoiner(COMMA);
//        Function<MetaCol,String> fn = namedParameter
//                ? col -> quote(col.getColName()) + EQUALS + COLON + col.getColName()
//                : col -> quote(col.getColName()) + EQUALS + PLACEHOLDER;
        assignments.forEach((col, value)->{
            if(value instanceof SqlArithmeticExp exp){
                var pe = exp.compile(this, target, false);
                argList.addAll(pe.argList());
                sj.add(quote(col.getColName()) + EQUALS + pe.expression());
            }
            else if(value instanceof MetaCol valueCol){
                sj.add(quote(col.getColName()) + EQUALS + quote(valueCol.getColName()));
            }
            else{
                argList.add(SqlArg.of(col, value));
                sj.add(quote(col.getColName()) + EQUALS + PLACEHOLDER);
            }

        });
        sb.append(sj);

        // WHERE condition
        var whereClause = whereClause(target, condition, argList, false);
        if(!EMPTY.equals(whereClause)) sb.append(WHERE).append(whereClause);

        var sql = sb.toString();
        var cmdId = sql.hashCode();
        return new SqlCmd(sql, argList, UPDATE+cmdId);
    }
    /**
     * {@inheritDoc}
     * <pre>
     *     {@code
     *     UPDATE t SET t.col = ?
     *     FROM table t INNER JOIN table2 b ON ...
     *     WHERE where_condition
     *
     *     }
     * </pre>
     *
     */
    @Override
    public SqlCmd update(final SqlMetaContext context, final LinkedHashMap<MetaCol,Object> assignments, final SqlCriteriaExp condition, boolean namedParameter) {
        Objects.requireNonNull(context, "context must not be null");
        if(CollectionUtil.isNullOrEmpty(context.getJoins())) throw new IllegalArgumentException("joins of the context must not be null or empty");
        if(CollectionUtil.isNullOrEmpty(assignments)) throw new IllegalArgumentException("assignments must not be null or empty");


        var argList = new ArrayList<SqlArg>();

        // UPDATE t SET
        var sb = new StringBuilder(UPDATE)
                .append(T)
                .append(SET);

        // t.col1 = ?, t.col2 = r.col3 + ?
        var sj = new StringJoiner(COMMA);
        assignments.forEach((col, value)->{
            if(value instanceof SqlArithmeticExp exp){
                var pe = exp.compile(this, context, true);
                argList.addAll(pe.argList());
                sj.add(T_DOT + quote(col.getColName()) + EQUALS + pe.expression());
            }
            else if(value instanceof MetaCol valueCol){
                var valueAlias = context.getObjectAlias(valueCol.getObjName());
                sj.add(T_DOT + quote(col.getColName()) + EQUALS + valueAlias + DOT + quote(valueCol.getColName()));
            }
            else{
                argList.add(SqlArg.of(col, value));
                sj.add(T_DOT + quote(col.getColName()) + EQUALS + PLACEHOLDER);
            }
        });
        sb.append(sj);

        //FROM clause
        sb.append(fromClause(context));

        // WHERE condition
        var whereClause = whereClause(context, condition, argList, true);
        if(!EMPTY.equals(whereClause)) sb.append(WHERE).append(whereClause);

        var sql = sb.toString();
        var cmdId = sql.hashCode();
        return new SqlCmd(sql, argList, UPDATE+cmdId);
    }
    //endregion of update

    //region DELETE

    /**
     * {@inheritDoc}
     * <pre>
     *     {@code
     *     DELETE t
     *     FROM table0 AS t
     *      INNER JOIN table1 AS b ON t.id = b.id
     *     WHERE where_condition
     *     }
     * </pre>
     */
    @Override
    public SqlCmd delete(final SqlMetaContext context, final SqlCriteriaExp condition){
        Objects.requireNonNull(context, "context must not be null");
        if(CollectionUtil.isNullOrEmpty(context.getJoins())) throw new IllegalArgumentException("Sql joins must not be null or empty");
        var argList = new ArrayList<SqlArg>();
        var sb = new StringBuilder(DELETE).append(T)
                .append(fromClause(context));

        // WHERE condition
        var whereClause = whereClause(context, condition, argList, true);
        if(!EMPTY.equals(whereClause)) sb.append(WHERE).append(whereClause);

        var sql = sb.toString();
        var cmdId = sql.hashCode();
        return new SqlCmd(sql, argList, DELETE+cmdId);
    }

    @Override
    public SqlCmd delete(final MetaObject target, final SqlCriteriaExp condition){
        var sb = delete(target);
        var argList = new ArrayList<SqlArg>();

        var whereClause = whereClause(target, condition, argList, false);
        if(!EMPTY.equals(whereClause)) sb.append(WHERE).append(whereClause);

        var sql = sb.toString();
        var cmdId = sql.hashCode();
        return new SqlCmd(sql, argList, DELETE+cmdId);
    }
    //endregion of DELETE

    //region DDL
    @Override
    public String drop(MetaObject metaObj, boolean ifExists){
        char objType = metaObj.getObjType().charAt(0);
        return  switch(objType) {
            case 'T' -> (ifExists ? DROP_TABLE + IF_EXISTS : DROP_TABLE) + quoteObject(metaObj) + SEMICOLON;
            case 'V' -> (ifExists ? DROP_VIEW + IF_EXISTS : DROP_VIEW) + quoteObject(metaObj) + SEMICOLON;
            case 'P' -> (ifExists ? DROP_PROC + IF_EXISTS : DROP_PROC) + quoteObject(metaObj) + SEMICOLON;
            case 'F' -> (ifExists ? DROP_FUNC + IF_EXISTS : DROP_FUNC) + quoteObject(metaObj) + SEMICOLON;
            default -> throw new IllegalStateException("Unexpected value: " + objType);
        };
    }

    @Override
    public StringBuilder createTable(MetaObject metaObj, boolean ifNotExists, boolean dropIfExists, boolean withComment){
        var sb = new StringBuilder();
        if(dropIfExists) sb.append(drop(metaObj, true)).append(LINE_SEPARATOR);

        sb.append(CREATE_TABLE).append(ifNotExists ? IF_NOT_EXISTS : EMPTY).append(quoteObject(metaObj))
                .append(PARENTHESES_START).append(LINE_SEPARATOR);
        //columns
        metaObj.getCols().stream()
                .forEach(col -> defineCol(sb.append(TAB), col, withComment).append(COMMA).append(LINE_SEPARATOR));

        addTableConstraints(sb, metaObj);

        sb.append(LINE_SEPARATOR).append(PARENTHESES_END).append(LINE_SEPARATOR);
        return sb;
    }

    /**
     * 添加表约束，包括主键、外键、索引、唯一约束
     * @param sb
     * @param metaObj
     */
    protected void addTableConstraints(StringBuilder sb, MetaObject metaObj){
        //pk
        definePrimaryKey(sb.append(TAB), metaObj);
        //indexes
//        metaObj.getIndexes().stream()
//                .forEach(idx -> defineIndex(sb.append(COMMA).append(LINE_SEPARATOR).append(TAB), idx));
        //fk
//        metaObj.getForeignKeys().stream()
//                .forEach(fk -> defineForeignKey(sb.append(COMMA).append(LINE_SEPARATOR).append(TAB), fk));
        //ck

        //unique
    }

    /**
     * 定义字段 `auditTime` DATETIME NOT NULL COMMENT '审计时间'
     * @param sb 字符串构建器用于写入文本
     * @param col 元列字段
     * @param withComment 是否内联注释，MySql需要
     * @return 写入文本后的 sb
     */
    protected StringBuilder defineCol(StringBuilder sb, MetaCol col, boolean withComment){
        var dbDataType = getDbDataType(col.getDataType());
        sb.append(quote(col)).append(SPACE);
        var maxLength = col.getMaxLength();
        dbDataType.describe(sb, maxLength != null && maxLength > 0 ? maxLength.longValue() : null, col.getNumericPrecision(),col.getNumericScale());
        sb.append(SPACE).append(col.isNullable() ? NULL : NOT_NULL);
        if(BaseUtil.hasText(col.getDefaultVal())) sb.append(DEFAULT).append(col.getDefaultVal());
        return sb;
    }

    /**
     * 定义主键如 PRIMARY KEY ("partnerID")
     * @param sb 字符串构建器用于写入文本
     * @param metaObj 元对象
     * @return 写入后的 sb
     */
    protected StringBuilder definePrimaryKey(StringBuilder sb, MetaObject metaObj){
        var keyList = metaObj.getKeyCols().stream()
                .map(k -> quote(k))
                .collect(Collectors.joining(COMMA));
        sb.append(PRIMARY_KEY).append(PARENTHESES_START)
                .append(keyList).append(PARENTHESES_END);
        return sb;
    }

    /**
     * 添加外键 ALTER TABLE table ADD CONSTRAINT `FK_bomitem_bom` FOREIGN KEY (`bomID`) REFERENCES `bom` (`bomID`) ON UPDATE CASCADE ON DELETE CASCADE
     * @param sb 字符串构建器用来写入文本
     * @param fk 外键
     * @return 写入内容后的sb
     */
    @Override
    public StringBuilder addForeignKey(StringBuilder sb, MetaForeignKey fk){
        var result = sb.append(ALTER_TABLE).append(quoteObject(fk.getDbSchema(),fk.getObjName()))
                .append(ADD).append(CONSTRAINT).append(quote(fk.getConstraintName()))
                .append(FOREIGN_KEY).append(PARENTHESES_START)
                .append(fk.getColRefs().keySet().stream().map(c -> quote(c)).collect(Collectors.joining(COMMA)))
                .append(PARENTHESES_END).append(REFERENCES)
                .append(quoteObject(fk.getRefDbSchema(),fk.getRefObjName())).append(PARENTHESES_START)
                .append(fk.getColRefs().values().stream().map(c -> quote(c)).collect(Collectors.joining(COMMA)))
                .append(PARENTHESES_END);
        if(fk.getOnUpdateAction() != MetaForeignKeyAction.NO_ACTION){
            result.append(" ON UPDATE ").append(fk.getOnUpdateAction().getText());
        }
        if(fk.getOnDeleteAction() != MetaForeignKeyAction.NO_ACTION){
            result.append(" ON DELETE ").append(fk.getOnDeleteAction().getText());
        }
        return result;
    }

    @Override
    public MetaView parseView(String sql) {
        var s = unquote(sql);
        var unionPos = s.indexOf(UNION.trim());
        if(unionPos != -1) s = s.substring(0,unionPos);

        var us = s.toUpperCase();
        var viewPos = us.indexOf(VIEW);
        var asPos = us.indexOf(AS);

        var selectPos = us.indexOf(SELECT);
        var fromPos = us.indexOf(FROM.trim());
        var wherePos = us.lastIndexOf(WHERE.trim());
        var orderByPos = us.lastIndexOf(ORDER_BY.trim());
        var groupByPos = us.lastIndexOf(GROUP_BY.trim());

        var viewName = s.substring(viewPos+VIEW.length(),asPos);
        var selectClause = s.substring(selectPos, fromPos);
        boolean hasWhereClause = wherePos > 0;
        boolean hasOrderByClause = orderByPos > 0;
        boolean hasGroupByClause = groupByPos > 0;
        groupByPos = groupByPos > 0 ? groupByPos : sql.length();
        orderByPos = orderByPos > 0 ? orderByPos : groupByPos;
        var fromClause = s.substring(fromPos, wherePos > 0 ? wherePos : orderByPos);
        var whereClause = hasWhereClause
                ? s.substring(wherePos, orderByPos > 0 ? orderByPos : groupByPos)
                : EMPTY;
        var orderByClause = hasOrderByClause
                ? s.substring(orderByPos, groupByPos)
                : EMPTY;
        var groupByClause = hasGroupByClause
                ? s.substring(groupByPos)
                : EMPTY;
        var fromMatcher = SqlPatterns.FROM_PATTERN.matcher(fromClause);
        if(fromMatcher.matches()){
            var source = fromMatcher.group("source");
            var alias = fromMatcher.group("alias");
            var srcMetaObj = metadataProvider.getMetaObject(source);
            var builder = new MetaView.Builder(srcMetaObj).withName(viewName,viewName);
            var joinOnMatcher = SqlPatterns.JOIN_ON_PATTERN.matcher(fromClause);
            while (joinOnMatcher.find()){
                var joinType = joinOnMatcher.group("joinType");
                var rel = joinOnMatcher.group("rel");
                var relAlias = joinOnMatcher.group("relAlias");
                var joinExp = joinOnMatcher.group("joinExp");
                var relativeObj = metadataProvider.getMetaObject(rel);
                builder.hasOne(joinType,relativeObj,relAlias,joinExp.replace(alias+".","@"));

            }
            //cols
            var selectFields = selectClause.substring(SELECT.length()).trim().split(",");

            return builder
                    .select(selectFields)
                    .build();
        }
        throw new IllegalArgumentException("Invalid SQL syntax: " + sql);
    }

    @Override
    public StringBuilder createView(MetaView metaView, boolean ifNotExists, boolean dropIfExists, boolean withComment) {
        var sb = new StringBuilder();
        if(dropIfExists){
            var dropSql = drop(metaView, true);
            sb.append(dropSql).append(LINE_SEPARATOR);
        }

        var query = SqlQuery.create(metadataProvider)
                .from(metaView.get())
                .withAll(metaView.getRelatives().keySet())
                .where(parseWhereClause(metaView.getWhereCondition()))
                .orderBy(Sort.parse(metaView.getOrderBy()))
                .selectAll()
                .compile(this);
        sb.append(CREATE_VIEW).append(quoteObject(metaView)).append(AS)
                .append(LINE_SEPARATOR)
                .append(query.sql())
                .append(SEMICOLON)
                ;
        return sb;
    }

    /**
     * 解析WHERE条件语句为表达式树
     * @param whereCondition WHERE后面的条件表达式
     * @return SQL条件表达式
     */
    protected SqlCriteriaExp parseWhereClause(String whereCondition) {
        if(!BaseUtil.hasText(whereCondition)) return null;

        var andConditions = whereCondition.split(AND);
        SqlCriteriaExp criteria = null;
        for(var condition : andConditions){
            if(condition.indexOf(OR) != -1) {
                var orConditions = condition.split(OR);
                SqlCriteriaExp orCriteria = null;
                for(var orCondition : orConditions){
                    if(orCriteria == null) orCriteria = parseWhereClause(orCondition);
                    else orCriteria.or(parseWhereClause(orCondition));
                }
                if(criteria == null) criteria = parseComparisonExp(condition);
                else criteria.and(parseComparisonExp(condition));
            }
            else{
                if(criteria == null) criteria = parseComparisonExp(condition);
                else criteria.and(parseComparisonExp(condition));
            }
        }
        return criteria;
    }

    /**
     * 解析单个条件表达式{@code t.orderID LIKE 'OD2026%'}
     * @param cmpExp 单个条件表达式
     * @return 表达式：{ lhs, op, rhs }
     */
    private SqlCriteriaExp parseComparisonExp(String cmpExp) {
        //TODO: 需要ANTLR4 解析
        //去除外层()
        cmpExp = cmpExp.trim();
        while (cmpExp.startsWith("(") && cmpExp.endsWith(")")) {
            cmpExp = cmpExp.substring(1, cmpExp.length() - 1).trim();
        }
        //使用正则匹配出 lhs op rhs
        var matcher = SqlPatterns.COMPARISON_EXP_PATTERN.matcher(cmpExp);
        if(matcher.matches()){
            var lhs = matcher.group("lhs");//左边只允许简单字段引用，例如t.orderID
            var rhs = matcher.group("rhs");//右边允许复杂算术表达式，例如t.amount * 0.2
            var op = matcher.group("cmp");
            return new SqlCriteriaExp(lhs,SqlOp.parse(op),rhs);
        }
        throw new IllegalArgumentException("Invalid SQL comparison expression: " + cmpExp);
    }

    private SqlArithmeticExp parseArithmeticExp(String exp) {
        //TODO: 需要ANTLR4 解析
        //去除外层()
        exp = exp.trim();
        while (exp.startsWith("(") && exp.endsWith(")")) {
            exp = exp.substring(1, exp.length() - 1).trim();
        }
        throw new IllegalArgumentException("Invalid SQL comparison expression: " + exp);
    }

    /**
     * 给对象添加注释，MySql不适用
     * @param sb
     * @param metaObj
     * @return
     */
    public StringBuilder commentObject(StringBuilder sb, MetaObject metaObj) {
        var quotedObjName = quoteObject(metaObj);
        if(BaseUtil.hasText(metaObj.getDescription())){
            sb.append(COMMENT_ON_TABLE).append(quotedObjName)
                    .append(SPACE)
                    .append(IS).append(quoteValue(metaObj.getDescription()))
                    .append(SEMICOLON).append(LINE_SEPARATOR);
        }
        metaObj.getCols().stream()
                .filter(c -> BaseUtil.hasText(c.getDescription()))
                .forEach(c -> commentCol(sb, c, quotedObjName));
        return sb;
    }

    /**
     * 给元列字段加注释
     * @param sb
     * @param c 元列字段
     * @param quotedObjName 传入已加引号的对象名，优化性能
     * @return sb
     */
    public StringBuilder commentCol(StringBuilder sb, MetaCol c, String quotedObjName) {
        var quotedColName = quotedObjName != null
                ? quoteObject(quotedObjName,c.getColName())
                : quoteCol(c);
        return sb.append(COMMENT_ON_COLUMN).append(quotedColName)
                .append(SPACE).append(IS).append(quoteValue(c.getDescription()))
                .append(SEMICOLON)
                .append(LINE_SEPARATOR);
    }
    //endregion of DDL
}
