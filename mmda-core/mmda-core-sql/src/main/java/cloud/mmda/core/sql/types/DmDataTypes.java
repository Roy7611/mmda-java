package cloud.mmda.core.sql.types;

import cloud.mmda.core.enums.DataType;

/**
 * 达梦数据类型
 *
 * @see <a href="https://eco.dameng.com/document/dm/zh-cn/sql-dev/dmpl-sql-datatype.html">官方文档</a>
 */
public class DmDataTypes {
    public static DbDataType getType(DataType dataType){
//        if(dataType.getGenericType().isInteger() && dataType.autoIncrement()){
//            // replace with serial type
//        }
//        return dataTypeMap.get(dataType);
        throw new UnsupportedOperationException();
    }
}
