import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springboot.test.dto.auth.JwtPayloadDto;
import springboot.test.dto.auth.TokenDto;
import springboot.test.service.JwtTokenService;

import javax.security.auth.message.AuthException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JwtTokenService.class)
public class JwtTokenServiceTest {
    @Autowired
    private JwtTokenService jwtTokenService;

    @Test
    public void testGenerateAndValidJwt() throws AuthException {
        JwtPayloadDto payloadDto = new JwtPayloadDto("test");
        TokenDto tokenDto = jwtTokenService.generateToken(payloadDto);

        Assertions.assertThrows(AuthException.class, () -> {
            jwtTokenService.validateToken("test");
        });

        Assertions.assertDoesNotThrow(() -> {
            jwtTokenService.validateToken(tokenDto.getToken());
        });

        Assert.assertEquals(jwtTokenService.validateToken(tokenDto.getToken()).get("username"), "test");
    }
}
