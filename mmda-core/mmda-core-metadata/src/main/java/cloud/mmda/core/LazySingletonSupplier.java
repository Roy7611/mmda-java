package cloud.mmda.core;

import org.springframework.data.util.Lazy;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * 懒加载单例模式提供者实现了{@link Supplier}接口，
 * 在多线程环境下确保传入的Lambda函数只会调用一次，避免对象实例爆炸。
 * @param <T> 对象类型
 */
public final class LazySingletonSupplier<T> implements Supplier<T> {
    private final AtomicReference<T> ref = new AtomicReference<>();
    private final Supplier<T> delegate;
    public LazySingletonSupplier(Supplier<T> delegate) {
        Objects.requireNonNull(delegate);
        this.delegate = delegate;
    }
    @Override
    public T get() {
        var value = ref.get();
        if (value == null) {
            if(ref.compareAndSet(null, delegate.get())) {
                value = ref.get();
            }
        }
        return value;
    }
}
