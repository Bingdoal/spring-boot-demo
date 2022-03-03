package springboot.demo.utils.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.file-server.http")
public class HttpFileServerInfo {
    private String url;
    private String host;
    private int port;
    private String user;
    private String password;
}
