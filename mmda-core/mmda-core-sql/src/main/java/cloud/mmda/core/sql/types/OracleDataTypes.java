package cloud.mmda.core.sql.types;

import cloud.mmda.core.enums.DataType;

import static cloud.mmda.core.sql.types.DbDataType.*;

/**
 * Oracle数据类型
 *
 * @see <a href="https://docs.oracle.com/en/database/oracle/oracle-database/19/sqlqr/Data-Types.html#GUID-219C338B-FE60-422A-B196-2F0A01CAD9A4">官方参考</a>
 * @see <a href="https://docs.oracle.com/en/database/oracle/oracle-database/19/sqlrf/Data-Types.html#GUID-A3C0D836-BADB-44E5-A5D4-265BA5968483">官方编程参考文档</a>
 */
public final class OracleDataTypes {
    private static int MAX_STRING_SIZE = 4000;
    private static int MAX_RAW_BYTES = 2000;//扩展模式可达到32767字节
    /**
     * 可变长字符串，长度n(1 ~4000)字节，扩展模式（MAX_STRING_SIZE = EXTENDED）可达到32767字节
     */
    public static DbDataType VARCHAR2 = make(DataType.VARCHAR, "VARCHAR2", maxLength(MAX_STRING_SIZE), 1);
    public static DbDataType NVARCHAR2 = make(DataType.NVARCHAR, "NVARCHAR2", maxLength(MAX_STRING_SIZE), 1);
    /**
     * 数值(p,s)要求 1 ~ 22 字节，精度p(1 ~ 38)，小数s(-84 ~ 127)
     */
    public static DbDataType NUMBER = make(DataType.DECIMAL, "NUMBER", precision(1, 38).scale(-84, 127, 0).maxBytes(22), 2);
    /**
     * 数值{@link #NUMBER}的子类型，精度p(1 ~ 126)
     *
     * A subtype of the NUMBER data type having precision p. A FLOAT value is represented internally as NUMBER.
     * The precision p can range from 1 to 126 binary digits. A FLOAT value requires from 1 to 22 bytes.
     */
    public static final DbDataType FLOAT  = NUMBER.as(DataType.FLOAT32, "FLOAT", precision(1,126).maxBytes(22));
    public static final DbDataType REAL  = FLOAT.as(DataType.REAL, "FLOAT(63)", true);
    public static final DbDataType DOUBLE  =NUMBER.as(DataType.FLOAT64, "FLOAT(126)", true);

    public static final DbDataType BINARY_FLOAT  = make(DataType.FLOAT32, "BINARY_FLOAT",null, 100);
    public static final DbDataType BINARY_DOUBLE = make(DataType.FLOAT64, "BINARY_DOUBLE", null, 101);

    /**
     * 最长2Gb的长文本
     */
    public static final DbDataType LONG  = make(DataType.CLOB, "LONG", maxBytes(0x7FFF_FFFFL),8);
    /**
     * 日期从January 1, 4712 BC到 December 31, 9999 AD，需7个字节，包含
     * YEAR, MONTH, DAY, HOUR, MINUTE, and SECOND
     */
    public static final DbDataType DATE  = make(DataType.DATE,"DATE", fixedBytes(7).dateBetween("-4712-01-01","9999-12-31"), 12);

    /**
     * 时间戳，秒的小数精度 p(0 ~ 9)，需要 7 or 11 bytes，不带时区
     */
    public static final DbDataType TIMESTAMP  = make(DataType.TIMESTAMP, "TIMESTAMP", precision(0, 9).inBytes(7,11),180);

    /**
     * 带时区的时间戳，秒的小数精度 p(0 ~ 9)默认6，固定13字节
     */
    public static final DbDataType TIMESTAMP_TZ  = make(DataType.TIMESTAMP_TZ, "TIMESTAMP WITH TIME ZONE", precision(0, 9).fixedBytes(13),181);

    public static final DbDataType TIMESTAMP_TZL  = TIMESTAMP.as(DataType.TIMESTAMP_TZ, "TIMESTAMP WITH LOCAL TIME ZONE", 231);

    /**
     * 时间间隔年到月，年小数精度p(0~9)默认2，固定5字节
     */
    public static final DbDataType INTERVAL_YTM  = make(DataType.INTERVAL_YTD, "INTERVAL YEAR TO MONTH", precision(0,9,2).fixedBytes(5),182);
    /**
     * 时间间隔天到秒，天小数精度p(0~9)默认2，秒的小数精度 p(0 ~ 9)默认6，固定11字节
     */
    public static final DbDataType INTERVAL_DTS  = make(DataType.INTERVAL_DTS, "INTERVAL DAY TO SECOND", precision(0,9,6).fixedBytes(11),183);

