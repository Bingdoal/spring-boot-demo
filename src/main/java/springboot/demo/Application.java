package springboot.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import springboot.demo.utils.properties.ServerInfo;

@SpringBootApplication()
@Slf4j
public class Application {

    public static void main(String[] args) {
        final ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        ServerInfo serverInfo = applicationContext.getBean(ServerInfo.class);
        log.info("\t============= " + serverInfo.getName() + ":" + serverInfo.getVersion() + " 程式啟動 on " + serverInfo.getPort() + " =============");
    }
}