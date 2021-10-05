package springboot.test.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
public class HeaderInfo {
    public String getUsername() {
        return Objects.toString(getTokenPayload("username"), "");
    }

    private Object getTokenPayload(String key) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) principal;
            return map.get(key);
        } else {
            return null;
        }
    }
}
