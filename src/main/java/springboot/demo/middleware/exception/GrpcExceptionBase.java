package springboot.demo.middleware.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GrpcExceptionBase extends Exception {
    public GrpcExceptionBase(int code, String message) {
        super(message);
        this.code = code;
    }

    private int code;
}
