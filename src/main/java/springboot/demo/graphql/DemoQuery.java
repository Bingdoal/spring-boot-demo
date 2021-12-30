package springboot.demo.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DemoQuery implements GraphQLQueryResolver {
    public String hello() {
        return "world";
    }
}
