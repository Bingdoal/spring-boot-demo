package springboot.demo.middleware.exception;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import springboot.demo.dto.basic.I18nDto;
import springboot.demo.middleware.exception.base.ExceptionBase;

@EqualsAndHashCode(callSuper = true)
public class RestException extends ExceptionBase {
    public RestException(int httpStatus, String message) {
        super(httpStatus, message);
    }

    public RestException(int httpStatus, I18nDto i18nDto) {
        super(httpStatus, i18nDto.toString());
    }

    public RestException(int httpStatus, JsonNode jsonNode) {
        super(httpStatus, jsonNode.toString());
    }
}
