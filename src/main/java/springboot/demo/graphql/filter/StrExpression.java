package springboot.demo.graphql.filter;

import lombok.Data;

@Data
public class StrExpression {
    private String eq;
    private String neq;
    private String contains;
    private String[] in;
    private Boolean isNull;
    private Boolean notNull;
}
