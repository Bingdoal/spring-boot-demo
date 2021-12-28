package springboot.demo.dto.basic;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiLogDto {
    private LocalDateTime logTime = LocalDateTime.now();
    private String url;
    private String method;
    private int status;
    private JsonNode requestBody;
    private JsonNode responseBody;
    private String operation;
}
