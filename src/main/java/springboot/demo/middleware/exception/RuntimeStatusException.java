package springboot.demo.middleware.exception;

import com.fasterxml.jackson.databind.JsonNode;
import springboot.demo.dto.basic.I18nDto;
import springboot.demo.middleware.exception.base.RuntimeExceptionBase;

public class RuntimeStatusException extends RuntimeExceptionBase {
    public RuntimeStatusException(int httpStatus, String message) {
        super(httpStatus, message);
    }

    public RuntimeStatusException(int httpStatus, I18nDto i18nDto) {
        super(httpStatus, i18nDto.toString());
    }

    public RuntimeStatusException(int httpStatus, JsonNode jsonNode) {
        super(httpStatus, jsonNode.toString());
    }
}
