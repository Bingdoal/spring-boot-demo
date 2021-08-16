package springboot.test.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String queryString = (request.getQueryString() == null) ? "" : "?" + request.getQueryString();
        log.info("\t[Request] {} {} {}", request.getMethod(), request.getRequestURI(), queryString);

        filterChain.doFilter(request, response);
    }
}