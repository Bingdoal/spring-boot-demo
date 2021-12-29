package springboot.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import springboot.demo.middleware.filter.JwtAuthorizationFilter;

@Configuration
@EnableWebSecurity
public class JwtWebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthorizationFilter jwtAuthorizationFilter;

    private final String allowLocalAccess = "hasIpAddress('127.0.0.1') or hasIpAddress('::1')";

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers()
                .contentSecurityPolicy("script-src 'self'");
        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/v1/auth/login").permitAll()
                .antMatchers("/graphiql").permitAll()
                .antMatchers("/vendor/graphiql/**").permitAll()
                .antMatchers("/graphql").permitAll()
                .antMatchers("/playground").permitAll()
                .antMatchers("/vendor/playground/**").permitAll()
                // 只允許本地訪問 swagger
                // http://127.0.0.1:8080/swagger-ui/
                // http://127.0.0.1:8080/v3/api-docs
                .antMatchers("/swagger-ui/*").access(allowLocalAccess)
                .antMatchers("/swagger-resources/**").access(allowLocalAccess)
                .antMatchers("/v3/api-docs").access(allowLocalAccess)
                .anyRequest().authenticated();
        httpSecurity
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
