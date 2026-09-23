package cloud.mmda.core.services.exceptions;

import cloud.mmda.core.services.i18n.LocalizedMessage;
import org.springframework.http.HttpStatus;

public class NotFoundException extends DomainException {
	public static final String CODE = "not.found";
	@Override
	public HttpStatus getStatus() {
		return HttpStatus.NOT_FOUND;
	}
	public NotFoundException(String message){
		super(message);
		this.code=CODE;
	}

	public NotFoundException(String message,Throwable cause){
		super(message,cause);
		this.code=CODE;
	}

	public NotFoundException(LocalizedMessage localizedMessage){
		super(localizedMessage);
	}

	public NotFoundException(LocalizedMessage localizedMessage, Throwable cause){
		super(localizedMessage,cause);
	}
	public NotFoundException(Throwable cause){
		super(cause);
		this.code=CODE;
	}
}
