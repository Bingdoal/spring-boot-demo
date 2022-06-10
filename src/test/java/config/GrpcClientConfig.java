package config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import springboot.demo.grpc.lib.MyServiceGrpc;

@TestConfiguration
public class GrpcClientConfig {

    @Bean
    public MyServiceGrpc.MyServiceBlockingStub serviceStub(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        MyServiceGrpc.MyServiceBlockingStub stub = MyServiceGrpc.newBlockingStub(channel);
        return stub;
    }
}
