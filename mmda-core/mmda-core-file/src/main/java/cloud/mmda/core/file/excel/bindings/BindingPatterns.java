package cloud.mmda.core.file.excel.bindings;

import java.util.regex.Pattern;

public abstract class BindingPatterns {
    public static final String FIRST_VARIABLE_STR = "#([a-zA-Z0-9_]+)\\W";
    public static final Pattern FIRST_VARIABLE = Pattern.compile(FIRST_VARIABLE_STR);

}
