package cloud.mmda.core.sql;

import cloud.mmda.core.enums.EnumBitSet;
import cloud.mmda.core.enums.EnumValue;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 可执行的SQL语句和参数列表，数据访问层会缓存可执行对象优化查询性能。具体实现包括：
 * @see SqlCmd SQL命令
 * @see SqlQueryable SQL查询脚本
 */
public interface SqlExecutable {
    String sql();
    List<SqlArg> argList();

    /**
     * 将参数转化为可传递给JDBC驱动的值，主要实现枚举值到整型数值的转化
     * @param arg 参数是Java对象
     * @return JDBC驱动能接受的参数值
     */
    default Object toJdbcValue(Object arg){
        if (arg instanceof EnumValue enumValue) {
            return enumValue.getValue();
        } else if (arg instanceof EnumBitSet enumBitSet) {
            return enumBitSet.getValue();
        } else {
            return arg;
        }
    }

    default Object[] getArgValues(int tenantId, Object...args){
        int j = 0;
        var argList = argList();
        var argValues = new ArrayList<Object>();
        for (int i = 0; i < argList.size(); i++) {
            var argProto = argList.get(i);
            if(SqlConsts.MIN_ID.equals(argProto.name())){
                Function<Integer,Long> getMinId = (Function<Integer,Long>) argProto.value();
                argValues.add(getMinId.apply(tenantId));
            }
            else if(SqlConsts.MAX_ID.equals(argProto.name())){
                Function<Integer,Long> getMaxId = (Function<Integer,Long>) argProto.value();
                argValues.add(getMaxId.apply(tenantId));
            }
            else if(args[j] == null){
                argValues.add(null);
                j++;
            }
            else{
                argValues.add(toJdbcValue(args[j]));
                j++;
            }
        }
        return argValues.toArray();
    }

    default void setArgValues(PreparedStatement ps, int tenantId, Object...args) throws SQLException {
        int j = 0;
        var argList = argList();
        for (int i = 0; i < argList.size(); i++) {
            var argProto = argList.get(i);
            int paramIndex = i + 1;
            if(SqlConsts.MIN_ID.equals(argProto.name())){
                Function<Integer,Long> getMinId = (Function<Integer,Long>) argProto.value();
                ps.setLong(paramIndex, getMinId.apply(tenantId));
            }
            else if(SqlConsts.MAX_ID.equals(argProto.name())){
                Function<Integer,Long> getMaxId = (Function<Integer,Long>) argProto.value();
                ps.setLong(paramIndex, getMaxId.apply(tenantId));
            }
            else if(args[j] == null){
                ps.setNull(paramIndex, argProto.jdbcType());
                j++;
            }
            else{
                //TODO:改进为TypeHandler, toJdbcValue仅仅处理枚举，未能支持其他特殊类型
                //argProto.dataType().getDefaultHandler().setParameter(ps,paramIndex,args[j], argProto.jdbcType());
                ps.setObject(paramIndex, toJdbcValue(args[j]), argProto.jdbcType());
                j++;
            }
        }
    }

    default void setArgValues(PreparedStatement ps, int tenantId, Map<String,Object> namedArgs) throws SQLException {
        var argList = argList();
        for (int i = 0; i < argList.size(); i++) {
            var argProto = argList.get(i);
            int paramIndex = i + 1;
            if(SqlConsts.MIN_ID.equals(argProto.name())){
                Function<Integer,Long> getMinId = (Function<Integer,Long>) argProto.value();
                ps.setLong(paramIndex, getMinId.apply(tenantId));
            }
            else if(SqlConsts.MAX_ID.equals(argProto.name())){
                Function<Integer,Long> getMaxId = (Function<Integer,Long>) argProto.value();
                ps.setLong(paramIndex, getMaxId.apply(tenantId));
            }
            else{
                var value = namedArgs.get(argProto.name());
                if(value == null){
                    ps.setNull(paramIndex, argProto.jdbcType());
                }
                else{
                    ps.setObject(paramIndex, toJdbcValue(value), argProto.jdbcType());
                }
            }
        }
    }

    default PreparedStatementSetter statementSetter(int tenantId, Object... args) {
        return ps -> setArgValues(ps, tenantId, args);
    }

    default PreparedStatementSetter statementSetter(int tenantId, Map<String, Object> namedArgs) {
        return ps -> setArgValues(ps, tenantId, namedArgs);
    }

    default PreparedStatementCreator statementCreator(int tenantId, Object... args) {
        return connection -> {
            var ps = connection.prepareStatement(sql());
            setArgValues(ps, tenantId, args);
            return ps;
        };
    }

    default PreparedStatementCreator statementCreator(int tenantId, Map<String, Object> namedArgs) {
        return connection -> {
            var ps = connection.prepareStatement(sql());
            setArgValues(ps, tenantId, namedArgs);
            return ps;
        };
    }
}
