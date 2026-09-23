package cloud.mmda.core.services.exceptions;

import cloud.mmda.core.services.i18n.LocalizedMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 余额不足
 * @author roshion
 *
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class NotEnoughException extends DomainException {
	private static final long serialVersionUID = -3168137168507321270L;
	private static final String CODE = "not.enough";


	@Override
	public HttpStatus getStatus() {
		return HttpStatus.NOT_ACCEPTABLE;
	}

	public NotEnoughException(String message){
		super(message);
		this.code=CODE;
	}
	
	public NotEnoughException(String message,Throwable cause){
		super(message,cause);
		this.code=CODE;
	}

	public NotEnoughException(LocalizedMessage localizedMessage){
		super(localizedMessage);
	}

	public NotEnoughException(LocalizedMessage localizedMessage, Throwable cause){
		super(localizedMessage,cause);
	}
	public NotEnoughException(Throwable cause){
		super(cause);
		this.code=CODE;
	}
}
