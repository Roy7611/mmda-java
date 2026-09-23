package cloud.mmda.core.web;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;

import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

/**
 * 语言区域上下文解决器
 * 每个请求都从参数?lang=zh-CN解析，或从Http头Accept-Language解析
 */
public class WebLocaleContextResolver extends AcceptHeaderLocaleContextResolver {
    public WebLocaleContextResolver(){
        super();
        setDefaultLocale(WebLocaleResolver.DEFAULT_LOCALE);
        setSupportedLocales(WebLocaleResolver.SUPPORTED_LOCALES);
        //设置默认为中文和东八时区
        LocaleContextHolder.setDefaultLocale(WebLocaleResolver.DEFAULT_LOCALE);
        LocaleContextHolder.setDefaultTimeZone(TimeZone.getTimeZone("GMT+8:00"));
    }
    @Override
    public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        LocaleContext localeContext=resolveLocaleContextFromQueryParam(exchange);
        if(localeContext==null){
            localeContext = super.resolveLocaleContext(exchange);
        }
        //LocaleContextHolder与Thread关联，不是每个请求关联，无法直接用于Service层
        //异常消息由GlobalErrorAttributes根据请求上下文重新本地化
        //但是非异常的消息组装必须由Controller传入Service层调用
        //LocaleContextHolder.setLocaleContext(localeContext);

        return localeContext;
    }

    @Override
    public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
        localeContext = resolveLocaleContextFromQueryParam(exchange);
    }

    @Nullable
    private LocaleContext resolveLocaleContextFromQueryParam(ServerWebExchange exchange) {
        String lang = exchange.getRequest().getQueryParams().getFirst("lang");
        Optional<Locale> targetLocale = WebLocaleResolver.resolveLocaleFromString(lang);
        if(!targetLocale.isPresent()) return null;
        return new SimpleLocaleContext(targetLocale.get());
    }
}
