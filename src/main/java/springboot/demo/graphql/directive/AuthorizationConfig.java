package springboot.demo.graphql.directive;

import graphql.kickstart.autoconfigure.tools.SchemaDirective;
import graphql.kickstart.servlet.context.GraphQLServletContext;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetcherFactories;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springboot.demo.middleware.exception.RuntimeStatusException;
import springboot.demo.service.JwtTokenService;

import javax.security.auth.message.AuthException;

@Configuration
@Slf4j
public class AuthorizationConfig {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Bean
    public SchemaDirective authorizationDirective() {
        return new SchemaDirective("isAuthorization", new SchemaDirectiveWiring() {
            @Override
            public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
                DataFetcher dataFetcher = DataFetcherFactories.wrapDataFetcher(env.getFieldDataFetcher(),
                        (dataFetchingEnvironment, value) -> {
                            GraphQLServletContext context = dataFetchingEnvironment.getContext();
                            String token = context.getHttpServletRequest().getHeader("Authorization").replaceFirst("Bearer ", "");
                            try {
                                jwtTokenService.validateToken(token);
                            } catch (AuthException e) {
                                throw new RuntimeStatusException(403, e.getMessage());
                            }
                            return value;
                        });
                return env.setFieldDataFetcher(dataFetcher);
            }
        });
    }
}
