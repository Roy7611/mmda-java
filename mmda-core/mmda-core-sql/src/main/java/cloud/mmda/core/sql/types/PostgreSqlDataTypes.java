package cloud.mmda.core.sql.types;

import com.google.common.collect.Range;
import cloud.mmda.core.enums.DataType;

import static cloud.mmda.core.sql.types.DbDataType.*;

/**
 * PostgreSql 数据类型
 * @see <a href="https://www.postgresql.org/docs/current/datatype.html">官方文档</a>
 *
 */
public final class PostgreSqlDataTypes {
    private static final long MAX_STRING_SIZE = 10485760L;
    //Numeric Types
    //smallint	2 bytes	small-range integer	-32768 to +32767
    //integer	4 bytes	typical choice for integer	-2147483648 to +2147483647
    //bigint	8 bytes	large-range integer	-9223372036854775808 to +9223372036854775807
    //decimal	variable	user-specified precision, exact	up to 131072 digits before the decimal point; up to 16383 digits after the decimal point
    //numeric	variable	user-specified precision, exact	up to 131072 digits before the decimal point; up to 16383 digits after the decimal point
    //real	4 bytes	variable-precision, inexact	6 decimal digits precision
    //double precision	8 bytes	variable-precision, inexact	15 decimal digits precision
    //smallserial	2 bytes	small autoincrementing integer	1 to 32767
    //serial	4 bytes	autoincrementing integer	1 to 2147483647
    //bigserial	8 bytes	large autoincrementing integer	1 to 9223372036854775807
    public static DbDataType SMALLINT = make(DataType.INT16, "smallint", fixedBytes(2).valueRange(Range.closed(-32768,32767)));
    public static DbDataType INTEGER = make(DataType.INT32, "integer", fixedBytes(4).valueRange(Range.closed(-2147483648,2147483647)));
    public static DbDataType BIGINT = make(DataType.INT64, "bigint", fixedBytes(8).valueRange(Range.closed(-9223372036854775808L,9223372036854775807L)));

    public static DbDataType DECIMAL = make(DataType.DECIMAL, "decimal", precision(0,1000).scale(-1000,1000));
    public static DbDataType NUMERIC = DECIMAL.as(DataType.NUMERIC);

    public static DbDataType REAL = make(DataType.REAL, "real", fixedBytes(4));
    public static DbDataType DOUBLE_PRECISION = make(DataType.FLOAT64, "double precision", fixedBytes(8));

    public static DbDataType SMALLSERIAL = make(DataType.UINT16, "smallserial", fixedBytes(2).valueRange(Range.closed(1,0x7FFF)));
    public static DbDataType SERIAL = make(DataType.UINT32, "serial", fixedBytes(4).valueRange(Range.closed(1,0x7FFF_FFFF)));
    public static DbDataType BIGSERIAL = make(DataType.UINT64, "bigserial", fixedBytes(8).valueRange(Range.closed(1L,9223372036854775807L)));

    //Monetary Types
    //money	8 bytes	currency amount	-92233720368547758.08 to +92233720368547758.07
    public static DbDataType MONEY = make(DataType.MONEY, "money",fixedBytes(8).valueRange(Range.closed(-92233720368547758.08, +92233720368547758.07)));

    //Character Types
    //character varying(n), varchar(n)	variable-length with limit 如果不定义长度意味着不限制
    //character(n), char(n), bpchar(n)	fixed-length, blank-padded (最慢),如果不定义长度，默认1
    //bpchar	variable unlimited length, blank-trimmed (独特扩展)
    //text	variable unlimited length 内部默认都是text
    private static Attributes defaultCharAttributes = maxLength(MAX_STRING_SIZE).maxBytes(GB); //up to 1Gb
    public static DbDataType CHARACTER_VARYING = make(DataType.VARCHAR, "character varying", defaultCharAttributes);
    public static DbDataType CHARACTER = make(DataType.CHAR, "character", defaultCharAttributes);
    public static DbDataType TEXT = make(DataType.CLOB, "text");
    public static DbDataType BPCHAR = CHARACTER.as(DataType.CHAR, "bpchar", defaultCharAttributes);
    public static DbDataType BPCHAR_VARIABLE = TEXT.as(DataType.CLOB, "bpchar varying");

    //Binary Data Types
    //bytea	1 or 4 bytes plus the actual binary string	variable-length binary string
    public static DbDataType BYTEA = make(DataType.VARBINARY, "bytea");

