package cloud.mmda.core.entities;

import cloud.mmda.core.lamda.LambdaGetter;
import cloud.mmda.core.lamda.LambdaUtil;
import org.jspecify.annotations.NonNull;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * 从实体{@code T}构建一个部分属性集合
 * <p>
 *     它实际相当于一个Lambda表达式：{@code ()->Map.of(K,V)}，用来构建一个实体对象的部分属性集合。
 *     当然你可以直接使用{@code Map.of}静态函数也很方便，但失去了编译期间检查功能。
 * </p>
 * <pre>
 *     {@code
 *     //假设我们有一个实体对象，来自客户端提交
 *     var partner = new Partner();
 *     //抽取部分属性（Builder模式）
 *     var properties = Partial.of(partner)
 *          .with(Partner::getPartnerCode)
 *          .with(Partner::getPartnerRoles) // _partnerRoles
 *          .with(...) //more
 *          .build(); // result() 返回 Map
 *     //你可以更新这个实体对象的这部分属性...
 *     factory.updatePartial(partner, properties);
 *
 *     //如果只有少量属性，可以直接
 *     var singlePart = Partial.of(partner, Partner::getPartnerCode);
 *     var parts = Partial.of(partner, Partner::getPartnerCode, Partner::getPartnerRoles);
 *
 *     //增加一个属性，注意非Builder模式需要提供实际值
 *     parts = parts.with(_categoryID, partner.getCategoryID()); // _categoryID 来自 Partner.Meta._categoryID
 *
 *     //增加多个属性，效率不高，建议使用Builder模式
 *     parts = parts.with(_countryCode. partner.getCountryCode())
 *          .with(_regionCode,partner.getRegionCode());
 *     }
 * </pre>
 * @param <T> 实体类型
 */
@FunctionalInterface
public interface Partial<T> extends Supplier<Map<String, Object>> {
    @Override
    Map<String, Object> get();

    /**
     * 链式添加单个属性，如果多次添加建议使用{@link #of(Object)}构建器，然后再{@code with}
     * @param property 属性名称
     * @param value 属性值
     * @return
     */
    default Partial<T> with(@NonNull final String property, Object value) {
        Map<String, Object> existing = get();
        Map<String, Object> newMap = new LinkedHashMap<>(existing);
        newMap.put(property, value);
        // 返回一个新的 Partial，保留原 Map 不可变
        return () -> newMap;
    }

    default <R> Partial<T> with(@NonNull final LambdaGetter<T,R> getter, R value) {
        return with(LambdaUtil.getFieldName(getter), value);
    }

    /**
     * 构建实体{@code t}多个{@code getters}属性集合，
     *
     * <p>
     *     如果你偏向使用属性字符串名称，可使用{@link Map#of(Object, Object)}。
     *     字段名称不要直接使用字符串字面量，建议使用实体的内部类{@code Meta}定义的常量，例如 {@code User.Meta._userID}
     * </p>
     * @param t 实体
     * @param getters 多个实体 getter 函数
     * @return 包含单个属性集合
     * @param <T> 实体类型
     */
    @SafeVarargs
    static <T> Partial<T> of(@NonNull final T t, @NonNull final LambdaGetter<T, ?>...getters) {
        return () -> new LinkedHashMap<>(){
            {
                for (LambdaGetter<T, ?> getter : getters) {
                    put(LambdaUtil.getFieldName(getter), getter.get(t));
                }
            }
        };
    }

    /**
     * 构建实体{@code T}类型的{@code getters}指定的属性集合
     * @param getters 多个实体 getter 函数
     * @return 属性名称集合生成函数
     * @param <T> 实体类型
     */
    @SafeVarargs
    static <T> Supplier<Set<String>> ofProperties(@NonNull final LambdaGetter<T, ?>...getters) {
        return () -> new LinkedHashSet<>(){
            {
                for (LambdaGetter<T, ?> getter : getters) {
                    add(LambdaUtil.getFieldName(getter));
                }
            }
        };
    }
    /**
     * 创建一个实体部分属性集合的构建器，
     * @param t 实体
     * @return 可链式添加属性的构建器
     * @param <T> 实体类型
     */
    static  <T> Builder<T> of(@NonNull final T t) {
        return new Builder<>(t);
    }

    /**
     * 部分实体属性集{@link Partial}构建器
     * @param <T> 实体类型
     */
    class Builder<T> {
        private final T t;
        private final Map<String, Object> properties;
        public Builder(@NonNull final T t) {
            this.t = t;
            properties = new LinkedHashMap<>();
        }

        public Builder<T> with(@NonNull final String property, Object value) {
            properties.put(property, value);
            return this;
        }

        public <U> Builder<T> with(@NonNull final LambdaGetter<T, U> getter) {
            properties.put(LambdaUtil.getFieldName(getter), getter.get(t));
            return this;
        }
        /**
         * 返回构建结果
         */
        public Partial<T> build() {
            return () -> properties;
        }

        /**
         * 直接返回构建结果集合
         */
        public Map<String, Object> result() {
            return properties;
        }
    }
}
