package springboot.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${swagger.enable}")
    private boolean swaggerEnable;
    @Value("${graphql.voyager.enabled}")
    private boolean voyagerEnable;
    @Value("${graphql.graphiql.enabled}")
    private boolean graphiqlEnable;

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .headers()
                .contentSecurityPolicy("script-src 'self'");

        if(voyagerEnable){
            httpSecurity.authorizeRequests()
                    .antMatchers("/voyager").permitAll()
                    .antMatchers("/vendor/voyager/**").permitAll();
        }

        if (graphiqlEnable) {
            httpSecurity.authorizeRequests()
                    .antMatchers("/vendor/graphiql/**").permitAll()
                    .antMatchers("/graphiql").permitAll()
                    .antMatchers("/graphql").permitAll();
        }

        if (swaggerEnable) {
            httpSecurity.authorizeRequests()
                    .antMatchers("/swagger-ui/*").permitAll()
                    .antMatchers("/swagger-resources/**").permitAll()
                    .antMatchers("/v3/api-docs").permitAll();
        }

//        httpSecurity
//                .csrf().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests()
//                .antMatchers("/v1/auth/login").permitAll()
//                .anyRequest().authenticated();

        httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .anyRequest().permitAll();


        httpSecurity
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
