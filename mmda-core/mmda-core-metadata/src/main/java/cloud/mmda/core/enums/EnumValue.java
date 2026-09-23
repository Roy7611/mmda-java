package cloud.mmda.core.enums;

/**
 * 枚举值类型，用于实现枚举类实现继承值类型。
 * <p>
 *     例如：枚举Rank内部使用Byte数据存储。
 *     <pre>
 *         {@code public enum Rank implements EnumValue<Byte>{ ...}}
 *     </pre>
 * </p>
 * @param <T> Byte,Short,Integer
 */
public interface EnumValue<T extends Number> {
    /**
     * 返回枚举对应的整形值
     */
    T getValue();

}
