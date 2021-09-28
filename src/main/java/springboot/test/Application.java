package springboot.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springboot.test.utils.ServerInfo;

@SpringBootApplication()
@Slf4j
public class Application {

    public static void main(String[] args) {
        final ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        ServerInfo serverInfo = applicationContext.getBean(ServerInfo.class);
        log.info("\t============= " + serverInfo.getName() + ":" + serverInfo.getVersion() + " start on " + serverInfo.getPort() + " =============");
    }
}
