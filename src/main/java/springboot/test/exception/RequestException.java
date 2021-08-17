package springboot.test.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RequestException extends Exception {
    private int code;
    private String message;

    public RequestException(int httpStatus, String message) {
        super(message);
        this.message = message;
        this.code = httpStatus;
    }
}
