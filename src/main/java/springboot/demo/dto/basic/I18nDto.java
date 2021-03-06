package springboot.demo.dto.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import springboot.demo.utils.ApplicationContextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Slf4j
public class I18nDto {
    private I18nDto(String key) {
        this.key = key;
        this.locale = LocaleContextHolder.getLocale();
    }

    private String key;
    private List<String> args = new ArrayList<>();
    private Locale locale;

    public static I18nDto key(String key) {
        return new I18nDto(key);
    }

    public I18nDto args(Object... objects) {
        for (Object object : objects) {
            args.add(Objects.toString(object));
        }
        return this;
    }

    @Override
    public String toString() {
        try {
            MessageSource messageSource = ApplicationContextUtils.getBean(MessageSource.class);
            return messageSource.getMessage(key, args.toArray(), locale);
        } catch (Exception ex) {
            log.warn("I18N toString: {}", ex.getMessage(), ex);
            return key;
        }
    }
}
