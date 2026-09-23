package cloud.mmda.core.file.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Annotations {
    private static final Integer language = 0;

    private static final String MAX_LENGTH = "最大长度<=";
    private static final String MAX_LENGTH_ENGLISH = "max length<=";

    private static final String NUMERIC_PRECISION = "数字限制个数<=";
    private static final String NUMERIC_PRECISION_ENGLISH = "Number limit<=";

    private static final String NUMERIC_SCALE= "小数点后位数<=";
    private static final String NUMERIC_SCALE_ENGLISH = "Number of decimal places<=";

    public static String getMaxLengthAnnotations(Integer length) {
        return (language == 0 ?MAX_LENGTH:MAX_LENGTH_ENGLISH).concat(String.valueOf(length)).concat("\n");
    }

    public static String getNumericPrecisionAnnotations(Integer length) {
        return (language == 0 ?NUMERIC_PRECISION:NUMERIC_PRECISION_ENGLISH).concat(String.valueOf(length)).concat("\n");
    }

    public static String getNumericScaleAnnotations(Integer length) {
        return (language == 0 ?NUMERIC_SCALE:NUMERIC_SCALE_ENGLISH).concat(String.valueOf(length)).concat("\n");
    }

}
