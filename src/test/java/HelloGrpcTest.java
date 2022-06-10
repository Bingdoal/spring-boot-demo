import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.rpc.ErrorInfo;
import config.GrpcClientConfig;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import springboot.demo.grpc.lib.HelloRequest;
import springboot.demo.grpc.lib.MyServiceGrpc;

@Slf4j
@Import(GrpcClientConfig.class)
@SpringBootTest(classes = {ObjectMapper.class})
public class HelloGrpcTest {
    @Autowired
    private MyServiceGrpc.MyServiceBlockingStub stub;

    @Test
    public void testSayHello() {
        HelloRequest request = HelloRequest.newBuilder()
                .setName("Test")
                .build();
        var result = stub.sayHello(request);
        log.info("message: {}", result.getMessage());
    }


    @Test
    public void testSayHelloOnError() throws InvalidProtocolBufferException {
        HelloRequest request = HelloRequest.newBuilder()
                .setName("Test")
                .build();
        try {
            var result = stub.sayHelloOnError(request);
        } catch (StatusRuntimeException ex) {
            com.google.rpc.Status status = io.grpc.protobuf.StatusProto.fromThrowable(ex);
            for (Any any : status.getDetailsList()) {
                if (!any.is(ErrorInfo.class)) {
                    continue;
                }
                ErrorInfo errorInfo = any.unpack(ErrorInfo.class);
                String code = errorInfo.getMetadataMap().get("code");
                String msg = errorInfo.getMetadataMap().get("msg");
                log.info("{}: {}", code, msg);
            }
        }
    }

    @Test
    public void testSayHelloThrowError() throws InvalidProtocolBufferException {
        HelloRequest request = HelloRequest.newBuilder()
                .setName("Test")
                .build();
        try {
            var result = stub.sayHelloThrowError(request);
        } catch (StatusRuntimeException ex) {
            com.google.rpc.Status status = io.grpc.protobuf.StatusProto.fromThrowable(ex);
            for (Any any : status.getDetailsList()) {
                if (!any.is(ErrorInfo.class)) {
                    continue;
                }
                ErrorInfo errorInfo = any.unpack(ErrorInfo.class);
                String code = errorInfo.getMetadataMap().get("code");
                String msg = errorInfo.getMetadataMap().get("msg");
                log.info("{}: {}", code, msg);
            }
        }
    }
}
