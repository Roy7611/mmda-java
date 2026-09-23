package cloud.mmda.core.file.excel.converters;

import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;
import java.util.Set;

public class StringToBooleanConverter implements Converter<String, Boolean> {
    private static final Set<String> trueValues = Set.of("true", "on", "yes", "1","是");
    private static final Set<String> falseValues = Set.of("false", "off", "no", "0","否");

    @Override
    public Boolean convert(String source) {
        String value = source.trim();
        if (value.isEmpty()) {
            return null;
        }
        value = value.toLowerCase();
        if (trueValues.contains(value)) {
            return Boolean.TRUE;
        }
        else if (falseValues.contains(value)) {
            return Boolean.FALSE;
        }
        else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }
}
