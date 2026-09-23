package cloud.mmda.core.data.conversion;

import cloud.mmda.core.enums.EnumBitSet;
import cloud.mmda.core.enums.EnumBitValue;

/**
 * 整形值与位元枚举集合{@link EnumBitSet}双向转换器
 * @param <E> 枚举类型
 */
public class IntegerEnumBitSetConverter<E extends Enum<E> & EnumBitValue> implements BiConverter<Integer, EnumBitSet<E>> {
    private final Class<E> enumClass;
    public IntegerEnumBitSetConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }
    @Override
    public Integer revert(EnumBitSet<E> target) {
        return target.getValue();
    }

    @Override
    public EnumBitSet<E> convert(Integer source) {
        if (source == null || source == 0) return EnumBitSet.noneOf(enumClass);
        return EnumBitSet.valueOf(enumClass, source);
    }
}
