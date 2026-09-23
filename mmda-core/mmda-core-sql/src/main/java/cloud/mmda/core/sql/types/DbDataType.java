package cloud.mmda.core.sql.types;

import cloud.mmda.core.enums.DataType;
import cloud.mmda.core.enums.DataTypeAttribute;
import cloud.mmda.core.enums.DataTypeAttributeSet;
import cloud.mmda.core.metadata.MetaRange;
import com.google.common.collect.Range;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Objects;

/**
 * 数据库数据类型记录，用于各个数据库厂商不同数据类型映射
 *
 * @param dataType 通用SQL数据类型，设计时数据库无关
 * @param vendorType 厂商特有的类型名称，例如VARCHAR2,MEDIUMINT,SMALLINT，用于生成DDL脚本
 * @param vendorCode 厂商定义的编码
 * @param nps 是否有长度、精度、小数参数
 * @param attributes 厂商定义的扩展属性
 */
public record DbDataType(DataType dataType, String vendorType, Integer vendorCode, Attributes attributes, DataTypeAttributeSet nps) {

    public DbDataType{
        Objects.requireNonNull(dataType, "SqlDataType cannot be null");
        if(vendorType == null) vendorType = dataType.name();
        if(attributes == null) attributes = new Attributes();
        if(nps == null) nps = attributes.propertySet();
    }

    /**
     * 返回对应的Jdbc数据类型，参考{@link java.sql.Types}
     * @return 整形值
     */
    public int jdbcType(){
        return dataType.getJdbcType();
    }
    /**
     * 定长字节长度
     * @return
     */
    public Integer fixedByteLength(){
        var property = getAttributeValue(DataTypeAttribute.BYTE_RANGE);
        if( property instanceof Integer i) return i / 8;
        return null;
    }

    public Object getAttributeValue(DataTypeAttribute attribute){
        if(attribute == null || attribute == DataTypeAttribute.NONE) return null;
        return attributes.get(attribute);
    }

    /**
     * 获取Jdbc类型处理器，例如CLOB类型的数据读写需要特殊处理，且各厂家有差异
     * @return 如果有特殊的数据类型处理则返回，否则返回空值
     */
    public JdbcTypeHandler<?> getJdbcTypeHandler(){
        if(attributes != null && attributes.containsKey(DataTypeAttribute.TYPE_HANDLER)){
            return attributes.getTypeHandler();
        }
        return null;
    }

    /**
     * 描述数据类型的SQL定义，例如VARCHAR(100), DECIMAL(18,3)
     * @param sb 写入此字符串构建器
     * @param maxLength 传入最大长度
     * @param precision 精度
     * @param scale 小数
     * @return 数据类型SQL定义脚本
     */
    public String describe(StringBuilder sb, Long maxLength, Integer precision, Integer scale){
        sb.append(vendorType);
        if(!nps.getEnumSet().isEmpty()){

            if(nps.hasFlag(DataTypeAttribute.MAX_LENGTH) && maxLength != null){
                if(maxLength == null) maxLength = attributes.getMaxLength().getDefaultValue();
                if(maxLength != null) {
                    sb.append('(').append(maxLength).append(')');
                }
            }
            else if(nps.hasFlag(DataTypeAttribute.PRECISION) && precision != null)
            {
                if(precision == null) precision = attributes.getPrecision();
                sb.append('(').append(precision);
                if(nps.hasFlag(DataTypeAttribute.SCALE) && scale != null){
                    if(scale == null) scale = attributes.getScale().getDefaultValue();
                    sb.append(scale).append(')');
                }
            }
        }
        else{
            sb.append(' ');
        }
        return sb.toString();
    }
    //region 原型克隆 as

    public DbDataType as(DataType dataType, String vendorType, Attributes attributes){
        if(attributes == null) attributes = new Attributes();
        return new DbDataType(dataType, vendorType, vendorCode, attributes, attributes.propertySet());
    }
    public DbDataType as(DataType dataType, String vendorType, int vendorCode){
        return new DbDataType(dataType, vendorType, vendorCode, attributes, attributes.propertySet());
    }

