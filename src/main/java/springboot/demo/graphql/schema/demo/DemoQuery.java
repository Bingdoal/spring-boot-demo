package springboot.demo.graphql.schema.demo;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springboot.demo.middleware.exception.GraphQLStatusException;
import springboot.demo.middleware.exception.StatusException;

@Component
@Slf4j
public class DemoQuery implements GraphQLQueryResolver {
    public String hello() {
        return "world";
    }

    public String runtimeException() {
        if (true) {
            throw new GraphQLStatusException(400, "GraphQLStatusException test.");
        }
        return "world";
    }

    public String exception() throws StatusException {
        if (true) {
            throw new StatusException(400, "StatusException test.");
        }
        return "world";
    }
}
