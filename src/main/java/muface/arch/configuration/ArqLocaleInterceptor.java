package muface.arch.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class ArqLocaleInterceptor implements HandlerInterceptor {

    public static final String EUSKERA = "eu";
    public static final String GALLEGO = "gl";
    public static final String CATALAN = "ca";
    public static final String CASTELLANO = "es";
    public static final String ENGLISH = "en";
    public final static Map<String, Locale> mapLocales = new HashMap<>();

    private Locale getLocale(String idioma) {
        if (mapLocales.isEmpty()) {
            mapLocales.put(EUSKERA, new Locale("eu", "ES"));
            mapLocales.put(GALLEGO, new Locale("gl", "ES"));
            mapLocales.put(CATALAN, new Locale("ca", "ES"));
            mapLocales.put(CASTELLANO, new Locale("es"));
            mapLocales.put(ENGLISH, new Locale("en"));
        }
        Locale locale = mapLocales.get(idioma);
        return locale == null ? Locale.getDefault() : locale;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String lang = request.getHeader("Accept-Language");
        if (lang != null && !lang.isEmpty()) {
            Locale locale = getLocale(lang);
            LocaleContextHolder.setLocale(locale);
        }
        return true;
    }

}
