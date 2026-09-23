package cloud.mmda.core.metadata;

import cloud.mmda.core.enums.DataType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;

/**
 * 元数据类型
 *
 * 统一定义不同数据库中的数据类型映射关系，用于模型驱动开发，从数据库定义生成不同编程语言的模型类。
 * 不同数据库迁移时便于生成数据库定义脚本。
 **/
public class MetaDataType implements Serializable {

//    //region 枚举
//    public static final int BIT = 0;//布尔
//
//    public static final int TINYINT_UNSIGNED = 1;//整型(微小)
//    public static final int SMALLINT_UNSIGNED = 2;//整型(小)
//    public static final int MEDIUMINT_UNSIGNED = 3;//整型(中)
//    public static final int INT_UNSIGNED = 4;//整型
//    public static final int BIGINT_UNSIGNED = 5;//整型(大)
//
//    public static final int TINYINT = 9;//整型(微小)
//    public static final int SMALLINT = 10;//整型(小)
//    public static final int MEDIUMINT = 11;//整型(中)
//    public static final int INT = 12;//整型
//    public static final int BIGINT = 13;//整型(大)
//
//    public static final int DECIMAL = 16;//数值
//    public static final int NUMERIC = 17;//数值
//    public static final int MONEY = 18;//货币
//    public static final int SMALLMONEY = 19;//货币(小)
//
//    public static final int FLOAT = 20;//数值(单精度)
//    public static final int REAL = 21;//实数 4字节
//    public static final int DOUBLE = 24;//数值(双精度)
//
//    public static final int CHAR = 32;//字符
//    public static final int NCHAR = 33;//字符(UNICODE)
//    public static final int VARCHAR = 34;//字符串
//    public static final int NVARCHAR = 35;//字符串(UNICODE)
//    public static final int UUID = 36;//唯一标识
//    public static final int HIERARCHYID = 37;//层次标识
//    public static final int CLOB = 40;//大文本对象
//    public static final int NCLOB = 41;//大文本对象
//    public static final int TEXT = 40;//文本
//    public static final int NTEXT = 41;//文本(UNICODE)
//    public static final int JSON = 42;//JSON
//    public static final int XML = 43;//XML
//
//
//    public static final int DATE = 64;//日期
//    public static final int TIME = 65;//时间
//    public static final int TIME_TZ = 66;//时间
//    public static final int DATETIME = 67;//日期时间
//    public static final int TIMESTAMP = 68;//时间戳
//    public static final int TIMESTAMP_TZ = 69;//日期时间偏移量
//    public static final int INTERVAL = 70;//周期
//
//    public static final int VECTOR = 128;//二进制
//    public static final int BIT_VECTOR = 129;//位向量
//    public static final int BINARY = 136;//二进制
//    public static final int VARBINARY = 137;//二进制(可变长)
//    public static final int BLOB = 138;//二进制大对象
//    public static final int LONGVARBINARY = 138;//长二进制(可变长)
//    public static final int BFILE = 139;//文件指针（存储在库外）
//
//    public static final int GEOGRAPHY = 256;//地理
//    public static final int GEOMETRY = 320;//几何数据
//    public static final int ANY = 512;//可变型 SQL_VARIANT
//    //endregion

    //region 属性
    /**
     * 数据类型，统一映射为此整型值
     */
    @NotNull
    @Min(0)
    @Getter @Setter
    private int dataType;

    /**
     * 通用数据类型名称
     */
    @Size(max = 30)
    @Getter @Setter
    private String dataTypeName;

    /**
     * C#语言类型
     */
    @Size(max = 30)
    @Getter @Setter
    private String csharpType;

    /**
     * Java语言类型
     */
    @Size(max = 30)
    @Getter @Setter
    private String javaType;

    /**
     * Dart类型，用于生成Flutter模型
     */
    @Size(max=30)
    @Getter @Setter
    private String dartType;

    /**
     * Swift类型，用于生成iOS模型
     */
    @Size(max=30)
    @Getter @Setter
    private String swiftType;

    /**
     * Python类型，用于生成测试用例模型
     */
    @Size(max=30)
    @Getter @Setter
    private String pythonType;

    /**
     * JDBC类型，用于JdbcTemplate
     */
    @Getter @Setter
    private Integer jdbcType;

    /**
     * MS SQL 对应Ado.Net Types，如为null表示无原生对应类型
     */
    @Min(0)
    @Getter @Setter
    private Integer mssqlType;
    /**
     * MS SQL 数据类型名称，如char,money
     */
    @Size(max = 30)
    @Getter @Setter
    private String mssqlName;

    /**
     * MySql 类型，如为null表示无原生对应类型
     */
    @Min(0)
    @Getter @Setter
    private Integer mysqlType;

