package cloud.mmda.core.enums;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * 枚举集抽象基类，内部数据为一个整形数，它的每个位代表一个独立枚举值。
 * <p>封装了{@link EnumSet}并实现了从整形数中取出位元构造枚举集的功能</p>
 * @param <E> 枚举类型并且实现了{@link EnumBitValue}接口
 */
public class EnumBitSet<E extends Enum<E> & EnumBitValue> implements Serializable {
    //枚举值，按位
    private int value;
    //枚举集合
    private EnumSet<E> enumSet;
    //枚举类型
    private final Class<E> enumClass;

    /**
     * 构造一个枚举集，不包含任何枚举值
     * @param enumClass
     */
    protected EnumBitSet(Class<E> enumClass){
        this.enumClass = enumClass;
        this.value = 0;
        this.enumSet = EnumSet.noneOf(enumClass);
    }
    protected EnumBitSet(Class<E> enumClass, int value){
        this.enumClass = enumClass;
        this.value = value;
        this.enumSet = setOf(enumClass, value);
    }
    protected EnumBitSet(Class<E> enumClass, EnumSet<E> enumSet){
        this.enumClass = enumClass;
        this.enumSet = enumSet;
        this.value = valueOf(enumSet);
    }

    public final Integer getValue() {
        return value;
    }

    public final String getText(){
        if(enumSet.isEmpty()) return "";
        return textOf(enumSet);
    }

    public final EnumSet<E> getEnumSet(){
        return enumSet;
    }

     /**
     * 是否包含枚举标记
     * @param e 枚举值
     * @return
     */
    public boolean hasFlag(E e){
        return (value & e.getValue()) != 0;
    }

    public void add(E e){
        enumSet.add(e);
        value |= e.getValue();
    }
    public final void addAll(E...elements){
        for(E e : elements){
            add(e);
        }
    }
    public void remove(E e){
        enumSet.remove(e);
        value &= ~e.getValue();
    }
    /**
     * 转为字符串，例如:NONE,ACTIVE,DEAD
     * @param delimiter 分隔符
     * @return
     */
    public String toString(String delimiter){
        return enumSet.stream().map(Enum::toString).collect(Collectors.joining(delimiter));
    }

    //region 静态函数
    public static <E extends Enum<E> & EnumBitValue> EnumBitSet<E> noneOf(Class<E> enumClass){
        return new EnumBitSet<>(enumClass);
    }
    public static <E extends Enum<E> & EnumBitValue> EnumBitSet<E> allOf(Class<E> enumClass){
        return new EnumBitSet<>(enumClass, EnumSet.allOf(enumClass));
    }
    public static <E extends Enum<E> & EnumBitValue> EnumBitSet<E> setOf(Class<E> enumClass, EnumSet<E> enumSet){
        return new EnumBitSet<>(enumClass, enumSet);
    }
    public static <E extends Enum<E> & EnumBitValue> EnumBitSet<E> valueOf(Class<E> enumClass, int value){
        return new EnumBitSet<>(enumClass, value);
    }

    //endregion of 静态函数

    //region converters between EnumSet and int
    static <E extends Enum<E> & EnumBitValue> EnumSet<E> setOf(Class<E> enumClass, int value){
        var enumSet = EnumSet.noneOf(enumClass);
        if(value>0) {
            for(var e : EnumSet.allOf(enumClass)){
                if(EnumBitValue.hasBit(value, e.getBit())) enumSet.add(e);
            }
        }
        return enumSet;
    }
    static <E extends Enum<E> & EnumBitValue> int valueOf(EnumSet<E> enumSet){
        return enumSet.stream()
                .map(e->EnumBitValue.bitOf(e.getValue(),e.getBit()))
                .reduce(0,(a,b)->a | b);
    }
    static <E extends Enum<E> & EnumBitValue> String textOf(EnumSet<E> enumSet){
        return enumSet.stream().map(EnumBitValue::getText)
                .collect(Collectors.joining(" "));
    }
    //endregion

    //region (de)serializer
    /**
     * 转换为整型数，用于Json序列化
     */
    public static class ToIntConverter extends StdConverter<EnumBitSet,Integer> {
        @Override
        public Integer convert(EnumBitSet value) {
            return value.getValue();
        }
    }
    /**
     * 从整型数构建枚举集合，用于Json反序列化
     */
    public static class FromIntConverter<E extends Enum<E> & EnumBitValue> extends StdConverter<Integer,EnumBitSet<E>>{
        private final Class<E> enumClass;
        public FromIntConverter(Class<E> enumClass){
            this.enumClass = enumClass;
        }
        @Override
        public EnumBitSet<E> convert(Integer value) {
            if(value == null || value == 0) return EnumBitSet.noneOf(enumClass);
            return EnumBitSet.valueOf(enumClass, value);
        }
    }
    //endregion
}
