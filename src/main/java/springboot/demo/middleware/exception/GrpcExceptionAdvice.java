package springboot.demo.middleware.exception;

import com.google.protobuf.Any;
import com.google.rpc.Code;
import com.google.rpc.ErrorInfo;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.protobuf.StatusProto;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

import java.util.Objects;

@Slf4j
@GrpcAdvice
public class GrpcExceptionAdvice {

    @GrpcExceptionHandler(RuntimeException.class)
    public StatusException handleRuntimeException(RuntimeException ex) {
        log.error("RuntimeException: {}", ex.getMessage(), ex);
        return getStatusException(null, ex.getMessage());
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public Status handleInvalidArgument(IllegalArgumentException e) {
        return Status.INVALID_ARGUMENT.withDescription("input parameter error.").withCause(e);
    }

    @GrpcExceptionHandler(GrpcExceptionBase.class)
    public StatusException handleGrpcExceptionBase(GrpcExceptionBase ex) {
        log.error("GrpcExceptionBase: {}", ex.getMessage(), ex);
        return getStatusException(ex.getCode(), ex.getMessage());
    }

    @GrpcExceptionHandler(GrpcRuntimeExceptionBase.class)
    public StatusException handleGrpcRuntimeExceptionBase(GrpcRuntimeExceptionBase ex) {
        log.error("GrpcRuntimeExceptionBase: {}", ex.getMessage(), ex);
        return getStatusException(ex.getCode(), ex.getMessage());
    }

    private StatusException getStatusException(Integer codeInt, String message) {
        Code code = Code.forNumber(codeInt);
        if (code == null) code = Code.UNKNOWN;
        var status = com.google.rpc.Status.newBuilder()
                .setCode(code.getNumber())
                .setMessage(message)
                .addDetails(Any.pack(ErrorInfo.newBuilder()
                        .putMetadata("code", Objects.toString(codeInt))
                        .putMetadata("msg", message)
                        .build()))
                .build();
        return StatusProto.toStatusException(status);
    }
}
