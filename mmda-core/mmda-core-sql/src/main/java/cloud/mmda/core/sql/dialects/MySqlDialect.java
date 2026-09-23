package cloud.mmda.core.sql.dialects;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.metadata.*;
import cloud.mmda.core.sql.SqlArg;
import cloud.mmda.core.sql.SqlCmd;
import cloud.mmda.core.sql.SqlMetaContext;
import cloud.mmda.core.sql.expressions.SqlArithmeticExp;
import cloud.mmda.core.sql.expressions.SqlCriteriaExp;
import cloud.mmda.core.sql.types.DbDataType;
import cloud.mmda.core.sql.types.MySqlDataTypes;
import cloud.mmda.core.utils.MapUtil;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cloud.mmda.core.sql.SqlConsts.*;

/**
 * MySql方言
 *
 * @see <a href="https://dev.mysql.com/doc/refman/8.4/en/sql-statements.html">官方SQL语法文档</a>
 */
public class MySqlDialect extends AnsiSqlDialect {
    private final int version = 8;
    public MySqlDialect(MetadataProvider metadataProvider) {
        super(metadataProvider);
    }

    @Override
    public String name() {
        return "MySql";
    }

    @Override
    public DbDataType getDbDataType(DataType genericDataType) {
        return MySqlDataTypes.getType(genericDataType);
    }

    @Override
    public final String quote(String identifier) {
        return "`"+identifier+"`";
    }

    @Override
    public String unquote(String identifier) {
        return identifier.replace("`","");
    }

    @Override
    public boolean supportOrderByField() {
        return true;
    }

//    @Override
//    public String rowNum(String orderBy, String partition) {
//        //8.0才支持ROW_NUMBER()=> return super.toRowNumSql(partition, orderBy);
//        //需要select语句中加上：,(SELECT @ROW_NUMBER:=0) AS t
//        return version < 8
//                ? "(@ROW_NUMBER:=@ROW_NUMBER + 1) AS rowNum"
//                : super.rowNum(orderBy, partition);
//    }
//    @Override
//    public String rowNumFrom(){
//        return "(SELECT @ROW_NUMBER:=0) AS _t";
//    }

    //region UPDATE

    /**
     * {@inheritDoc} UPDATE JOIN
     * <pre>
     *     {@code
     *     UPDATE [LOW_PRIORITY] [IGNORE] table_reference
     *     SET assignment_list
     *     [WHERE where_condition]
     *     [ORDER BY ...]
     *     [LIMIT row_count]
     *     }
     * </pre>
     *
     * MySql支持更新的时候排序和限制（ORDER BY and LIMIT），是它特殊的功能，但是多表关联时不能用。
     * <pre>
     *     {@code UPDATE t SET id = id + 1 ORDER BY id DESC;//批量更新主键id}
     * </pre>
     * MySql支持 PARTITION clause 指定表分区，可用于限制租户数据更新。
     *
     * @see <a href="https://dev.mysql.com/doc/refman/8.4/en/update.html">MySql官方文档</a>
     */
    @Override
    public SqlCmd update(final SqlMetaContext context, LinkedHashMap<MetaCol, Object> assignments, SqlCriteriaExp condition, boolean namedParameter) {
        Objects.requireNonNull(context, "context must not be null");
        if(MapUtil.isNullOrEmpty(assignments)) throw new IllegalArgumentException("setters must not be null or empty");

        var argList = new ArrayList<SqlArg>();

        // UPDATE table SET
        var sb = new StringBuilder(UPDATE)
                .append(fromClause(context))
                .append(SET);

        // col1 = ?, col2 = ?
        var setterJoiner = new StringJoiner(COMMA);
        Function<MetaCol,String> fn = namedParameter
                ? col -> quote(col.getColName()) + EQUALS + COLON + col.getColName()
                : col -> quote(col.getColName()) + EQUALS + PLACEHOLDER;
        assignments.forEach((col, value)->{
            if(value instanceof SqlArithmeticExp exp){
                var pe = exp.compile(this, context, false);
                argList.addAll(pe.argList());
                setterJoiner.add(quote(col.getColName()) + EQUALS + pe.expression());
            }
            else if(value instanceof MetaCol valueCol){
                setterJoiner.add(quote(col.getColName()) + EQUALS + quote(valueCol.getColName()));
            }
            else{
                argList.add(SqlArg.of(col, value));
                setterJoiner.add(fn.apply(col));
            }

        });
        sb.append(setterJoiner);

        // WHERE condition
        var whereClause = whereClause(context.get(), condition, argList, false);
        if(!EMPTY.equals(whereClause)) sb.append(WHERE).append(whereClause);

        return new SqlCmd(sb.toString(), argList, UPDATE+whereClause);
    }

