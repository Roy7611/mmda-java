package cloud.mmda.core.sql.types;

import cloud.mmda.core.enums.DataType;

/**
 * 金仓数据类型
 *
 * @see <a href="https://help.kingbase.com.cn/v8/development/sql-plsql/sql-quick/datatype.html">官方文档</a>
 */
public class KingbaseDataTypes {
    public static DbDataType getType(DataType dataType){
//        if(dataType.getGenericType().isInteger() && dataType.autoIncrement()){
//            // replace with serial type
//        }
//        return dataTypeMap.get(dataType);
        throw new UnsupportedOperationException();
    }
}
