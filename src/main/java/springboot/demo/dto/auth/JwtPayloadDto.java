package springboot.demo.dto.auth;

import lombok.Data;

@Data
public class JwtPayloadDto {
    public JwtPayloadDto(String username) {
        this.username = username;
    }

    private String username;
}
