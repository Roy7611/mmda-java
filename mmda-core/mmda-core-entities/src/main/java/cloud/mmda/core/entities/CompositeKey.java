package cloud.mmda.core.entities;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 组合键接口
 */
public interface CompositeKey {
    String KEY_DELIMITER = ",";

    default String join(Object...values) {
        return Arrays.stream(values).map(Object::toString)
                .collect(Collectors.joining(KEY_DELIMITER));
    }
    static String[] split(final String keyString) {
        return keyString.split(KEY_DELIMITER);
    }
}
