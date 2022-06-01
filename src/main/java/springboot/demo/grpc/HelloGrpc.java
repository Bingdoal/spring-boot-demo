package springboot.demo.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import springboot.demo.grpc.lib.HelloReply;
import springboot.demo.grpc.lib.HelloRequest;
import springboot.demo.grpc.lib.MyServiceGrpc;
import springboot.demo.middleware.exception.GrpcExceptionBase;
import springboot.demo.middleware.exception.GrpcRuntimeExceptionBase;

@Slf4j
@GrpcService
public class HelloGrpc extends MyServiceGrpc.MyServiceImplBase {
    @Override
    public void sayHello(HelloRequest request,
                         StreamObserver<HelloReply> responseObserver) {
        responseObserver.onNext(HelloReply.newBuilder()
                .setMessage("Hello " + request.getName())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void sayHelloOnError(HelloRequest request,
                                StreamObserver<HelloReply> responseObserver){
        responseObserver.onNext(HelloReply.newBuilder()
                .setMessage("Hello onError" + request.getName())
                .build());
        responseObserver.onError(new GrpcExceptionBase(-20,"Hello onError"));
    }

    @Override
    public void sayHelloThrowError(HelloRequest request,
                                StreamObserver<HelloReply> responseObserver){
        responseObserver.onNext(HelloReply.newBuilder()
                .setMessage("Hello throwError" + request.getName())
                .build());
        throw new GrpcRuntimeExceptionBase(-20,"Hello throwError");
    }
}
