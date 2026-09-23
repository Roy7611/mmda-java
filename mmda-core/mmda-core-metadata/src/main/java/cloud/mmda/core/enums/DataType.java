package cloud.mmda.core.enums;

import cloud.mmda.core.data.conversion.DataTypeHandler;
import lombok.Getter;

import java.sql.Types;
import java.util.*;

/**
 * 数据类型枚举定义了通用的数据类型，用于在不同编程语言、数据库之间创建类型映射。
 * <p>
 * 例如：设备通信协议支持的数据类型(DeviceDataType)、数据库字段数据类型（DbDataType）都会映射到统一数据类型枚举。
 * 通用数据类型默认实现了到{@link #jdbcType}的映射和{@link #defaultHandler}数据类型处理器，用于通过JDBC读写数据库。
 */
@Getter
public enum DataType implements EnumValue<Integer> {
    //Boolean
    BOOL        (0,  Types.BOOLEAN,     DataTypeHandler.BOOLEAN,  "boolean"),

    //Unsigned integer
    UINT8       (1,  Types.SMALLINT,    DataTypeHandler.SHORT,    "unsigned byte"),
    UINT16      (2,  Types.INTEGER,     DataTypeHandler.INTEGER,  "unsigned short"),
    UINT24      (3,  Types.INTEGER,     DataTypeHandler.INTEGER,  "unsigned medium int"),
    UINT32      (4,  Types.BIGINT,      DataTypeHandler.LONG,     "unsigned int"),
    UINT64      (5,  Types.BIGINT,      DataTypeHandler.BIG_INTEGER,"unsigned long"),

    //Integer
    INT8        (9,  Types.TINYINT,     DataTypeHandler.BYTE,     "byte"),
    INT16       (10, Types.SMALLINT,    DataTypeHandler.SHORT,    "short"),
    INT24       (11, Types.INTEGER,     DataTypeHandler.INTEGER,  "medium int"),
    INT32       (12, Types.INTEGER,     DataTypeHandler.INTEGER,  "int"),
    INT64       (13, Types.BIGINT,      DataTypeHandler.LONG,     "long"),

    //Numeric includes fixed and float point
    DECIMAL     (16, Types.DECIMAL,     DataTypeHandler.DECIMAL,  "decimal"),
    NUMERIC     (17, Types.NUMERIC,     DataTypeHandler.DECIMAL,  "numeric"),
    MONEY       (18, Types.DECIMAL,     DataTypeHandler.DECIMAL,  "money"),
    FLOAT32     (20, Types.FLOAT,       DataTypeHandler.FLOAT,    "float"),
    REAL        (21, Types.REAL,        DataTypeHandler.FLOAT,    "real"),
    FLOAT64     (24, Types.DOUBLE,      DataTypeHandler.DOUBLE,   "double"),

    //Text such as CHAR, VARCHAR, TEXT, CLOB
    CHAR        (32, Types.CHAR,        DataTypeHandler.STRING,    "character"),
    NCHAR       (33, Types.NCHAR,       DataTypeHandler.STRING,    "national character"),
    VARCHAR     (34, Types.VARCHAR,     DataTypeHandler.STRING,    "character varying"),
    NVARCHAR    (35, Types.NVARCHAR,    DataTypeHandler.STRING,    "national character varying"),
    UUID        (36, Types.CHAR,        DataTypeHandler.STRING,    "UUID"),
    BIT_STRING  (38, Types.VARCHAR,     DataTypeHandler.BIT_STR,   "bit string"),//二进制字符串，b'01010001'
    CLOB        (40, Types.CLOB,        DataTypeHandler.CLOB,      "character large object"),
    NCLOB       (41, Types.NCLOB,       DataTypeHandler.NCLOB,     "national character large object"),
    JSON        (42, Types.LONGNVARCHAR,DataTypeHandler.STRING,    "json"),
    XML         (43, Types.SQLXML,      DataTypeHandler.STRING,    "xml"),

    //Date & Time
    DATE        (64, Types.DATE,        DataTypeHandler.DATE,    "Date"),
    TIME        (65, Types.TIME,        DataTypeHandler.TIME,    "Time"),
    TIME_TZ     (66, Types.TIME_WITH_TIMEZONE, DataTypeHandler.TIME,"Time with timezone"),
    DATETIME    (67, Types.TIMESTAMP,   DataTypeHandler.TIMESTAMP,"DateTime"),
    TIMESTAMP   (68, Types.TIMESTAMP,   DataTypeHandler.TIMESTAMP,"Timestamp"),
    TIMESTAMP_TZ(69, Types.TIMESTAMP_WITH_TIMEZONE, DataTypeHandler.TIMESTAMP,"Timestamp with timezone"),
    INTERVAL_YTD(70, Types.BIGINT,      DataTypeHandler.LONG_PERIOD,"Interval year to day"),
    INTERVAL_DTS(71, Types.DECIMAL,     DataTypeHandler.DURATION,"Interval day to second"),

    //Vector & array such as BINARY, BLOB, IMAGE etc.
    VECTOR      (128, Types.ARRAY,      DataTypeHandler.OBJECT,"Vector<E>"),//向量，默认浮点32
    BIT_VECTOR  (129, Types.BINARY,     DataTypeHandler.BIT_SET,"bit vector"),//比特向量，例如整形数当作BitVector32
    BINARY      (136, Types.BINARY,     DataTypeHandler.BYTE_STREAM,"binary"), //byte array
    VARBINARY   (137, Types.VARBINARY,  DataTypeHandler.BYTE_STREAM,"binary varying"),
    BLOB        (138, Types.BLOB,       DataTypeHandler.BLOB,"binary large object"),
    BFILE       (139, Types.DATALINK,   DataTypeHandler.OBJECT,"binary file"),

