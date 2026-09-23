package cloud.mmda.core.lamda;

import java.io.Serializable;

/**
 * Lambda 实体属性获取器
 * @param <T> 实体类型
 */
@FunctionalInterface
public interface LambdaGetter<T, U> extends Serializable {
    /**
     * 返回实体属性
     * @param t 实体
     * @return 实体类的属性
     */
    U get(T t);
}