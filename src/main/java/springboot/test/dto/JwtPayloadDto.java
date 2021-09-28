package springboot.test.dto;

import lombok.Data;

@Data
public class JwtPayloadDto {
    public JwtPayloadDto(String username) {
        this.username = username;
    }

    private String username;
}
