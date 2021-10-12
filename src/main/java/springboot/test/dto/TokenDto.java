package springboot.test.dto;

import lombok.Data;

@Data
public class TokenDto {
    public TokenDto(String token, long expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }

    private String token;
    private Long expirationTime;
}
