import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springboot.test.dto.auth.JwtPayloadDto;
import springboot.test.dto.auth.TokenDto;
import springboot.test.service.JwtTokenService;

import javax.security.auth.message.AuthException;


@SpringBootTest(classes = JwtTokenService.class)
public class JwtTokenServiceTest {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    public void testGenerateAndValidJwt() throws AuthException {
        Assertions.assertThrows(AuthException.class, () -> {
            jwtTokenService.validateToken("test");
        });

        Assertions.assertThrows(SignatureException.class, () -> {
            jwtTokenService.validateToken("eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MzkyMDQwMjYsInVzZXJuYW1lIjoib2FtLXJlcG9ydGVyIn0.Rx1rDcfZjxh55eemcvYMA1-E0Tt4V5PCH8LyZUsEaTNMIsHVO4CGQPh9WwHYk3xkcOJnMfyzOc0Mk6Kl9h3USg");
        });

        Assertions.assertThrows(ExpiredJwtException.class, () -> {
            jwtTokenService.validateToken("eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjEsInVzZXJuYW1lIjoib2FtLXJlcG9ydGVyIn0._RJl8H4dOWpjYoOz1qyr_qnqSExFB0i7FqA8LAJwFSfvwhE4DS6YD7zXgpCqoZqROzaZryCJ2xF34ARMbPZtFw");
        });

        JwtPayloadDto payloadDto = new JwtPayloadDto("test");
        TokenDto tokenDto = jwtTokenService.generateToken(payloadDto);
        Assertions.assertDoesNotThrow(() -> {
            jwtTokenService.validateToken(tokenDto.getToken());
        });
        Assertions.assertEquals(jwtTokenService.validateToken(tokenDto.getToken()).get("username"), "test");
    }
}
