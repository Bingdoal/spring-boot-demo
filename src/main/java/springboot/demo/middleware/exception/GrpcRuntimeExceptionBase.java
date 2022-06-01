package springboot.demo.middleware.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class GrpcRuntimeExceptionBase extends RuntimeException {
    public GrpcRuntimeExceptionBase(int code, String message) {
        super(message);
        this.code = code;
    }

    private int code;
}
