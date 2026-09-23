package cloud.mmda.core.sql;

import cloud.mmda.core.data.pagination.Paginator;
import cloud.mmda.core.data.pagination.Sort;
import cloud.mmda.core.metadata.MetaContext;
import cloud.mmda.core.metadata.MetaObject;
import cloud.mmda.core.metadata.MetaRelation;
import cloud.mmda.core.metadata.SimpleMetaContext;
import cloud.mmda.core.sql.dialects.SqlDialect;
import cloud.mmda.core.utils.BaseUtil;
import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static cloud.mmda.core.sql.SqlConsts.*;

/**
 * 可执行的 SQL查询是从{@link SqlQuery}编译而得，包含 SELECT 语句的片段、整体 SQL语句和参数列表。
 * <pre>
 *     {@code
 *     sql:      SELECT userID, userName, mobile, email FROM User WHERE status = ?;
 *     countSql: SELECT COUNT(*) FROM User WHERE status = ?;
 *     }
 * </pre>
 */
public class SqlQueryable implements SqlExecutable{
    /**
     * 方言名称，例如MySql, Oracle
     */
    @Getter
    private final String dialectName;

    private final String selectClause;
    private final String fromClause;
    private final String whereClause;
    private final String orderByClause;
    private final String rowNumClause;

//        /**
//         * 返回 SELECT ... FROM ...
//         */
//        private String selectFrom;
//        /**
//         * 返回 SELECT ... FROM ... WHERE ...，
//         *
//         */
//        private String selectFromWhere;
    /**
     * 返回完整SQL语句
     */
    private String sql;
    /**
     * 返回完整SQL COUNT语句
     */
    private String countSql;

    /**
     * 参数列表
     */
    private final List<SqlArg> argList;

    public SqlQueryable(SqlDialect dialect, String selectClause, String fromClause, String whereClause, String orderByClause, List<SqlArg> argList) {
        this.dialectName = dialect.name();
        this.selectClause = selectClause;
        this.fromClause = fromClause;
        this.whereClause = whereClause;
        this.orderByClause = orderByClause;
        this.rowNumClause = dialect.rowNumClause(orderByClause);
        this.argList = argList;
        buildSql();
    }

    public boolean hasWhereClause(){
        return BaseUtil.hasText(whereClause);
    }

    public String sql(){
        return sql;
    }
    public String countSql(){
        return countSql;
    }
    public List<SqlArg> argList(){
        return argList;
    }

//        public String selectFrom(){
//            return selectFrom;
//        }
//        public String selectFromWhere(){
//            return selectFromWhere;
//        }

    private String buildCountSql(){
        return hasWhereClause()
                ? new StringBuilder(SELECT_COUNT_ALL_FROM).append(fromClause).append(WHERE).append(whereClause).toString()
                : new StringBuilder(SELECT_COUNT_ALL_FROM).append(fromClause).toString();
    }
    private void buildSql() {
        StringBuilder sb = new StringBuilder(SELECT).append(selectClause)
                .append(rowNumClause)
                .append(FROM).append(fromClause);
//            selectFrom = sb.toString();

        if(BaseUtil.hasText(whereClause)){
            sb.append(WHERE).append(whereClause);
        }
//            selectFromWhere = sb.toString();

        sb.append(ORDER_BY).append(orderByClause);
        sql = sb.toString();
        countSql = selectClause.startsWith(COUNT) ? sql : buildCountSql();
    }

    /**
     * 替换排序规则后创建新的查询脚本
     * @param dialect 方言
     * @param context 元上下文
     * @param sort 新的排序规则
     * @return 新的查询脚本
     */
    public SqlQueryable orderBy(final SqlDialect dialect, final MetaContext context, final Sort sort) {
        var orderByClause = dialect.orderByClause(sort, context);
        if(orderByClause.equals(this.orderByClause)) return this;//未改变排序规则
        return new SqlQueryable(dialect, selectClause, fromClause, whereClause, orderByClause, argList);
    }


    /**
     * 返回分页查询SQL语句
     * @param dialect 方言
     * @param pageSize 页大小
     * @param pageNo 页序号，从1开始
     * @return 查询一页的SQL语句
     */
    public String pageOf(final SqlDialect dialect, int pageSize, int pageNo){
        return sql + dialect.limitClause(pageSize, pageNo);
    }

    public SqlQueryable pageOf(final SqlDialect dialect, final MetaContext context, final Paginator paginator) {
        var orderByClause = dialect.orderByClause(paginator.getSort(), context);
        if(orderByClause.equals(this.orderByClause)) return this;//未改变排序规则
        return new SqlQueryable(dialect, selectClause, fromClause, whereClause, orderByClause, argList);
    }

    public String first(final SqlDialect dialect){
        return sql + dialect.limitClause(1, 1);
    }
}