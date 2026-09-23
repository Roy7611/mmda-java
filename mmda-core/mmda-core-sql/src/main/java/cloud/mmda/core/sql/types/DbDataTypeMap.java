package cloud.mmda.core.sql.types;

import cloud.mmda.core.enums.DataType;

import java.util.HashMap;

/**
 * 不同厂家的数据类型映射，通过标准的{@link DataType}查找{@link DbDataType}
 */
public class DbDataTypeMap extends HashMap<DataType, DbDataType> {
    public DbDataType add(DbDataType dbDataType){
        return put(dbDataType.dataType(), dbDataType);
    }
}
