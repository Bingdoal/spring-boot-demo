package springboot.test.middleware.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import springboot.test.service.JwtTokenService;
import springboot.test.utils.SecurityUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;
    @Autowired
    private ObjectMapper objectMapper;

    private final String headerString = "Authorization";
    private List<String> excludeUrlPatterns = new ArrayList<>() {{
        add("/v1/auth/login");
    }};
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) throws ServletException {
        return excludeUrlPatterns.stream()
                .anyMatch(p -> pathMatcher.match(p, req.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws ServletException, IOException {
        log.debug("Check authorization...");
        String header = req.getHeader(headerString);

        if (header == null || !header.startsWith(SecurityUtils.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        String token = req.getHeader(headerString);
        if (token != null) {
            try {
                Map<String, Object> payload = jwtTokenService.validateToken(token.replace(SecurityUtils.TOKEN_PREFIX, ""));
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
