package springboot.test.utils.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DatasourceInfo {
    private String url;
    private String username;
    private String password;
    @Value("${spring.jpa.properties.hibernate.default_schema}")
    private String schema;
    @Value("${spring.flyway.baseline-version}")
    private String migrationVersion;
}
