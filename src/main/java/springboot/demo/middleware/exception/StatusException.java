package springboot.demo.middleware.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import springboot.demo.dto.basic.I18nDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatusException extends Exception {
    private int code;
    private JsonNode jsonNode;

    public StatusException(int httpStatus, String message) {
        super(message);
        this.code = httpStatus;
        this.jsonNode = new ObjectMapper().createObjectNode().put("message", message);
    }

    public StatusException(int httpStatus, I18nDto i18nDto) {
        super(i18nDto.toString());
        this.code = httpStatus;
        this.jsonNode = new ObjectMapper().createObjectNode().put("message", i18nDto.toString());
    }

    public StatusException(int httpStatus, JsonNode jsonNode) {
        super(jsonNode.toString());
        this.jsonNode = jsonNode;
        this.code = httpStatus;
    }
}
