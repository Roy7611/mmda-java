package cloud.mmda.core.sql;

import lombok.Getter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.util.List;
import java.util.Map;

/**
 * Sql命令，可通过{@link #sql ()}获取SQL语句，调用{@link #statementSetter(int, Object...)}传入参数值
 * 创建{@code JdbcTemplate}可执行的{@link PreparedStatementSetter}或者{@link PreparedStatementCreator}
 */
public record SqlCmd(String sql, List<SqlArg> argList, String cmdName) implements SqlExecutable {


    /**
     * 创建一个插入指令
     * @param sql SQL语句
     * @param argList 参数列表
     * @return SQL INSERT 命令
     */
    public static SqlCmd insert(String sql, List<SqlArg> argList) {
        return new SqlCmd(sql, argList, SqlConsts.INSERT);
    }
}
