package springboot.demo.middleware.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class XSSFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest req,
                                 HttpServletResponse res,
                                 FilterChain chain) throws IOException, ServletException {
        XSSWrapper wrappedRequest = new XSSWrapper(req);
        String body = wrappedRequest.getBody();
        final String queryString = (req.getQueryString() == null) ? "" : "?" + req.getQueryString();
        log.info("\t[Request] {} {}{}", req.getMethod(), req.getRequestURI(), queryString);
        if (log.isDebugEnabled() &&
                (req.getMethod().equalsIgnoreCase("POST") ||
                        req.getMethod().equalsIgnoreCase("PUT"))) {
            log.debug("\t[Request] Body: {}", body);
        }
        if (!StringUtils.isBlank(body)) {
            body = XSSWrapper.stripXSS(body);
            wrappedRequest.resetInputStream(body.getBytes());
        }
        chain.doFilter(wrappedRequest, res);
    }
}