package springboot.demo.utils.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.influx")
public class InfluxInfo {
    private String host;
    private int backupPort;
    private int port;
    private String url;
    private String user;
    private String password;
    private String database;
}
