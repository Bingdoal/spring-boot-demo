package springboot.test.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import springboot.test.dto.I18nDto;

@Service
@Slf4j
public class I18nService {
    public I18nDto getMsg(String key) {
        return new I18nDto(key, LocaleContextHolder.getLocale());
    }
}
