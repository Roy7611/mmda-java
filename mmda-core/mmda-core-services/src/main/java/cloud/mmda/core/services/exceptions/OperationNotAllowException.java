package cloud.mmda.core.services.exceptions;

import cloud.mmda.core.services.i18n.LocalizedMessage;
import org.springframework.http.HttpStatus;

/**
 * 操作不允许
 * @author roshion
 *
 */
public class OperationNotAllowException extends DomainException {
	private static final long serialVersionUID = -1818022674585791372L;
	public static final String CODE = "operation.not.allow";

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.NOT_ACCEPTABLE;
	}
	public OperationNotAllowException(String message){
		super(message);
		this.code=CODE;
	}

	public OperationNotAllowException(String message,Throwable cause){
		super(message,cause);
		this.code=CODE;
	}

	public OperationNotAllowException(LocalizedMessage localizedMessage){
		super(localizedMessage);
	}

	public OperationNotAllowException(LocalizedMessage localizedMessage, Throwable cause){
		super(localizedMessage,cause);
	}
	
	public OperationNotAllowException(Throwable cause){
		super(cause);
		this.code=CODE;
	}
}
