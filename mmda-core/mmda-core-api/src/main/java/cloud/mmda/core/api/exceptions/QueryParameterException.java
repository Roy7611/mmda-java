package cloud.mmda.core.api.exceptions;

import cloud.mmda.core.services.exceptions.DomainException;
import cloud.mmda.core.services.i18n.LocalizedMessage;
import org.springframework.http.HttpStatus;

/**
 * 请求参数非法
 * @author roshion
 *
 */
public class QueryParameterException extends DomainException {
	public static final String CODE = "query.parameter";
	public static final String CODE_REQUIRED = "query.parameter.required";
	public static final String CODE_INVALID = "query.parameter.invalid";

	@Override
	public HttpStatus getStatus() {
		return HttpStatus.NOT_ACCEPTABLE;
	}

	public QueryParameterException(String message){
		super(message);
		this.code = CODE;
	}


	public QueryParameterException(String message, Throwable cause){
		super(message,cause);
		this.code = CODE;
	}

	public QueryParameterException(LocalizedMessage localizedMessage){
		super(localizedMessage);
	}

	public QueryParameterException(LocalizedMessage localizedMessage, Throwable cause){
		super(localizedMessage,cause);
	}

	public QueryParameterException(Throwable cause){
		super(cause);
		this.code = CODE;
	}
}
