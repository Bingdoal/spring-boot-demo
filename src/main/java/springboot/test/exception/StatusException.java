package springboot.test.exception;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatusException extends Exception {
    private int code;
    private String message;
    private JsonNode jsonNode;

    public StatusException(int httpStatus, String message) {
        super(message);
        this.message = message;
        this.code = httpStatus;
    }

    public StatusException(int httpStatus, JsonNode jsonNode) {
        super(jsonNode.toString());
        this.jsonNode = jsonNode;
        this.code = httpStatus;
    }
}
