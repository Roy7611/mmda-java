package cloud.mmda.core.data.conversion;

import cloud.mmda.core.utils.Patterns;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.*;
import java.util.BitSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 标准数据类型和字符串之间的相互转换器
 * @param <T> 目标类型
 */
public abstract class DataTypeHandler<T> implements BiConverter<String, T> {

    /**
     * T 目标类型
     */
    public abstract Class<T> targetClass();

    @Override
    public abstract T convert(String  source) ;

    @Override
    public String revert(T target){
        return target.toString();
    }

    /**
     * 从结果集中读取字段名称为 colName 的值
     * @param colName 字段名称
     * @param rs 结果集
     * @param nullable 是否允许空值
     * @return 字段值
     * @throws SQLException
     */
    public abstract T getResult(final String colName, final ResultSet rs, boolean nullable) throws SQLException;

    /**
     * 从结果集中读取字段序号为 colIdx 的值
     * @param colIdx 字段序号，从1开始为第一个字段
     * @param rs 结果集
     * @param nullable 是否允许空值
     * @return 字段值
     * @throws SQLException
     */
    public abstract T getResult(final int colIdx, final ResultSet rs, boolean nullable) throws SQLException;

    /**
     * 设置参数索引 paramIndex 的参数值到 {@link PreparedStatement}
     * @param ps 已准备的语句
     * @param paramIndex 参数索引，从1开始
     * @param value 参数值
     * @throws SQLException
     */
    public abstract void setParameter(PreparedStatement ps, int paramIndex, T value) throws SQLException;
    public void setParameter(PreparedStatement ps, int paramIndex, Object value, int jdbcType) throws SQLException{
        ps.setObject(paramIndex, value, jdbcType);
    }
    protected static boolean isNullLikeOrEmpty(final String value){
        return value == null || value.isEmpty() || "NULL".equalsIgnoreCase(value);
    }
    public static String unquoteDefValue(String defValue){
        if(isNullLikeOrEmpty(defValue)) return null;

        return defValue
                .replaceAll("^b'|'$","")
                .replaceAll("^N'|'$","")
                .replaceAll("\\(|\\)","");
    }
    public static final DataTypeHandler<String> STRING = new DataTypeHandler<String>(){
        @Override
        public Class<String> targetClass() {
            return String.class;
        }

        @Override
        public String convert(String source) {
            if(isNullLikeOrEmpty(source)) return null;
            return source;
        }
        @Override
        public String revert(String target) {
            return target;
        }

        @Override
        public String getResult(final String colName, final ResultSet rs, boolean nullable) throws SQLException {
            return rs.getString(colName);
        }

        @Override
        public String getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getString(colIdx);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, String value) throws SQLException {
            ps.setString(paramIndex, value);
        }

    };
    public static final DataTypeHandler<BitSet> BIT_STR = new DataTypeHandler<BitSet>() {

        @Override
        public Class<BitSet> targetClass() {
            return BitSet.class;
        }

        @Override
        public BitSet convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;

            var bs = source.trim();
            if(bs.startsWith("b'")) bs = bs.replaceAll("^b'|'$", "");

            BitSet bitSet = new BitSet(bs.length());
            for (int i = 0; i < bs.length(); i++) {
                if (bs.charAt(i) == '1') {
                    bitSet.set(i);
                }
            }
            return bitSet;
        }

        @Override
        public BitSet getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            return convert(rs.getString(colName));
        }

        @Override
        public BitSet getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return convert(rs.getString(colIdx));
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, BitSet value) throws SQLException {
            if (value == null) {
                ps.setString(paramIndex, null);
            }
            else if(value.isEmpty()){
                ps.setString(paramIndex, "");
            }
            else{
                int len = value.length(); // 返回最高位索引+1
                StringBuilder sb = new StringBuilder(len);
                for (int i = 0; i < len; i++) {
                    sb.append(value.get(i) ? '1' : '0');
                }
                ps.setString(paramIndex, sb.toString());
            }
        }
    };

    public static final DataTypeHandler<Boolean> BOOLEAN = new DataTypeHandler<Boolean>(){
        @Override
        public Class<Boolean> targetClass() {
            return Boolean.class;
        }
        @Override
        public Boolean convert(String source) {
            if(isNullLikeOrEmpty(source )) return null;
            else if(source.equals("0") || source.equals("b'0'") || source.equalsIgnoreCase("false") || source.equalsIgnoreCase("no")) return false;
            else if(source.equals("1") || source.equals("b'1'") || source.equalsIgnoreCase("true") || source.equalsIgnoreCase("yes")) return true;
            return Boolean.parseBoolean(source);
        }

        @Override
        public Boolean getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getBoolean(colName);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public Boolean getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getBoolean(colIdx);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Boolean value) throws SQLException {
            ps.setBoolean(paramIndex, value);
        }

    };

    public static final DataTypeHandler<Byte> BYTE = new DataTypeHandler<Byte>() {
        @Override
        public Class<Byte> targetClass() {
            return Byte.class;
        }
        @Override
        public Byte convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return Byte.decode(source);
        }

        @Override
        public Byte getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getByte(colName);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public Byte getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getByte(colIdx);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Byte value) throws SQLException {
            ps.setByte(paramIndex, value);
        }

    };
    public static final DataTypeHandler<Short> SHORT = new DataTypeHandler<Short>() {
        @Override
        public Class<Short> targetClass() {
            return Short.class;
        }
        @Override
        public Short convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return Short.decode(source);
        }

        @Override
        public Short getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getShort(colName);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public Short getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getShort(colIdx);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Short value) throws SQLException {
            ps.setShort(paramIndex, value);
        }
    };
    public static final DataTypeHandler<Integer> INTEGER = new DataTypeHandler<Integer>() {
        @Override
        public Class<Integer> targetClass() {
            return Integer.class;
        }

        @Override
        public Integer convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return Integer.decode(source);
        }

        @Override
        public Integer getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getInt(colName);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public Integer getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getInt(colIdx);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Integer value) throws SQLException {
            ps.setInt(paramIndex, value);
        }
    };
    public static final DataTypeHandler<Long> LONG = new DataTypeHandler<Long>() {
        @Override
        public Class<Long> targetClass() {
            return Long.class;
        }

        @Override
        public Long convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return Long.decode(source);
        }

        @Override
        public Long getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getLong(colName);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public Long getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getLong(colIdx);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Long value) throws SQLException {
            ps.setLong(paramIndex, value);
        }
    };
    public static final DataTypeHandler<BigInteger> BIG_INTEGER = new DataTypeHandler<BigInteger>() {
        @Override
        public Class<BigInteger> targetClass() {
            return BigInteger.class;
        }

        @Override
        public BigInteger convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return new BigInteger(source);
        }

        @Override
        public BigInteger getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getBigDecimal(colName);
            if(nullable && rs.wasNull()) return null;
            return value == null ? null : value.toBigInteger();
        }

        @Override
        public BigInteger getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getBigDecimal(colIdx);
            if(nullable && rs.wasNull()) return null;
            return value == null ? null : value.toBigInteger();
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, BigInteger value) throws SQLException {
            if(value == null) ps.setNull(paramIndex, Types.BIGINT);
            else ps.setBigDecimal(paramIndex, new BigDecimal(value));
        }
    };

    public static final DataTypeHandler<Double> DOUBLE = new DataTypeHandler<Double>() {
        @Override
        public Class<Double> targetClass() {
            return Double.class;
        }

        @Override
        public Double convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return Double.parseDouble(source);
        }

        @Override
        public Double getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getDouble(colName);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public Double getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getDouble(colIdx);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Double value) throws SQLException {
            ps.setDouble(paramIndex, value);
        }
    };
    public static final DataTypeHandler<Float> FLOAT = new DataTypeHandler<Float>() {
        @Override
        public Class<Float> targetClass() {
            return Float.class;
        }

        @Override
        public Float convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return Float.parseFloat(source);
        }

        @Override
        public Float getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getFloat(colName);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public Float getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getFloat(colIdx);
            if(nullable && rs.wasNull()) return null;
            return value;
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Float value) throws SQLException {
            ps.setFloat(paramIndex, value);
        }
    };
    public static final DataTypeHandler<BigDecimal> DECIMAL = new DataTypeHandler<BigDecimal>() {
        @Override
        public Class<BigDecimal> targetClass() {
            return BigDecimal.class;
        }

        @Override
        public BigDecimal convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return new BigDecimal(source);
        }

        @Override
        public BigDecimal getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getBigDecimal(colName);
        }

        @Override
        public BigDecimal getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getBigDecimal(colIdx);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, BigDecimal value) throws SQLException {
            ps.setBigDecimal(paramIndex, value);
        }
    };
    public static final DataTypeHandler<Date> DATE = new DataTypeHandler<Date>() {
        private static final Pattern datePattern = Pattern.compile("^(\\d{4})[-|/|.](\\d{1,2})[-|/|.](\\d{1,2})$");
        private static final Pattern currDatePattern = Pattern.compile("CURRENT_DATE|CURDATE|GETDATE|LOCALDATE(\\(\\))?", Pattern.CASE_INSENSITIVE);
        @Override
        public Class<Date> targetClass() {
            return Date.class;
        }

        /**
         * 将字符串转化为日期
         * @param source 字符串格式 yyyy-[m]m-[d]d
         * @return
         */
        @Override
        public Date convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            if(currDatePattern.matcher(source).matches()){
                return Date.valueOf(LocalDate.now());
            }
            return Date.valueOf(source);
        }

        @Override
        public Date getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getDate(colName);
        }

        @Override
        public Date getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getDate(colIdx);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Date value) throws SQLException {
            ps.setDate(paramIndex, value);
        }
    };
    public static final DataTypeHandler<Time> TIME = new DataTypeHandler<Time>() {
        private static final Pattern currTimePattern = Pattern.compile("CURRENT_TIME|CURTIME|LOCALTIME(\\(\\))?", Pattern.CASE_INSENSITIVE);

        @Override
        public Class<Time> targetClass() {
            return Time.class;
        }

        /**
         * 将字符串 source 转化为时间类型
         * @param source 字符串格式 "hh:mm:ss"
         * @return
         */
        @Override
        public Time convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            if(currTimePattern.matcher(source).matches()) return Time.valueOf(LocalTime.now());

            return Time.valueOf(source);
        }

        @Override
        public Time getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getTime(colName);
        }

        @Override
        public Time getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getTime(colIdx);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Time value) throws SQLException {
            ps.setTime(paramIndex, value);
        }
    };
    public static final DataTypeHandler<Timestamp> TIMESTAMP = new DataTypeHandler<Timestamp>() {
        private static final Pattern currTimestampPattern = Pattern.compile("CURRENT_TIMESTAMP|NOW|LOCALTIMESTAMP|GETDATE(\\(\\))?", Pattern.CASE_INSENSITIVE);

        @Override
        public Class<Timestamp> targetClass() {
            return Timestamp.class;
        }

        /**
         * 将字符串 source 转化为时间戳
         * @param source 字符串格式 {@code yyyy-[m]m-[d]d hh:mm:ss[.f...]}
         * @return
         */
        @Override
        public Timestamp convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            if(currTimestampPattern.matcher(source).matches()) return Timestamp.valueOf(LocalDateTime.now());

            return Timestamp.valueOf(source);
        }

        @Override
        public Timestamp getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getTimestamp(colName);
        }

        @Override
        public Timestamp getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getTimestamp(colIdx);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Timestamp value) throws SQLException {
            ps.setTimestamp(paramIndex, value);
        }
    };

    /**
     * 期间 (INTERVAL YEAR TO DAY) 在数据库中使用BIGINT存储（16位年-16为月-32位天），解析为{@link Period}
     *
     */
    public static final DataTypeHandler<Period> LONG_PERIOD = new DataTypeHandler<Period>() {
        private static final LongPeriodConverter _converter = new LongPeriodConverter();
        @Override
        public Class<Period> targetClass() {
            return Period.class;
        }

        @Override
        public Period convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            var value = Long.parseLong(source);
            return _converter.convert(value);
        }

        @Override
        public String revert(Period target) {
            if(target == null) return null;
            var value = _converter.revert(target);
            return String.valueOf(value);
        }

        @Override
        public Period getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getLong(colName);
            if(nullable && rs.wasNull()) return null;
            return _converter.convert(value);
        }

        @Override
        public Period getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getLong(colIdx);
            if(nullable && rs.wasNull()) return null;
            return _converter.convert(value);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Period value) throws SQLException {
            if(value == null) ps.setNull(paramIndex, Types.BIGINT);
            else ps.setLong(paramIndex, _converter.revert(value));
        }
    };

    /**
     * 期间 (INTERVAL DAY(p) TO SECOND(s)) 在数据库中使用DECIMAL存储。
     * 整数部分（16位天-16位时-16位分-16位秒），小数部分为纳秒。解析为{@link Duration}
     */
    public static final DataTypeHandler<Duration> DURATION = new DataTypeHandler<Duration>() {
        private static final BigDecimalDurationConverter _converter = new BigDecimalDurationConverter();
        @Override
        public Class<Duration> targetClass() {
            return Duration.class;
        }

        /**
         * {@inheritDoc}
         * @param source ISO-8601 duration format PnDTnHnMn. nS
         */
        @Override
        public Duration convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return _converter.convert(new BigDecimal(source));
        }

        @Override
        public Duration getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getBigDecimal(colName);
            if(nullable && rs.wasNull()) return null;
            return _converter.convert(value);
        }

        @Override
        public Duration getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            var value = rs.getBigDecimal(colIdx);
            if(nullable && rs.wasNull()) return null;
            return _converter.convert(value);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Duration value) throws SQLException {
            ps.setBigDecimal(paramIndex, _converter.revert(value));
        }

    };

    public static final DataTypeHandler<BitSet> BIT_SET = new DataTypeHandler<BitSet>() {
        @Override
        public Class<BitSet> targetClass() {
            return BitSet.class;
        }

        @Override
        public BitSet convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return BitSet.valueOf(source.getBytes(StandardCharsets.US_ASCII));
        }

        @Override
        public BitSet getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            var bytes = rs.getBytes(colName);
            if(nullable && rs.wasNull() || bytes == null) return null;
            return BitSet.valueOf(bytes);
        }

        @Override
        public BitSet getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            var bytes = rs.getBytes(colIdx);
            if(nullable && rs.wasNull() || bytes == null) return null;
            return BitSet.valueOf(bytes);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, BitSet value) throws SQLException {
            ps.setBytes(paramIndex, value.toByteArray());
        }
    };

    public static final DataTypeHandler<InputStream> BYTE_STREAM = new DataTypeHandler<InputStream>() {

        @Override
        public Class<InputStream> targetClass() {
            return InputStream.class;
        }

        @Override
        public InputStream convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return new ByteArrayInputStream(source.getBytes(StandardCharsets.US_ASCII));
        }

        @Override
        public InputStream getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getBinaryStream(colName);
        }

        @Override
        public InputStream getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getBinaryStream(colIdx);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, InputStream value) throws SQLException {
            ps.setBinaryStream(paramIndex, value);
        }
    };
    public static final DataTypeHandler<InputStream> ASCII_STREAM = new DataTypeHandler<InputStream>(){
        @Override
        public Class<InputStream> targetClass() {
            return InputStream.class;
        }

        @Override
        public InputStream convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return new ByteArrayInputStream(source.getBytes(StandardCharsets.US_ASCII));
        }

        @Override
        public InputStream getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getBinaryStream(colName);
        }

        @Override
        public InputStream getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getBinaryStream(colIdx);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, InputStream value) throws SQLException {
            ps.setAsciiStream(paramIndex, value);
        }
    };
    public static final DataTypeHandler<Blob> BLOB = new DataTypeHandler<Blob>(){
        @Override
        public Class<Blob> targetClass() {
            return Blob.class;
        }

        @Override
        public Blob convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Blob getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getBlob(colName);
        }

        @Override
        public Blob getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getBlob(colIdx);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Blob value) throws SQLException {
            ps.setBlob(paramIndex, value);
        }
    };
    public static final DataTypeHandler<Clob> CLOB = new DataTypeHandler<Clob>(){
        @Override
        public Class<Clob> targetClass() {
            return Clob.class;
        }

        @Override
        public Clob convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Clob getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getClob(colName);
        }

        @Override
        public Clob getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getClob(colIdx);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Clob value) throws SQLException {
            ps.setClob(paramIndex, value);
        }
    };
    public static final DataTypeHandler<NClob> NCLOB = new DataTypeHandler<NClob>(){

        @Override
        public Class<NClob> targetClass() {
            return NClob.class;
        }

        @Override
        public NClob convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public NClob getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getNClob(colName);
        }

        @Override
        public NClob getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getNClob(colIdx);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, NClob value) throws SQLException {
            ps.setNClob(paramIndex, value);
        }
    };
    public static final DataTypeHandler<Object> OBJECT = new DataTypeHandler<Object>() {

        @Override
        public Class<Object> targetClass() {
            return Object.class;
        }

        @Override
        public Object convert(String source) {
            if (isNullLikeOrEmpty(source)) return null;
            return source;
        }

        @Override
        public Object getResult(String colName, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getObject(colName);
        }

        @Override
        public Object getResult(int colIdx, ResultSet rs, boolean nullable) throws SQLException {
            return rs.getObject(colIdx);
        }

        @Override
        public void setParameter(PreparedStatement ps, int paramIndex, Object value) throws SQLException {
            ps.setObject(paramIndex, value);
        }
    };
}
