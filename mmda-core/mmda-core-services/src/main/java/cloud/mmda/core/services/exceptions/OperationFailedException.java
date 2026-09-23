package cloud.mmda.core.services.exceptions;

import cloud.mmda.core.services.i18n.LocalizedMessage;
import org.springframework.http.HttpStatus;

/**
 * 操作失败
 * @author roshion
 *
 */
public class OperationFailedException extends DomainException {
	private static final long serialVersionUID = 2752124224875163817L;
	public static final String CODE = "operation.failed";

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.BAD_REQUEST;
	}
	public OperationFailedException(String message){
		super(message);
		this.code=CODE;
	}
	
	public OperationFailedException(String message,Throwable cause){
		super(message,cause);
		this.code=CODE;
	}
	public OperationFailedException(LocalizedMessage localizedMessage){
		super(localizedMessage);
	}

	public OperationFailedException(LocalizedMessage localizedMessage,Throwable cause){
		super(localizedMessage,cause);
	}
	public OperationFailedException(Throwable cause){
		super(cause);
		this.code=CODE;
	}
}