    //endregion of UPDATE

    //region DDL
    @Override
    public StringBuilder createTable(MetaObject metaObj, boolean ifNotExists, boolean dropIfExists, boolean withComment) {
        var sb = super.createTable(metaObj, ifNotExists, dropIfExists, withComment);

        //注释
        if(withComment) sb.append(COMMENT).append(quoteValue(metaObj.getDescription())).append(LINE_SEPARATOR);
        return sb.append("ENGINE=InnoDB").append(SEMICOLON).append(LINE_SEPARATOR);
    }

    @Override
    protected void addTableConstraints(StringBuilder sb, MetaObject metaObj) {
        definePrimaryKey(sb, metaObj);
        //indexes
        var NEXT_STATEMENT = COMMA + LINE_SEPARATOR + TAB;
        metaObj.getIndexes().stream()
                .filter(idx -> !"PRIMARY".equalsIgnoreCase(idx.getIndexName()))
                .forEach(idx -> defineIndex(sb.append(NEXT_STATEMENT), idx));

    }

    @Override
    protected StringBuilder defineCol(StringBuilder sb, MetaCol col, boolean withComment) {
        return super.defineCol(sb, col, withComment) //添加注释
                .append(withComment ? SPACE + COMMENT + quoteValue(col.getDescription()) : EMPTY);
    }

    @Override
    protected StringBuilder definePrimaryKey(StringBuilder sb, MetaObject metaObj) {
        return super.definePrimaryKey(sb, metaObj);
    }

    /**
     * 定义索引 INDEX `IDX_bomitem_materialID` (`materialID`) USING BTREE
     * @param sb
     * @param index
     * @return 写入索引定义的 sb
     */
    private StringBuilder defineIndex(StringBuilder sb, MetaIndex index) {
        return sb.append("KEY ").append(quote(index.getIndexName())).append(PARENTHESES_START)
                .append(index.getCols().stream().map(c -> quote(c.colName())).collect(Collectors.joining(COMMA)))
                .append(PARENTHESES_END);
    }

    /**
     * 删除索引 ALTER TABLE `bomitem` DROP INDEX `IDX_bomitem_partNo`。不支持 IF EXISTS 语法
     * @param sb 字符串构建器用于写入文本
     * @param index 索引
     * @param dropIfExists 如果存在才删除
     * @return 写入内容后的 sb
     */
    @Override
    public StringBuilder dropIndex(StringBuilder sb, MetaIndex index, boolean dropIfExists) {
        return sb.append(ALTER_TABLE).append(quoteObject(index.getDbSchema(),index.getObjName()))
                .append(DROP).append(INDEX).append(quote(index.getIndexName()));
    }

    /**
     * 创建索引 ALTER TABLE `bomitem` ADD INDEX `IDX_bomitem_brand` (`brand`)。不支持 IF EXISTS 语法
     * @param sb 字符串构建器用于写入文本
     * @param index 索引
     * @param ifNotExists 仅当索引不存在才创建
     * @param dropIfExists 如果存在先删除索引
     * @return 写入内容后的 sb
     */
    @Override
    public StringBuilder createIndex(StringBuilder sb, MetaIndex index, boolean ifNotExists, boolean dropIfExists) {
        if(dropIfExists) sb = dropIndex(sb, index, true).append(SEMICOLON).append(LINE_SEPARATOR);
        return sb.append(ALTER_TABLE).append(quoteObject(index.getDbSchema(),index.getObjName()))
                .append(ADD).append(INDEX).append(quote(index.getIndexName()))
                .append(PARENTHESES_START)
                .append(index.getCols().stream().map(c -> quote(c.colName())).collect(Collectors.joining(COMMA)))
                .append(PARENTHESES_END);
    }

    @Override
    public MetaView parseView(String sql) {
        return super.parseView(sql);
    }

    //endregion of DDL
}
