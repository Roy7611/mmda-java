package cloud.mmda.core.data.exceptions;

/**
 * 未指定排序或命名分区
 *
 * 由于生成的Sql语句中存在$_SORT和$_PARTITION插槽，需先替换为实际的内容后才能交由JdbcTemplate执行。
 * $_SORT通常在行号表达式中存在，
 * $_PARTITION在MySql中启用表分区后使用，启用表分区的MetaObject.isPartitioned()=true
 */
public class SqlQueryUnresolvedException extends RuntimeException {
    private static final String UNRESOLVED_SORT = "排序（ORDER BY）语句";
    private static final String UNRESOLVED_PARTITION = "命名分区（PARTITION）";
    private static final String UNRESOLVED_BOTH = UNRESOLVED_SORT +"或"+UNRESOLVED_PARTITION;

    private static final String msg = "必须先指定%1$s后才能直接使用Sql语句，请尝试使用带tenantID和sort参数的版本";

    public SqlQueryUnresolvedException(){
        super(String.format(msg,UNRESOLVED_BOTH));
    }

    public SqlQueryUnresolvedException(boolean unresolvedSort, boolean unresolvedPartition){

        super(String.format(msg,
                unresolvedSort
                        ? (unresolvedPartition ? UNRESOLVED_BOTH : UNRESOLVED_SORT)
                        : (unresolvedPartition ? UNRESOLVED_PARTITION : UNRESOLVED_BOTH))
        );
    }
}
