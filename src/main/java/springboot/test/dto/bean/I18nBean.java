package springboot.test.dto.bean;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import springboot.test.utils.StaticApplicationContext;

import java.util.*;

public class I18nBean {
    private I18nBean(String key) {
        this.key = key;
        this.locale = LocaleContextHolder.getLocale();
    }

    private String key;
    private List<String> args = new ArrayList<>();
    private Locale locale;

    public static I18nBean key(String key){
        return new I18nBean(key);
    }

    public I18nBean args(Object... objects) {
        for (Object object : objects) {
            args.add(Objects.toString(object));
        }
        return this;
    }

    @Override
    public String toString() {
        MessageSource messageSource = StaticApplicationContext.getBean(MessageSource.class);
        return messageSource.getMessage(key, args.toArray(), locale);
    }
}
