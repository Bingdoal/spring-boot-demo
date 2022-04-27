package springboot.demo.utils.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.flyway")
public class FlywayInfo {
    private String url;
    private String user;
    private String password;
    private String locations;
    private String target;
    private String baselineVersion;
    private boolean baselineOnMigrate;
    private boolean validateOnMigrate;
    private boolean outOfOrder;
    private String defaultSchema;
}
