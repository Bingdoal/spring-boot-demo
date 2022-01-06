package springboot.demo.graphql.schema.demo;

import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DemoMutation implements GraphQLMutationResolver {

    public LocalDateTime addOneDay(LocalDateTime dateTime) {
        return dateTime.plusDays(1);
    }
}
