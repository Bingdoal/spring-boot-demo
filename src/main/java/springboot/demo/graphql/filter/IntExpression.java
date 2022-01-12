package springboot.demo.graphql.filter;

import lombok.Data;

@Data
public class IntExpression {
    private Long eq;
    private Long neq;
    private Long[] in;
    private Long gt;
    private Long gte;
    private Long lt;
    private Long lte;
    private Boolean isNull;
    private Boolean notNull;
}
