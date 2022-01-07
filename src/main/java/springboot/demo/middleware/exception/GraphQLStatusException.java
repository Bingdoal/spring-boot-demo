package springboot.demo.middleware.exception;

import com.fasterxml.jackson.databind.JsonNode;
import springboot.demo.dto.basic.I18nDto;

public class GraphQLStatusException extends RuntimeStatusException {
    public GraphQLStatusException(int httpStatus, String message) {
        super(httpStatus, message);
    }

    public GraphQLStatusException(int httpStatus, I18nDto i18nDto) {
        super(httpStatus, i18nDto.toString());
    }

    public GraphQLStatusException(int httpStatus, JsonNode jsonNode) {
        super(httpStatus, jsonNode.toString());
    }
}
