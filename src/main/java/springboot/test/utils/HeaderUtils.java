package springboot.test.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class HeaderUtils {
    public static String getUsername() {
        return Objects.toString(getTokenPayload("username"), "");
    }

    private static Object getTokenPayload(String key) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) principal;
            return map.get(key);
        } else {
            return null;
        }
    }

    private static String getHeader(String key) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader(key);
    }
}
