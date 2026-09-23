package cloud.mmda.core.data.conversion;

/**
 * 转换器定义数据类型转换接口
 * @see BiConverter 双向转换接口
 * @param <S> 源数据类型
 * @param <T> 目的数据类型
 *
 */
@FunctionalInterface
public interface Converter<S,T> {
    /**
     * 将源类型值{@code source}转化为目的{@code T}类型
     */
    T convert(S source);
}
