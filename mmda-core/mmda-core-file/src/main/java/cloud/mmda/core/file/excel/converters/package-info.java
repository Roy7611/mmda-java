/**
 * 数据类型转换
 * <p>
 *     使用<a href="https://docs.spring.io/spring-framework/reference/core/validation/convert.html">Spring类型转换框架</a>
 *     中{@link org.springframework.core.convert.support.DefaultConversionService}加上自定义的一些转换器，
 *     实现Excel数据读写时的转换功能。函数canConvert判断是否能转换，函数convert执行转换。
 * </p>
 * <p>
 *      {@link org.springframework.core.convert.converter.Converter}单源转换器，
 *      {@link org.springframework.core.convert.converter.GenericConverter}支持多种源类型转换为目标类型。
 * </p>
 * <ul>
 *     <li>{@link org.springframework.core.convert.converter.Converter}单源转换器</li>
 *     <li>{@link org.springframework.core.convert.converter.GenericConverter}支持多种源类型转换为目标类型</li>
 *     <li>{@link org.springframework.core.convert.converter.ConditionalGenericConverter}条件多源转换器</li>
 * </ul>
 */
package cloud.mmda.core.file.excel.converters;