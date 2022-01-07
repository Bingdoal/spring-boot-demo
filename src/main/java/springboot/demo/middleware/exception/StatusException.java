package springboot.demo.middleware.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import springboot.demo.dto.basic.I18nDto;

@EqualsAndHashCode(callSuper = true)
public class StatusException extends ExceptionBase {
    public StatusException(int httpStatus, String message) {
        super(httpStatus, message);
    }

    public StatusException(int httpStatus, I18nDto i18nDto) {
        super(httpStatus, i18nDto.toString());
    }

    public StatusException(int httpStatus, JsonNode jsonNode) {
        super(httpStatus, jsonNode.toString());
    }
}
