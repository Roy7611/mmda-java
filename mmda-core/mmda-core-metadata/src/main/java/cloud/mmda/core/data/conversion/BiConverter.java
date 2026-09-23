package cloud.mmda.core.data.conversion;

/**
 * 双向数据类型转换器扩展了{@link Converter}，定义源和目的类型的双向转换接口
 * @param <S> 源类型
 * @param <T> 目的类型
 */
public interface BiConverter<S,T> extends Converter<S,T> {
    /**
     * 从目的类型恢复为源类型
     * @param target 目的类型值
     * @return 源类型值
     */
    S revert(T target);
}
