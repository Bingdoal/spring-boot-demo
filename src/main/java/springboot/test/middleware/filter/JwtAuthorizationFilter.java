package springboot.test.middleware.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import springboot.test.service.JwtTokenService;
import springboot.test.utils.SecurityUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private ObjectMapper objectMapper;

    private final String headerString = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        final String queryString = (req.getQueryString() == null) ? "" : "?" + req.getQueryString();
        log.info("\t[Request] {} {}{}", req.getMethod(), req.getRequestURI(), queryString);

        String header = req.getHeader(headerString);

        if (header == null || !header.startsWith(SecurityUtil.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        String token = req.getHeader(headerString);
        if (token != null) {
            try {
                Map<String, Object> payload = jwtTokenService.validateToken(token.replace(SecurityUtil.TOKEN_PREFIX, ""));
                log.info("[Token] payload: {}", objectMapper.valueToTree(payload).toString());
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(payload, null, null);
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (Exception e) {
                throw new BadCredentialsException(e.getMessage());
            }
        }
        try {
            chain.doFilter(req, res);
        } catch (final AccessDeniedException e) {
            throw e;
        }
    }
}
