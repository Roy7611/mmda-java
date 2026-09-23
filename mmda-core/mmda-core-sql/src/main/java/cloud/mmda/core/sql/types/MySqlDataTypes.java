package cloud.mmda.core.sql.types;

import cloud.mmda.core.enums.DataType;
import com.google.common.collect.Range;

import java.math.BigInteger;
import java.sql.Time;

import static cloud.mmda.core.sql.types.DbDataType.*;

/**
 * MySql 数据类型定义
 * @see <a href="https://dev.mysql.com/doc/refman/8.4/en/data-types.html">官方文档 V8.4</a>
 */
public final class MySqlDataTypes {

    ////////////////////////////
    // Numeric Data Types
    ////////////////////////////
    // MySQL supports all standard SQL numeric data types.
    // These types include the exact numeric data types (INTEGER, SMALLINT, DECIMAL, and NUMERIC),
    // as well as the approximate numeric data types (FLOAT, REAL, and DOUBLE PRECISION).
    // The keyword INT is a synonym for INTEGER, and the keywords DEC and FIXED are synonyms for DECIMAL.
    // MySQL treats DOUBLE as a synonym for DOUBLE PRECISION (a nonstandard extension).
    // MySQL also treats REAL as a synonym for DOUBLE PRECISION (a nonstandard variation),
    // unless the REAL_AS_FLOAT SQL mode is enabled.


    /**
     * BIT[(M)]
     *
     * A bit-value type. M indicates the number of bits per value, from 1 to 64. The default is 1 if M is omitted.
     */
    public static DbDataType BIT = make(DataType.BIT_VECTOR,"BIT", maxLength(1L,64L, 1L));

    /**
     * TINYINT[(M)] [UNSIGNED] [ZEROFILL]
     *
     * A very small integer. The signed range is -128 to 127. The unsigned range is 0 to 255.
     */
    public static DbDataType TINYINT = make(DataType.INT8,"TINYINT", valueRange(Byte.MIN_VALUE, Byte.MAX_VALUE).fixedBytes(1));
    public static DbDataType TINYINT_UNSIGNED = make(DataType.UINT8,"TINYINT UNSIGNED", valueRange(0, 255).fixedBytes(1));

    /**
     * BOOL, BOOLEAN
     *
     * These types are synonyms for TINYINT(1). A value of zero is considered false. Nonzero values are considered true:
     */
    public static DbDataType BOOL = TINYINT.as(DataType.BOOL, "BIT(1)", valueRange(0,1));

    /**
     * SMALLINT[(M)] [UNSIGNED] [ZEROFILL]
     *
     * A small integer. The signed range is -32768 to 32767. The unsigned range is 0 to 65535.
     */
    public static DbDataType SMALLINT = make(DataType.INT16,"SMALLINT", valueRange(Short.MIN_VALUE, Short.MAX_VALUE).fixedBytes(2));
    public static DbDataType SMALLINT_UNSIGNED = make(DataType.UINT16,"SMALLINT UNSIGNED", valueRange(0, 65535).fixedBytes(2));

    /**
     * MEDIUMINT[(M)] [UNSIGNED] [ZEROFILL] (不用)
     *
     * A medium-sized integer. The signed range is -8388608 to 8388607. The unsigned range is 0 to 16777215.
     */
//    public static DbDataType MEDIUMINT = make(SqlDataType.INT,"MEDIUMINT", valueRange(-8388608, 8388607).fixedBytes(3));

     /**
     * INT[(M)] [UNSIGNED] [ZEROFILL] （INTEGER）
     *
     * A normal-size integer. The signed range is -2147483648 to 2147483647. The unsigned range is 0 to 4294967295.
     */
     public static DbDataType INT = make(DataType.INT32,"INT", valueRange(Integer.MIN_VALUE, Integer.MAX_VALUE).fixedBytes(4));
     public static DbDataType INT_UNSIGNED = make(DataType.UINT32,"INT UNSIGNED", valueRange(0L, LONG_SIZE).fixedBytes(4));