    //Date/Time Types
    //timestamp [ (p) ] [ without time zone ]	8 bytes	both date and time (no time zone)	4713 BC	294276 AD	1 microsecond
    //timestamp [ (p) ] with time zone	8 bytes	both date and time, with time zone	4713 BC	294276 AD	1 microsecond
    //date	4 bytes	date (no time of day)	4713 BC	5874897 AD	1 day
    //time [ (p) ] [ without time zone ]	8 bytes	time of day (no date)	00:00:00	24:00:00	1 microsecond
    //time [ (p) ] with time zone	12 bytes	time of day (no date), with time zone	00:00:00+1559	24:00:00-1559	1 microsecond
    //interval [ fields ] [ (p) ]	16 bytes	time interval	-178000000 years	178000000 years	1 microsecond
    public static DbDataType TIME = make(DataType.TIME, "time", fixedBytes(8).precision(0,6));
    public static DbDataType TIME_WITH_TIMEZONE = make(DataType.TIME_TZ, "time with time zone", fixedBytes(12).precision(0,6));
    public static DbDataType DATE = make(DataType.DATE, "date", fixedBytes(4));
    public static DbDataType TIMESTAMP = make(DataType.TIMESTAMP, "timestamp", fixedBytes(8).precision(0,6));
    public static DbDataType TIMESTAMP_WITH_TIMEZONE = make(DataType.TIMESTAMP_TZ, "timestamp with time zone", fixedBytes(12).precision(0,6));
    public static DbDataType INTERVAL = make(DataType.INTERVAL_DTS, "interval", fixedBytes(16).precision(0,6));

    //Boolean Type
    //boolean	1 byte	state of true or false
    public static DbDataType BOOLEAN = make(DataType.BOOL, "boolean", fixedBytes(1));

    //Geometric Types
    //point	16 bytes	Point on a plane	(x,y)
    //line	24 bytes	Infinite line	{A,B,C}
    //lseg	32 bytes	Finite line segment	[(x1,y1),(x2,y2)]
    //box	32 bytes	Rectangular box	(x1,y1),(x2,y2)
    //path	16+16n bytes	Closed path (similar to polygon)	((x1,y1),...)
    //path	16+16n bytes	Open path	[(x1,y1),...]
    //polygon	40+16n bytes	Polygon (similar to closed path)	((x1,y1),...)
    //circle	24 bytes	Circle	<(x,y),r> (center point and radius)

    //Network Address Types
    //cidr	7 or 19 bytes	IPv4 and IPv6 networks
    //inet	7 or 19 bytes	IPv4 and IPv6 hosts and networks
    //macaddr	6 bytes	MAC addresses
    //macaddr8	8 bytes	MAC addresses (EUI-64 format)

    //Bit String Types B'10100001'
    //bit(n) and bit varying(n), bit = bit(1), bit varying with unlimited length
    public static DbDataType BIT_VARYING = make(DataType.BIT_STRING, "bit varying", maxLength(Long.MAX_VALUE));
    //UUID Type specifically a group of 8 digits followed by three groups of 4 digits followed by a group of 12 digits,
    // for a total of 32 digits representing the 128 bits.
    public static DbDataType UUID = make(DataType.UUID, "uuid", fixedBytes(128/8));
    //XML Type xml
    public static DbDataType XML = make(DataType.XML, "xml");
    //JSON Types json and jsonb
    public static DbDataType JSON = make(DataType.JSON, "json");


    private static final DbDataTypeMap dataTypeMap = new DbDataTypeMap(){
        {
            add(BOOLEAN);//bit = bit(1)

            add(SMALLINT.as(DataType.UINT8, "smallint", valueRange(0, 255)));
            add(SMALLSERIAL.as(DataType.UINT16, "integer"));
            add(SERIAL.as(DataType.UINT32,"bigint"));
            add(BIGSERIAL.as(DataType.UINT64,"bigint"));

            add(SMALLINT.as(DataType.INT8, "smallint", valueRange(Byte.MIN_VALUE, Byte.MAX_VALUE)));
            add(SMALLINT);
            add(INTEGER);
            add(BIGINT);

            add(DECIMAL);
            add(NUMERIC);
            add(MONEY);

            add(REAL);
            add(REAL.as(DataType.FLOAT32));
            add(DOUBLE_PRECISION);

            add(CHARACTER);
            add(CHARACTER.as(DataType.NCHAR));
            add(CHARACTER_VARYING.as(DataType.VARCHAR));
            add(CHARACTER_VARYING.as(DataType.NVARCHAR));
            add(UUID);
            add(BIT_VARYING.as(DataType.BIT_STRING));
            add(BYTEA.as(DataType.CLOB));
            add(BYTEA.as(DataType.NCLOB));
            add(JSON);
            add(XML);

            add(TIME);
            add(TIME_WITH_TIMEZONE);
            add(DATE);
            add(TIMESTAMP.as(DataType.DATETIME));
            add(TIMESTAMP);
            add(TIMESTAMP_WITH_TIMEZONE);
            add(INTERVAL);

//            add(NVARCHAR2.as(SqlDataType.VECTOR));
            add(BYTEA.as(DataType.BINARY));//BINARY
            add(BYTEA.as(DataType.VARBINARY));//VARBINARY
            add(BYTEA.as(DataType.BLOB));
//            add(BFILE);
        }
    };

    public static DbDataType getType(DataType dataType){
//        if(dataType.getGenericType().isInteger() && dataType.autoIncrement()){
//            // replace with serial type
//        }
        return dataTypeMap.get(dataType);
    }

    private PostgreSqlDataTypes() {}
}
