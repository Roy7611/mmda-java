package cloud.mmda.core.sql;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.metadata.MetaCol;
import cloud.mmda.core.sql.expressions.SqlExp;

/**
 * SQL 参数
 * @param name 参数名称
 * @param dataType 参数数据类型
 * @param value 参数值
 */
public record SqlArg(String name, DataType dataType, Object value) {
    public int jdbcType(){
        return dataType.getJdbcType();
    }
    public static SqlArg of(MetaCol col) {
        return new SqlArg(col.getColName(), col.getDataType(), SqlExp.PARAM_VALUE);
    }
    public static SqlArg of(MetaCol col, Object value) {
        return new SqlArg(col.getColName(), col.getDataType(), value);
    }

    //TODO:应该使用DbDataType支持自定义DataTypeHandler
}