    /**
     * BIGINT[(M)] [UNSIGNED] [ZEROFILL]
     *
     * A large integer. The signed range is -9223372036854775808 to 9223372036854775807. The unsigned range is 0 to 18446744073709551615.
     *
     * SERIAL is an alias for BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE.
     */
    public static DbDataType BIGINT = make(DataType.INT64,"BIGINT", valueRange(Long.MIN_VALUE, Long.MAX_VALUE).fixedBytes(8));
    public static DbDataType BIGINT_UNSIGNED = make(DataType.UINT64,"BIGINT UNSIGNED", valueRange(BigInteger.ZERO, new BigInteger("18446744073709551615")).fixedBytes(8));
    public static DbDataType SERIAL = BIGINT_UNSIGNED.as(DataType.UINT64, "BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE");

    /**
     * DECIMAL[(M[,D])] [UNSIGNED] [ZEROFILL] 官方建议不要使用UNSIGNED，而改用CHECK
     * <p>
     * A packed “exact” fixed-point number. M is the total number of digits (the precision) and D is the number of digits
     * after the decimal point (the scale). The decimal point and (for negative numbers) the - sign are not counted in M.
     * If D is 0, values have no decimal point or fractional part. The maximum number of digits (M) for DECIMAL is 65.
     * The maximum number of supported decimals (D) is 30. If D is omitted, the default is 0.
     * If M is omitted, the default is 10. (There is also a limit on how long the text of DECIMAL literals can be
     */
    public static DbDataType DECIMAL = make(DataType.DECIMAL,"DECIMAL", precision(0,65).scale(0,30));

    /**
     * FLOAT(p) [UNSIGNED] [ZEROFILL]
     *
     * p is from 0 to 24, the data type becomes FLOAT with no M or D values.
     * If p is from 25 to 53, the data type becomes DOUBLE with no M or D values.
     */
    public static DbDataType FLOAT = make(DataType.FLOAT32,"FLOAT", valueRange(Float.MIN_VALUE,Float.MAX_VALUE).fixedBytes(4));
    /**
     * DOUBLE[(M,D)] [UNSIGNED] [ZEROFILL]
     *
     * 别名 REAL
     */
    public static DbDataType DOUBLE = make(DataType.FLOAT64,"DOUBLE", valueRange(Double.MIN_VALUE,Double.MAX_VALUE).fixedBytes(8));

    ////////////////////////////
    // Date and Time Data Types
    ////////////////////////////

    /**
     * DATE
     *
     * A date. The supported range is '1000-01-01' to '9999-12-31'.
     * MySQL displays DATE values in 'YYYY-MM-DD' format,
     * but permits assignment of values to DATE columns using either strings or numbers.
     */
    public static DbDataType DATE = make(DataType.DATE,"DATE", fixedBytes(3).dateBetween("1000-01-01","9999-12-31"));

    /**
     * TIME values may range from '-838:59:59' to '838:59:59'.
     * The hours part may be so large because the TIME type can be used not only
     * to represent a time of day (which must be less than 24 hours),
     * but also elapsed time or a time interval between two events
     * (which may be much greater than 24 hours, or even negative).
     *
     * With the fractional part included, the range for TIME values is '-838:59:59.000000' to '838:59:59.000000'.
     *
     * 3 bytes + fractional seconds storage（0-3）
     */
    public static DbDataType TIME = make(DataType.TIME,"TIME", maxPrecision(6).inBytes(3,6).valueRange(Range.closed(Time.valueOf("00:00:00"), Time.valueOf("23:59:59"))));

    /**
     * MySQL retrieves and displays DATETIME values in 'YYYY-MM-DD hh:mm:ss' format.
     * The supported range is '1000-01-01 00:00:00' to '9999-12-31 23:59:59'.
     */
    public static DbDataType DATETIME = make(DataType.DATETIME,"DATETIME", maxPrecision(6).inBytes(5,8).dateTimeBetween("1000-01-01 00:00:00.000000", "9999-12-31 23:59:59.499999"));