    GEOGRAPHY   (256, Types.STRUCT,     DataTypeHandler.OBJECT,"Geography"),//圆形地球坐标系
    GEOMETRY    (320, Types.STRUCT,     DataTypeHandler.OBJECT,"Geometry"),//欧几里得（平面）坐标系

    ANY         (512, Types.JAVA_OBJECT,DataTypeHandler.OBJECT,"Any");

    private final Integer value;

    private final String text;

    /**
     * Jdbc类型是 {@link Types}中定义的值，用于读写数据库
     */
    private final int jdbcType;

    final DataTypeHandler<?> defaultHandler;

    DataType(int value, int jdbcType, DataTypeHandler<?> defaultHandler, String text){
        this.value = value;
        this.jdbcType = jdbcType;
        this.defaultHandler = defaultHandler;
        this.text = text;
    }

    /**
     * 获取对应的Java类型，例如枚举值{@link #BOOL}对应{@link Boolean}
     */
    public Class<?> getJavaClass(){
        return defaultHandler.targetClass();
    }
    //region 类型判断函数
    private static boolean isBetween(int value, int min, int max){
        return value >= min && value < max;
    }
    public boolean isBool(){
        return BOOL.equals(this);
    }
    public boolean isNumber(){
        return isBetween(this.value, UINT8.value,32);//1~32
    }
    public boolean isInteger(){
        return isBetween(this.value,UINT8.value, 16);//1~16
    }
    public boolean isBigInt(){
        return INT64.equals(this) || UINT64.equals(this);
    }
    public boolean isUnsignedInteger(){
        return isBetween(this.value,UINT8.value, 8);//1~8
    }
    public boolean isSignedInteger(){
        return isBetween(this.value,INT8.value, 16);
    }
    public boolean isFloatPoint(){
        return FLOAT32.equals(this) || FLOAT64.equals(this);
    }
    public boolean isDecimal(){
        return isBetween(this.value, DECIMAL.value, FLOAT32.value);
    }
    /**
     * 是否包含日期数据
     * @return
     */
    public boolean hasDatePart(){
        return DATE.equals(this) || DATETIME.equals(this) || TIMESTAMP.equals(this) || TIMESTAMP_TZ.equals(this);
    }
    public boolean isDate(){
        return DATE.equals(this);
    }
    public boolean isTime(){
        return TIME.equals(this);
    }
    public boolean isDateTime(){
        return DATETIME.equals(this) || TIMESTAMP.equals(this) || TIMESTAMP_TZ.equals(this);
    }
    public boolean isDateOrTime(){
        return isBetween(this.value, DATE.value, VECTOR.value);
    }
    public boolean isString(){
        return isBetween(this.value, CHAR.value, DATE.value);//32~64
    }
    public boolean isQuotable(){
        return isString() || isDateOrTime();
    }

    public boolean isFixedLengthString(){
        return CHAR.equals(this) || NCHAR.equals(this) || UUID.equals(this);
    }
    //endregion

    /**
     * 根据值 value 映射到枚举
     * @param value 值，例如 0
     * @return 返回枚举，例如 BOOL
     */
    public static DataType valueOf(int value){
        return _enumMap.get(value);
    }

    private static final Map<Integer,DataType> _enumMap = new LinkedHashMap<>(){
        {
            put(BOOL.value,BOOL);

            put(UINT8.value,UINT8);
            put(UINT16.value,UINT16);
            put(UINT32.value,UINT32);
            put(UINT64.value,UINT64);

            put(INT8.value,INT8);
            put(INT16.value,INT16);
            put(INT32.value,INT32);
            put(INT64.value,INT64);

            put(DECIMAL.value,DECIMAL);
            put(FLOAT32.value,FLOAT32);
            put(FLOAT64.value,FLOAT64);

            put(CHAR.value,CHAR);
            put(NCHAR.value,NCHAR);
            put(VARCHAR.value,VARCHAR);
            put(NVARCHAR.value,NVARCHAR);
            put(UUID.value,UUID);
            put(BIT_STRING.value,BIT_STRING);
            put(CLOB.value,CLOB);
            put(NCLOB.value,NCLOB);
            put(JSON.value,JSON);
            put(XML.value,XML);

            put(DATE.value,DATE);
            put(TIME.value,TIME);
            put(DATETIME.value,DATETIME);
            put(TIMESTAMP.value,TIMESTAMP);
            put(INTERVAL_YTD.value, INTERVAL_YTD);
            put(INTERVAL_DTS.value, INTERVAL_DTS);

            put(VECTOR.value,VECTOR);
            put(BIT_VECTOR.value,BIT_VECTOR);
            put(BINARY.value,BINARY);
            put(VARBINARY.value,VARBINARY);
            put(BLOB.value,BLOB);

            put(BFILE.value,BFILE);

            put(GEOGRAPHY.value,GEOGRAPHY);
            put(GEOMETRY.value,GEOMETRY);
            put(ANY.value,ANY);
        }
    };
}
