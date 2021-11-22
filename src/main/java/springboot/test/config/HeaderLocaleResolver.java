package springboot.test.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Component
public class HeaderLocaleResolver extends AcceptHeaderLocaleResolver {
    private final String langHeader = "Accept-Language";

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        if (request.getHeader(langHeader) == null ||
                StringUtils.isBlank(request.getHeader(langHeader))) {
            return Locale.getDefault();
        }
        List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader(langHeader));
        return Locale.lookup(list, Arrays.asList(
                Locale.US,
                Locale.TAIWAN));
    }
}
