package cloud.mmda.core.services.exceptions;

import cloud.mmda.core.services.i18n.LocalizedMessage;
import org.springframework.http.HttpStatus;

/**
 * 没有记录被影响
 * @author roshion
 *
 */
public class NoRecordsAffectedException extends DomainException {
	public static final String CODE = "no.records.affected";
	private static final long serialVersionUID = -2700349429670714440L;


	@Override
	public HttpStatus getStatus() {
		return HttpStatus.NOT_FOUND;
	}

	public NoRecordsAffectedException(String msg) {
		super(msg);
		this.code=CODE;
	}
	public NoRecordsAffectedException(String msg, Throwable cause) {
		super(msg, cause);
		this.code=CODE;
	}

	public NoRecordsAffectedException(LocalizedMessage localizedMessage){
		super(localizedMessage);
	}

	public NoRecordsAffectedException(LocalizedMessage localizedMessage,Throwable cause){
		super(localizedMessage,cause);
	}

}
