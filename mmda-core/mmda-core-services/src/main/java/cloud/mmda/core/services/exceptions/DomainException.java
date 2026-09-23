package cloud.mmda.core.services.exceptions;

import cloud.mmda.core.services.i18n.LocalizedMessage;
import org.springframework.http.HttpStatus;

/**
 * 业务异常
 */
public class DomainException extends RuntimeException {
	protected String code = "domain.error";
	protected Object[] args;

	public HttpStatus getStatus() {
		return HttpStatus.INTERNAL_SERVER_ERROR;
	}
	public String getCode(){ return code;}
	public Object[] getArgs(){return args;}
	public DomainException(){super();}

	public DomainException(String message){
		super(message);
		this.code = "domain.error";
	}
	public DomainException(String message, Throwable cause){
		super(message,cause);
		this.code = "domain.error";
	}

	public DomainException(LocalizedMessage localizedMessage){
		super(localizedMessage.getMessage());
		this.code=localizedMessage.getCode();
		this.args=localizedMessage.getArgs();
	}

	public DomainException(LocalizedMessage localizedMessage, Throwable cause){
		super(localizedMessage.getMessage(),cause);
		this.code=localizedMessage.getCode();
		this.args=localizedMessage.getArgs();
	}

	public DomainException(Throwable cause){
		super(cause);
		this.code = "domain.error";
	}
}