    /**
     * MySql 类型名称，如char,mediumint
     */
    @Size(max = 30)
    @Getter @Setter
    private String mysqlName;

    /**
     * Oracle 类型，如为null表示无原生对应类型
     */
    @Min(0)
    @Getter @Setter
    private Integer oracleType;

    /**
     * Oracle 类型名称，如char,number
     */
    @Size(max = 30)
    @Getter @Setter
    private String oracleName;

    /**
     * Sqlite 类型，如为null表示无原生对应类型
     */
    @Min(0)
    @Getter @Setter
    private Integer sqliteType;

    /**
     * Sqlite 类型名称，如char,text
     */
    @Size(max = 30)
    @Getter @Setter
    private String sqliteName;


    /**
     * Postgre SQL 类型，如为null表示无原生对应类型
     */
    @Min(0)
    @Getter @Setter
    private Integer pgsqlType;

    /**
     * Postgre SQL 类型名称，如char,text
     */
    @Size(max = 30)
    @Getter @Setter
    private String pgsqlName;


    /**
     * Kingbase 类型，如为null表示无原生对应类型
     */
    @Min(0)
    @Getter @Setter
    private Integer kingbaseType;

    /**
     * Kingbase 类型名称，如char,text
     */
    @Size(max = 30)
    @Getter @Setter
    private String kingbaseName;


    /**
     * 达梦 类型，如为null表示无原生对应类型
     */
    @Min(0)
    @Getter @Setter
    private Integer dmType;

    /**
     * 达梦 类型名称，如char,text
     */
    @Size(max = 30)
    @Getter @Setter
    private String dmName;
    //endregion

//    //region 类型判断
//
//    /**
//     * 是否数值型，包括整型、定点数、浮点数、货币
//     * @param dt
//     * @return
//     */
//    public static boolean isNumber(int dt){
//        return dt>=1 && dt<32;
//    }
//
//    /**
//     * 是否整型
//     * @param dt
//     * @return
//     */
//    public static boolean isInteger(int dt){
//        return dt>=1 && dt<16;
//    }
//
//    /**
//     * 是否带小数型，包括定点数、浮点数、货币
//     * @param dt
//     * @return
//     */
//    public static boolean isDecimal(int dt){
//        return dt>=16 && dt<32;
//    }
//
//    /**
//     * 是否包含日期数据
//     * @param dt
//     * @return
//     */
//    public static boolean isDate(int dt){
//        return dt>=DATE && dt<=TIMESTAMP_TZ && dt != TIME && dt != TIME_TZ;
//    }
//
//    /**
//     * 是否包含日期与时间
//     * @param dt
//     * @return
//     */
//    public static boolean isDateTime(int dt){
//        return dt>=DATETIME && dt<=TIMESTAMP_TZ;
//    }
//
//    /**
//     * 是否包含日期或者时间
//     * @param dt
//     * @return
//     */
//    public static boolean isDateOrTime(int dt){
//        return dt>=DATE && dt<=INTERVAL;
//    }
//
//    /**
//     * 是否字符串
//     * @param dt
//     * @return
//     */
//    public static boolean isString(int dt){
//        return dt>=32 && dt<64;
//    }
//
//    public static boolean isQuotable(int dt){
//        return isString(dt) || isDateOrTime(dt);
//    }
//    /**
//     * 是否二进制数据
//     * @param dt
//     * @return
//     */
//    public static boolean isBinary(int dt){
//        return dt>=BINARY && dt<=LONGVARBINARY;
//    }
//    //endregion

    private static final boolean isNullOrEmpty(String defValue){
        return defValue==null || defValue.isEmpty() || "NULL".equalsIgnoreCase(defValue);
    }
    public static final String getPureDefValue(String defValue){
        if(defValue == null) return defValue;
        if(defValue.startsWith("N'") || defValue.startsWith("b'")) defValue = defValue.substring(2);
        return defValue.replaceAll("[\\(||\\)||']","");
    }

    public static Object parseDefaultValue(String defValue, DataType dataType){
        if(isNullOrEmpty(defValue)) return null;
        String d = getPureDefValue(defValue);

        try{
            return dataType.getDefaultHandler().convert(d);
        }
        catch(Exception e){
            return d;
        }
    }
    //////////////////////////////////////////////////////////////////////////
    // Hash code and equals
    //////////////////////////////////////////////////////////////////////////
    @Override
    public int hashCode() {
        return dataType;
    }

    @Override
    public boolean equals(Object obj) {
        if(super.equals(obj)) return true;
        if(obj instanceof MetaDataType){
            MetaDataType metaDataType = (MetaDataType)obj;
            return dataType==metaDataType.getDataType();
        }
        return false;
    }
}
