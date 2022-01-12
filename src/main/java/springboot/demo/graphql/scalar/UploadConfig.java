package springboot.demo.graphql.scalar;

import graphql.kickstart.servlet.apollo.ApolloScalars;
import graphql.schema.GraphQLScalarType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class UploadConfig {
    @Bean
    public GraphQLScalarType uploadScalar() {
        return ApolloScalars.Upload;
    }
}
