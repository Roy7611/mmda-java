package cloud.mmda.core.exceptions;

/**
 * 元枚举解析异常在解析枚举字符串时遇到非法的语法抛出
 */
public class MetaEnumParseException extends MetadataException {
    public MetaEnumParseException(String message) {
        super(message);
    }

    public MetaEnumParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetaEnumParseException(Throwable cause) {
        super(cause);
    }
}
