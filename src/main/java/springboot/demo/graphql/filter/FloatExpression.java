package springboot.demo.graphql.filter;

import lombok.Data;

@Data
public class FloatExpression {
    private Double eq;
    private Double neq;
    private Double[] in;
    private Double gt;
    private Double gte;
    private Double lt;
    private Double lte;
    private Boolean isNull;
    private Boolean notNull;
}