    /**
     * the range for TIMESTAMP values is '1970-01-01 00:00:01.000000' to '2038-01-19 03:14:07.499999'.
     */
    public static DbDataType TIMESTAMP = make(DataType.TIMESTAMP,"TIMESTAMP", maxPrecision(6).inBytes(4,7).dateTimeBetween("1970-01-01 00:00:01.000000", "2038-01-19 03:14:07.499999"));

    ////////////////////////////
    // String Data Types (ENUM, SET我们不用)
    ////////////////////////////

    /**
     * [NATIONAL] CHAR[(M)] [CHARACTER SET charset_name] [COLLATE collation_name]
     * CHARACTER SET latin1 => ASCII
     */
    public static DbDataType CHAR = make(DataType.CHAR, "CHAR", maxLength(0L,TINY_SIZE, 10L));
    /**
     * 以下等同
     * <p>
     * CHAR(10) CHARACTER SET UTF8MB4 <br>
     * NATIONAL CHARACTER(10) <br>
     * NCHAR(10) <br>
     */
    public static DbDataType NCHAR = make(DataType.NCHAR, "NCHAR", maxLength(0L,TINY_SIZE, 10L));

    /**
     *  VARCHAR(M) A variable-length string. M represents the maximum column length in characters.
     *  The range of M is 0 to 65,535. 受到行字节数的限制，实际达不到65535长度，跟字符集有关
     *  <p>
     *  values as a 1-byte or 2-byte length prefix plus data.
     *  The length prefix indicates the number of bytes in the value.
     *  A VARCHAR column uses one length byte if values require no more than 255 bytes,
     *  two length bytes if values may require more than 255 bytes.
     *
     */
    public static DbDataType VARCHAR = make(DataType.VARCHAR, "VARCHAR", maxLength(0L,SHORT_SIZE, 50L));

    /**
     * 以下等同
     * <p>
     * VARCHAR(10) CHARACTER SET UTF8MB4 <br>
     * NATIONAL VARCHAR(10) <br>
     * NVARCHAR(10) <br>
     * NCHAR VARCHAR(10) <br>
     * NATIONAL CHARACTER VARYING(10) <br>
     * NATIONAL CHAR VARYING(10) <br>
     */
    public static DbDataType NVARCHAR = make(DataType.NVARCHAR, "NVARCHAR", maxLength(0L,SHORT_SIZE, 50L));

    /**
     * TEXT[(M)] [CHARACTER SET charset_name] [COLLATE collation_name]
     *
     * <p>
     * A TEXT column with a maximum length of 65,535 (2^16 − 1) bytes.
     * The effective maximum length is less if the value contains multibyte characters.
     * Each TEXT value is stored using a 2-byte length prefix that indicates the number of bytes in the value.
     * <p>
     *
     * An optional length M can be given for this type. If this is done,
     * MySQL creates the column as the smallest TEXT type large enough to hold values M characters long.
     * <p>
     * MySql 分TINYTEXT, TEXT, MEDIUMTEXT, LONGTEXT仅仅长度不一2^(8,16,24,32)
     */
    public static DbDataType TEXT = make(DataType.CLOB, "TEXT", maxLength(SHORT_SIZE));//长度可选
    public static DbDataType LONGTEXT = TEXT.as(DataType.CLOB, "LONGTEXT", maxLength(LONG_SIZE));

    /**
     * BINARY[(M)] 别名 CHAR BYTE
     * <p>
     * The BINARY type is similar to the CHAR type, but stores binary byte strings rather than nonbinary character strings.
     * An optional length M represents the column length in bytes. If omitted, M defaults to 1.
     *
     * <p>
     * Specifying the CHARACTER SET binary attribute causes: <br>
     * 1. CHAR -> BINARY <br>
     * 2. VARCHAR -> VARBINARY <br>
     * 3. TEXT -> BLOB <br>
     */
    public static DbDataType BINARY = make(DataType.BINARY, "BINARY", maxLength(0L,TINY_SIZE, 1L));


