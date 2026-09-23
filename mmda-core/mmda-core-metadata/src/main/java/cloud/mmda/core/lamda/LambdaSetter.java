package cloud.mmda.core.lamda;

import java.io.Serializable;

@FunctionalInterface
public interface LambdaSetter<T, U> extends Serializable {
    void set(T t, U u);
}