    public DbDataType as(DataType dataType, String vendorType, boolean emptyProperties){
        if(emptyProperties) return new DbDataType(dataType, vendorType, vendorCode, null, null);
        return new DbDataType(dataType, vendorType, vendorCode, attributes, nps);
    }
    public DbDataType as(DataType dataType, String vendorType){
        return as(dataType, vendorType, false);
    }
    public DbDataType as(DataType dataType){
        return as(dataType, vendorType, false);
    }
    //endregion of as

    //region 新造 make
    public static DbDataType make(DataType dataType, String vendorType, Attributes attributes, Integer vendorCode){
        if(attributes == null) attributes = new Attributes();
        return new DbDataType(dataType,vendorType,vendorCode, attributes, attributes.propertySet());

    }
    public static DbDataType make(DataType dataType, String vendorType, Attributes attributes){
        return make(dataType,vendorType, attributes, null);
    }
    public static DbDataType make(DataType dataType, String vendorType, int vendorCode){
        return make(dataType,vendorType,null, vendorCode);
    }
    public static DbDataType make(DataType dataType, String vendorType){
        return make(dataType, vendorType, null);
    }

    public static DbDataType make(DataType dataType, Attributes attributes){
        return make(dataType, dataType.name(), attributes, null);
    }
    //endregion of make

    //region 属性定义
    public static Attributes maxLength(long length){
        return new Attributes().maxLength(length);
    }
    public static Attributes maxLength(long min, long max, Long defaultValue){
        return new Attributes().maxLength(min,max,defaultValue);
    }
    public static Attributes maxPrecision(int precision){
        return new Attributes().precision(0, precision);
    }
    public static Attributes precision(int minPrecision, int maxPrecision){
        return new Attributes().precision(minPrecision, maxPrecision);
    }
    public static Attributes precision(int minPrecision, int maxPrecision, Integer defaultPrecision){
        return new Attributes().precision(minPrecision, maxPrecision, defaultPrecision);
    }
    public static Attributes maxScale(int scale){
        return new Attributes().scale(0, scale);
    }
    public static <T extends Comparable<T>> Attributes valueRange(T min, T max){
        return new Attributes().valueRange(Range.closed(min, max));
    }
    public static Attributes fixedBytes(int byteLength){
        return new Attributes().fixedBytes(byteLength);
    }
    public static Attributes maxBytes(long byteLength){
        return new Attributes().maxBytes(byteLength);
    }
    //endregion of 属性定义

    public static final long KB = 1024;
    public static final long MB = 1024 * KB;
    public static final long GB = 1024 * MB;
    public static final long TINY_SIZE   = 0xFFL; //2^8
    public static final long SHORT_SIZE  = 0xFFFFL;
    public static final long MEDIUM_SIZE = 0x00FF_FFFFL;
    public static final long LONG_SIZE   = 0xFFFF_FFFFL;

    public static class Attributes extends HashMap<DataTypeAttribute,Object> {
        public Attributes defineMaxLength(MetaRange<Long> maxLength){
            put(DataTypeAttribute.MAX_LENGTH, maxLength);
            return this;
        }
        public Attributes maxLength(long length){
            put(DataTypeAttribute.MAX_LENGTH, MetaRange.closed(1L, length));
            return this;
        }
        public Attributes maxLength(long minLength, long maxLength, Long defaultLen){
            put(DataTypeAttribute.MAX_LENGTH, MetaRange.closed(minLength, maxLength, defaultLen));
            return this;
        }
        public boolean hasMaxLength(){
            return containsKey(DataTypeAttribute.MAX_LENGTH);
        }
        public MetaRange<Long> getMaxLength(){
            if(containsKey(DataTypeAttribute.MAX_LENGTH)) {
                var value = get(DataTypeAttribute.MAX_LENGTH);
                return value instanceof MetaRange<?> ? (MetaRange<Long>)value : null;
            }
            return null;
        }

