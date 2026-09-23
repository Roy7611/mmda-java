package cloud.mmda.core.exceptions;

/**
 * 元数据异常是所有关于元数据的加载、解析、转换、序列化和反序列化等异常的根类
 */
public class MetadataException extends RuntimeException {
    public MetadataException(String message) {
        super(message);
    }
    public MetadataException(String message, Throwable cause) {
        super(message, cause);
    }
    public MetadataException(Throwable cause) {
        super(cause);
    }
}
