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
import springboot.demo.middleware.exception.GraphQLStatusException;
import springboot.demo.service.JwtTokenService;

@Configuration
@Slf4j
public class AuthenticationConfig {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Bean
    public SchemaDirective isAuthenticatedDirective() {
        return new SchemaDirective("isAuthenticated", new SchemaDirectiveWiring() {
            @Override
            public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
                DataFetcher dataFetcher = DataFetcherFactories.wrapDataFetcher(env.getFieldDataFetcher(),
                        (dataFetchingEnvironment, value) -> {
                            GraphQLServletContext context = dataFetchingEnvironment.getContext();
                            try {
                                String token = context.getHttpServletRequest().getHeader("Authorization")
                                        .replaceFirst("Bearer ", "");
                                jwtTokenService.validateToken(token);
                            } catch (Exception e) {
                                throw new GraphQLStatusException(403, e.getMessage());
                            }
                            return value;
                        });
                return env.setFieldDataFetcher(dataFetcher);
            }
        });
    }
}
