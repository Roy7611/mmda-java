package cloud.mmda.core.data.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;

public class TenancyDataAccessException extends DataAccessException {
    public TenancyDataAccessException(String msg) {
        super(msg);
    }
    public TenancyDataAccessException(@Nullable String msg, @Nullable Throwable cause) {
        super(msg, cause);
    }
}
