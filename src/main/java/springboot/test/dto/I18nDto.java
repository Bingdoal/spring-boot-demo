package springboot.test.dto;

import org.springframework.context.MessageSource;
import springboot.test.utils.StaticApplicationContext;

import java.util.*;

public class I18nDto {
    public I18nDto(String key, Locale locale) {
        this.key = key;
        this.locale = locale;
    }

    private String key;
    private List<String> args = new ArrayList<>();
    private Locale locale;

    public I18nDto args(Object... objects) {
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
