import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.demo.dto.auth.JwtPayloadDto;
import springboot.demo.dto.auth.TokenDto;
import springboot.demo.service.JwtTokenService;

import javax.security.auth.message.AuthException;


@SpringBootTest(classes = JwtTokenService.class)
public class JwtTokenServiceTest {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    public void testInvalidJwt() {
        Assertions.assertThrows(AuthException.class, () -> {
            jwtTokenService.validateToken("test");
        });
    }

    @Test
    public void testWrongSignature() {
        Assertions.assertThrows(SignatureException.class, () -> {
            jwtTokenService.validateToken("eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MzkyMDQwMjYsInVzZXJuYW1lIjoib2FtLXJlcG9ydGVyIn0.Rx1rDcfZjxh55eemcvYMA1-E0Tt4V5PCH8LyZUsEaTNMIsHVO4CGQPh9WwHYk3xkcOJnMfyzOc0Mk6Kl9h3USg");
        });
    }

    @Test
    public void testExpiredToken() {
        Assertions.assertThrows(ExpiredJwtException.class, () -> {
            jwtTokenService.validateToken("eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjEsInVzZXJuYW1lIjoib2FtLXJlcG9ydGVyIn0._RJl8H4dOWpjYoOz1qyr_qnqSExFB0i7FqA8LAJwFSfvwhE4DS6YD7zXgpCqoZqROzaZryCJ2xF34ARMbPZtFw");
        });
    }

    @Test
    public void testGenerateAndValidToken() throws AuthException {
        String username = "test";
        JwtPayloadDto payloadDto = new JwtPayloadDto(username);
        TokenDto tokenDto = jwtTokenService.generateToken(payloadDto);
        Assertions.assertDoesNotThrow(() -> {
            jwtTokenService.validateToken(tokenDto.getToken());
        });
        Assertions.assertEquals(jwtTokenService.validateToken(tokenDto.getToken()).get("username"), username);
    }
}
