package springboot.demo.middleware.exception.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import springboot.demo.dto.basic.I18nDto;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class RuntimeExceptionBase extends RuntimeException {
    private int code;
    private JsonNode jsonNode;

    public RuntimeExceptionBase(int httpStatus, String message) {
        super(message);
        this.code = httpStatus;
        this.jsonNode = new ObjectMapper().createObjectNode().put("message", message);
    }

    public RuntimeExceptionBase(int httpStatus, I18nDto i18nDto) {
        super(i18nDto.toString());
        this.code = httpStatus;
        this.jsonNode = new ObjectMapper().createObjectNode().put("message", i18nDto.toString());
    }

    public RuntimeExceptionBase(int httpStatus, JsonNode jsonNode) {
        super(jsonNode.toString());
        this.jsonNode = jsonNode;
        this.code = httpStatus;
    }
}
