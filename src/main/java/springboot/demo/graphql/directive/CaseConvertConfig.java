package springboot.demo.graphql.directive;

import graphql.kickstart.autoconfigure.tools.SchemaDirective;
import graphql.language.StringValue;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetcherFactories;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CaseConvertConfig {
    @Bean
    public SchemaDirective caseConvertDirective() {
        final String directiveName = "caseConvert";
        return new SchemaDirective(directiveName, new SchemaDirectiveWiring() {
            @Override
            public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
                GraphQLDirective directive = env.getDirective(directiveName);
                DataFetcher dataFetcher = DataFetcherFactories.wrapDataFetcher(env.getFieldDataFetcher(),
                        (dataFetchingEnvironment, value) -> {
                            if (value == null) {
                                return null;
                            }
                            StringValue stringValue = ((StringValue) directive.getArgument("convert")
                                    .getArgumentValue().getValue());
                            if (stringValue == null) {
                                return null;
                            }

                            String convert = stringValue.getValue();
                            switch (convert) {
                                case "LOWER":
                                    return ((String) value).toLowerCase();
                                case "UPPER":
                                    return ((String) value).toUpperCase();
                            }
                            return null;
                        });
                return env.setFieldDataFetcher(dataFetcher);
            }
        });
    }
}
