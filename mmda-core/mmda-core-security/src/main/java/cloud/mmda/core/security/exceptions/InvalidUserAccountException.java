package cloud.mmda.core.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class InvalidUserAccountException extends AuthenticationException {
    public InvalidUserAccountException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InvalidUserAccountException(String msg) {
        super(msg);
    }

}
