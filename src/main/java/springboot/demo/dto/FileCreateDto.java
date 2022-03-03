package springboot.demo.dto;

import lombok.Data;

@Data
public class FileCreateDto {
    private String destination;
    private boolean success;
}
