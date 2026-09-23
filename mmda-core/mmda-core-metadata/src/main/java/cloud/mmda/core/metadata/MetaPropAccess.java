package cloud.mmda.core.metadata;

/**
 * 元属性访问定义{@code getter}和{@code setter}名称和属性，
 * 在构建元对象访问{@link MetaObjectAccess}时创建，访问存取器，
 * 实现通过元列{@link MetaCol}读写一个实体对象的属性
 * @param getterName 属性 getter 方法名
 * @param getterIndex 属性 getter 方法索引
 * @param setterName 属性 setter 方法名
 * @param setterIndex 属性 setter 方法索引
 */
public record MetaPropAccess(String getterName, int getterIndex, String setterName, int setterIndex) {
}
