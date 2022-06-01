package springboot.demo.graphql.operation.demo;

import graphql.kickstart.servlet.context.GraphQLServletContext;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springboot.demo.middleware.exception.GraphQLStatusException;
import springboot.demo.middleware.exception.RuntimeStatusException;
import springboot.demo.middleware.exception.RestException;

@Component
@Slf4j
public class DemoQuery implements GraphQLQueryResolver {
    public String hello(DataFetchingEnvironment env) {
        GraphQLServletContext context = env.getContext();
        String token = context.getHttpServletRequest().getHeader("Authorization");
        log.info("Authorization: {}", token);
        return "world";
    }

    public String graphQLStatusException() {
        throw new GraphQLStatusException(400, "GraphQLStatusException test.");
    }

    public String runtimeException() {
        throw new RuntimeStatusException(400, "RuntimeStatusException test.");
    }

    public String exception() throws RestException {
        throw new RestException(400, "StatusException test.");
    }
}
