package springboot.demo.graphql.operation.post;

import lombok.Data;
import springboot.demo.graphql.filter.DatetimeExpression;
import springboot.demo.graphql.filter.IntExpression;
import springboot.demo.graphql.filter.StrExpression;

@Data
public class PostFilter {
    private IntExpression id;
    private StrExpression content;
    private DatetimeExpression creationTime;
}