        public Attributes definePrecision(MetaRange<Integer> precision){
            put(DataTypeAttribute.PRECISION, precision);
            return this;
        }
        public Attributes precision(int min, int max, Integer defaultPrecision){
            put(DataTypeAttribute.PRECISION, MetaRange.closed(min, max, defaultPrecision));
            return this;
        }
        public Attributes precision(int min, int max){
            put(DataTypeAttribute.PRECISION, MetaRange.closed(min, max));
            return this;
        }
        public Integer getPrecision(){
            if(containsKey(DataTypeAttribute.PRECISION)) {
                var value = get(DataTypeAttribute.PRECISION);
                return value instanceof Integer p ? p : null;
            }
            return null;
        }
        public Attributes defineScale(MetaRange<Integer> scale){
            put(DataTypeAttribute.SCALE, scale);
            return this;
        }
        public Attributes scale(int min, int max, Integer defaultScale){
            put(DataTypeAttribute.SCALE, MetaRange.closed(min, max, defaultScale));
            return this;
        }
        public Attributes scale(int min, int max){
            put(DataTypeAttribute.SCALE, MetaRange.closed(min, max));
            return this;
        }
        public MetaRange<Integer> getScale(){
            if(containsKey(DataTypeAttribute.SCALE)) {
                var value = get(DataTypeAttribute.SCALE);
                return value instanceof MetaRange  s ? (MetaRange<Integer>)s : null;
            }
            return null;
        }
        public <T> Attributes typeHandler(JdbcTypeHandler<T> typeHandler){
            put(DataTypeAttribute.TYPE_HANDLER, typeHandler);
            return this;
        }
        public JdbcTypeHandler<?> getTypeHandler(){
            if(containsKey(DataTypeAttribute.TYPE_HANDLER)) {
                var value = get(DataTypeAttribute.TYPE_HANDLER);
                return value instanceof JdbcTypeHandler ? (JdbcTypeHandler<?>)value : null;
            }
            return null;
        }
        public Attributes valueRange(Range<?> range){
            put(DataTypeAttribute.VALUE_RANGE, range);
            return this;
        }
        public Attributes dateBetween(String from, String to){
            var fromDate = Date.valueOf(from);
            var toDate = Date.valueOf(to);
            put(DataTypeAttribute.VALUE_RANGE, Range.closed(fromDate, toDate));
            return this;
        }
        public Attributes timeBetween(String from, String to){
            var fromTime = Time.valueOf(from);
            var toTime = Time.valueOf(to);
            put(DataTypeAttribute.VALUE_RANGE, Range.closed(fromTime, toTime));
            return this;
        }
        public Attributes dateTimeBetween(String from, String to){
            var dtFrom  = Timestamp.valueOf(from);
            var dtTo  = Timestamp.valueOf(to);
            put(DataTypeAttribute.VALUE_RANGE, Range.closed(dtFrom, dtTo));
            return this;
        }
        public Range<?> valueRange(){
            if(containsKey(DataTypeAttribute.VALUE_RANGE)) {
                var value = get(DataTypeAttribute.VALUE_RANGE);
                return value instanceof Range<?> r ? r : null;
            }
            return null;
        }


        public Attributes fixedBits(long fixedBits){
            put(DataTypeAttribute.BYTE_RANGE, Range.singleton(fixedBits * 8L));
            return this;
        }
        public Attributes fixedBytes(long fixedBytes){
            put(DataTypeAttribute.BYTE_RANGE, Range.singleton(fixedBytes));
            return this;
        }
        public Attributes inBytes(long min, long max){
            put(DataTypeAttribute.BYTE_RANGE, Range.closed(min,max));
            return this;
        }
        public Attributes maxBytes(long max){
            put(DataTypeAttribute.BYTE_RANGE, Range.closed(1L,max));
            return this;
        }
        public Range<?> getByteRange(){
            if(containsKey(DataTypeAttribute.BYTE_RANGE)) {
                var value = get(DataTypeAttribute.BYTE_RANGE);
                return value instanceof Range<?> r ? r : null;
            }
            return null;
        }
        public DataTypeAttributeSet propertySet(){
            DataTypeAttributeSet p = DataTypeAttributeSet.noneOf();
            if(!this.isEmpty()) this.keySet().stream().forEach(k ->p.add(k));
            return p;
        }
    }

}