    /**
     * 可变长原生字节流，长度n(1 ~2000)字节，扩展模式（MAX_STRING_SIZE = EXTENDED）可达到32767字节
     */
    public static final DbDataType RAW = make(DataType.BINARY, "RAW", maxLength(MAX_RAW_BYTES), 23);
    /**
     * 可变长原生字节流，最长可大2Gb
     */
    public static final DbDataType LONG_RAW = make(DataType.VARBINARY, "LONG RAW", maxBytes(2 * GB), 24);

    /**
     * 行ID是Base 64编码的唯一代表一行的标识
     */
    public static final DbDataType ROW_ID = make(DataType.VARCHAR, "ROWID", 69);
    /**
     * 长度n最大和默认值 = 4000 bytes
     */
    public static final DbDataType UROW_ID = make(DataType.VARCHAR, "UROWID", 208);

    /**
     * 固定长度字符串，长度n(1 ~ 2000)，单位可以是字符或者字节
     */
    public static final DbDataType CHAR = make(DataType.CHAR, "CHAR", maxLength(MAX_STRING_SIZE/2), 96);
    /**
     * 固定长度字符串，长度n(1 ~ 2000)字节
     */
    public static final DbDataType NCHAR = make(DataType.NCHAR, "NCHAR", maxLength(MAX_STRING_SIZE/2), 96);

    public static final DbDataType CLOB = make(DataType.CLOB, "CLOB", maxBytes(4*GB-1),  112);
    public static final DbDataType NCLOB = make(DataType.NCLOB, "NCLOB",  112);

    /**
     * 二进制大对象，(4Gb - 1) * 块大小
     */
    public static final DbDataType BLOB = make(DataType.BLOB, "BLOB", 113);
    /**
     * 存储在数据库外面的大文件（最大 4Gb）
     */
    public static final DbDataType BFILE = make(DataType.BLOB, "BFILE", maxBytes(4*GB),114);


    private static final DbDataTypeMap dataTypeMap = new DbDataTypeMap(){
        {
            add(NUMBER.as(DataType.BOOL,"NUMBER(1)", true));

            add(NUMBER.as(DataType.UINT8,"NUMBER(3)"));
            add(NUMBER.as(DataType.UINT16,"NUMBER(8)"));
            add(NUMBER.as(DataType.UINT32,"NUMBER(12)"));
            add(NUMBER.as(DataType.UINT64,"NUMBER(21)"));

            add(NUMBER.as(DataType.INT8,"NUMBER(3)", true));
            add(NUMBER.as(DataType.INT16,"NUMBER(7)", true));
            add(NUMBER.as(DataType.INT32,"NUMBER(11)", true));
            add(NUMBER.as(DataType.INT64,"NUMBER(20)", true));

            add(NUMBER);
            add(NUMBER.as(DataType.NUMERIC));
            add(NUMBER.as(DataType.MONEY, "NUMBER(19,4)", true));

            add(FLOAT);
            add(REAL);
            add(DOUBLE);

            add(BINARY_FLOAT);
            add(BINARY_DOUBLE);

            add(CHAR);
            add(NCHAR);
            add(VARCHAR2);
            add(NVARCHAR2);
            add(CHAR.as(DataType.UUID,"CHAR(36)", fixedBytes(36)));
            add(VARCHAR2.as(DataType.BIT_STRING,"VARCHAR"));
            add(CLOB);
            add(NCLOB);
            add(NVARCHAR2.as(DataType.JSON, "VARCHAR2"));
            add(NVARCHAR2.as(DataType.XML, "VARCHAR2"));

            add(DATE);
            add(TIMESTAMP_TZ.as(DataType.TIME_TZ, "TIMESTAMP", true));//DataTypeHandler
            add(DATE.as(DataType.TIME, "DATE"));
            add(TIMESTAMP.as(DataType.DATETIME, "TIMESTAMP", true));
            add(TIMESTAMP);
            add(TIMESTAMP_TZ);

            add(INTERVAL_YTM);//
            add(INTERVAL_DTS);

//            add(NVARCHAR2.as(SqlDataType.VECTOR));
//            add(NUMBER.as(SqlDataType.BIT_VECTOR, "NUMBER(128)"));
            add(RAW);//BINARY
            add(LONG_RAW);//VARBINARY
            add(BLOB);
            add(BFILE);
        }
    };

    public static DbDataType getType(DataType dataType){
        return dataTypeMap.get(dataType);
    }
    private OracleDataTypes(){}
}
