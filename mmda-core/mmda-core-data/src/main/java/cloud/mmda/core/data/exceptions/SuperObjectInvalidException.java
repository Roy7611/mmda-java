package cloud.mmda.core.data.exceptions;

import org.springframework.dao.DataAccessException;

/**
 * 超类对象不可用，导致扩展对象无法使用
 */
public class SuperObjectInvalidException extends DataAccessException {
    public SuperObjectInvalidException(String msg) {
        super(msg);
    }

    public SuperObjectInvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
