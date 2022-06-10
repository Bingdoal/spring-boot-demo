package springboot.demo.middleware.listener;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.event.GrpcServerStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrpcEventListener {
    @EventListener
    public void onServerStarted(GrpcServerStartedEvent event) {
        log.info("Grpc Server start on: {}:{}", event.getAddress(), event.getPort());
    }
}
