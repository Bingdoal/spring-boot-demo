package springboot.demo.graphql.directive;

import graphql.kickstart.autoconfigure.tools.SchemaDirective;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetcherFactories;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UppercaseConfig {
    @Bean
    public SchemaDirective uppercaseDirective() {
        return new SchemaDirective("uppercase", new SchemaDirectiveWiring() {
            @Override
            public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
                DataFetcher dataFetcher = DataFetcherFactories.wrapDataFetcher(env.getFieldDataFetcher(),
                        (dataFetchingEnvironment, value) -> {
                            if (value == null) {
                                return null;
                            }
                            return ((String) value).toUpperCase();
                        });
                return env.setFieldDataFetcher(dataFetcher);
            }
        });
    }
}