    /**
     * VARBINARY(M)
     *
     * The VARBINARY type is similar to the VARCHAR type, but stores binary byte strings rather than nonbinary character strings.
     * M represents the maximum column length in bytes.
     */
    public static DbDataType VARBINARY = make(DataType.VARBINARY, "VARBINARY", maxLength(0L,SHORT_SIZE, 50L));

    /**
     * BLOB[(M)]
     *
     * A BLOB column with a maximum length of 65,535 (2^16 − 1) bytes.
     * Each BLOB value is stored using a 2-byte length prefix that indicates the number of bytes in the value.
     *
     * An optional length M can be given for this type. If this is done,
     * MySQL creates the column as the smallest BLOB type large enough to hold values M bytes long.
     *
     * <p>
     * MySql 分TINYBLOB, BLOB, MEDIUMBLOB, LONGBLOB 仅仅长度不一2^(8,16,24,32)
     */
    public static DbDataType BLOB = make(DataType.BLOB, "BLOB", maxLength(SHORT_SIZE));
    public static DbDataType LONGBLOB = BLOB.as(DataType.BLOB, "LONGBLOB", maxLength(LONG_SIZE));

    ////////////////////////////
    // Spatial Data Types
    ////////////////////////////

    ////////////////////////////
    // The JSON Data Type
    ////////////////////////////

    /**
     * The space required to store a JSON document is roughly the same as for LONGBLOB or LONGTEXT
     * BLOB and TEXT columns cannot have DEFAULT values.
     */

    private static final DbDataTypeMap dataTypeMap = new DbDataTypeMap(){
        {
            add(BOOL);

            add(TINYINT_UNSIGNED);
            add(SMALLINT_UNSIGNED);
            add(INT_UNSIGNED);
            add(BIGINT_UNSIGNED);

            add(TINYINT);
            add(SMALLINT);
            add(INT); //MEDIUMINT 转为 INT
            add(BIGINT);

            add(DECIMAL);
            add(DECIMAL.as(DataType.NUMERIC));
            add(DECIMAL.as(DataType.MONEY, "DECIMAL(20,4)", true));

            add(FLOAT);
            add(FLOAT.as(DataType.REAL)); //REAL 实际使用FLOAT
            add(DOUBLE);

            add(CHAR);
            add(NCHAR);
            add(VARCHAR);
            add(NVARCHAR);
            add(CHAR.as(DataType.UUID,"CHAR(36)", fixedBytes(36)));
            add(VARCHAR.as(DataType.BIT_STRING,"VARCHAR"));//TEXT尺寸与VARCHAR一样
            add(LONGTEXT.as(DataType.CLOB, "LONGTEXT")); // CLOB 实际是 LONGTEXT
            add(LONGTEXT.as(DataType.NCLOB, "LONGTEXT CHARACTER SET UTF8"));
            add(LONGTEXT.as(DataType.JSON));
            add(LONGTEXT.as(DataType.XML));

            add(DATE);
            add(TIME);
            add(DATETIME);
            add(TIMESTAMP);
            //MySql自动存入UTC时间，每个连接取出时指定时区，它自动转换
            add(TIMESTAMP.as(DataType.TIMESTAMP_TZ));
            //它没有INTERVAL类型 需要 DataTypeHandler 自定义支持

//            add(NVARCHAR2.as(SqlDataType.VECTOR));
            add(BIT);// BIT VECTOR(1-64)
            add(BINARY);//BINARY
            add(VARBINARY);//VARBINARY 和 BLOB 在MySql中等同
            add(LONGBLOB);//LONGBLOB 我们认为是真正的 BLOB
//            add(BFILE);
        }
    };

    public static DbDataType getType(DataType dataType){
        return dataTypeMap.get(dataType);
    }
    private MySqlDataTypes() {}
}
