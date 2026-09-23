package cloud.mmda.core.web;

import cloud.mmda.core.utils.BaseUtil;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.*;

public abstract class WebLocaleResolver {
    public static final Locale DEFAULT_LOCALE = Locale.CHINA;
    public static final List<Locale> SUPPORTED_LOCALES = Arrays.asList(
            Locale.SIMPLIFIED_CHINESE,
            Locale.US,
            Locale.TRADITIONAL_CHINESE
    );

    public static Optional<Locale> resolveLocaleFromString(String lang) {
        if(lang==null || lang.isEmpty()) return Optional.empty();

        if(lang.startsWith("en")) lang="en-US";
        if(lang.startsWith("zh")) {
           if(lang.startsWith("zh-TW") || lang.startsWith("zh-HK") ||
                   lang.startsWith("zh-SG") || lang.startsWith("zh-MO") ||
                   lang.startsWith("zh-Hant") ||
                   lang.startsWith("zh-CHT")){
               if(lang.indexOf("Hant")==-1) lang = "zh-Hant"+lang.substring(2);
               Locale locale = Locale.forLanguageTag(lang);
               return Optional.of(locale);
           }
           return Optional.of(Locale.SIMPLIFIED_CHINESE);
        }

        //if("zh-TW".equals(lang)) lang="zh-HK";
        Locale localeResolved = Locale.forLanguageTag(lang);
        if(localeResolved!=null){
            if(SUPPORTED_LOCALES.contains(localeResolved)) return Optional.of(localeResolved);
        }
        return Optional.empty();
    }

    public static String getSupportLangFromLocale(final Locale locale){
        String lang = locale.getLanguage();
        if("en".equals(lang)) return lang;
        else if("zh".equals(lang)){
            if(locale.equals(Locale.TRADITIONAL_CHINESE)) return "zh-Hant";
            String script = locale.getScript();
            if(BaseUtil.hasText(script) && "Hant".equals(script)) return "zh-Hant";
        }
        return "zh";
    }
    @Nullable
    public static Locale resolveSupportedLocale(@Nullable List<Locale> requestLocales) {
        if (CollectionUtils.isEmpty(requestLocales)) {
            return DEFAULT_LOCALE;
        } else {
            if (SUPPORTED_LOCALES.isEmpty()) {
                return requestLocales.get(0);
            } else {
                Locale languageMatch = null;
                Iterator var4 = requestLocales.iterator();

                Locale locale;
                label55:
                do {
                    while(var4.hasNext()) {
                        locale = (Locale)var4.next();
                        if (SUPPORTED_LOCALES.contains(locale)) {
                            continue label55;
                        }

                        if (languageMatch == null) {
                            Iterator var6 = SUPPORTED_LOCALES.iterator();

                            while(var6.hasNext()) {
                                Locale candidate = (Locale)var6.next();
                                if (!StringUtils.hasLength(candidate.getCountry()) && candidate.getLanguage().equals(locale.getLanguage())) {
                                    languageMatch = candidate;
                                    break;
                                }
                            }
                        }
                    }

                    if (languageMatch != null) {
                        return languageMatch;
                    }

                    return DEFAULT_LOCALE;
                } while(languageMatch != null && !languageMatch.getLanguage().equals(locale.getLanguage()));

                return locale;
            }
        }
    }

    public static Locale resolveLocaleFromRequest(ServerRequest request){
        //从参数lang解析
        Optional<String> lang = request.queryParam("lang");
        if(lang.isPresent()){
            Optional<Locale> locale = resolveLocaleFromString(lang.get());
            if(locale.isPresent()) return locale.get();
        }

        //若不成功，从http头解析
        List<Locale> requestLocales = request.headers().asHttpHeaders().getAcceptLanguageAsLocales();
        return resolveSupportedLocale(requestLocales);
    }
}
