package cloud.mmda.core.services.exceptions;

import cloud.mmda.core.entities.ValidationError;
import cloud.mmda.core.services.i18n.LocalizedMessage;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * 数据非法
 * @author roshion
 *
 */
public class DataInvalidException extends DomainException {

	private static final long serialVersionUID = 1735211207040446067L;
	public static final String CODE = "data.invalid";


	@Getter
	private List<ValidationError> validationErrors;

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.NOT_ACCEPTABLE;
	}

	public DataInvalidException(String message){
		super(message);
		this.code = CODE;
	}

	
	public DataInvalidException(String message,Throwable cause){
		super(message,cause);
		this.code = CODE;
	}

	public DataInvalidException(LocalizedMessage localizedMessage){
		super(localizedMessage);
	}

	public DataInvalidException(LocalizedMessage localizedMessage, Throwable cause){
		super(localizedMessage,cause);
	}
	public DataInvalidException(LocalizedMessage localizedMessage,List<ValidationError> validationErrors, Throwable cause){
		super(localizedMessage,cause);
		this.validationErrors=validationErrors;
	}

	public DataInvalidException(Throwable cause){
		super(cause);
		this.code = CODE;
	}
}
