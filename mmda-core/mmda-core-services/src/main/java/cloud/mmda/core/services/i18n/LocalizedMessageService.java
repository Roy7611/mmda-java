package cloud.mmda.core.services.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化消息服务
 *
 * @author roshion
 * @since 2020-04-04
 * @version 3.0.0
 *
 */
@Component
public class LocalizedMessageService {
    private final MessageSource messageSource;
    public LocalizedMessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code, Object...args){
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Locale locale, Object...args){
        return messageSource.getMessage(code, args, locale);
    }

    public LocalizedMessage getLocalizedMessage(String code, Object...args){
        String message = messageSource.getMessage(code,args, LocaleContextHolder.getLocale());
        return new LocalizedMessage(message,code,args);
    }
    public LocalizedMessage getLocalizedMessage(String code, Locale locale, Object...args){
        String message = messageSource.getMessage(code,args,locale);
        return new LocalizedMessage(message,code,args);
    }
}
