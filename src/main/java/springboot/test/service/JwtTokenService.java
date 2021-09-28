package springboot.test.service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.test.dto.JwtPayloadDto;
import springboot.test.dto.auth.TokenDto;
import springboot.test.utils.HeaderInfo;

import javax.security.auth.message.AuthException;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class JwtTokenService implements Serializable {

    @Autowired
    HeaderInfo headerInfo;

    private final long expiration = 30 * 60 * 1000;
    private final String secret = "test";

    public TokenDto generateToken(JwtPayloadDto payload) {
        Map<String, Object> claims = new HashMap<>() {{
            put("username", payload.getUsername());
        }};
        long expirationTime = Instant.now().toEpochMilli() + expiration;
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(expirationTime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return new TokenDto(token, expirationTime);
    }

    public String validateToken(String token) throws AuthException, SignatureException, ExpiredJwtException, IllegalArgumentException {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token);
            headerInfo.setUsername(Objects.toString(claimsJws.getBody().get("username")));
            return Objects.toString(claimsJws.getBody().get("username"));
        } catch (SignatureException | ExpiredJwtException | IllegalArgumentException e) {
            throw e;
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new AuthException("Invalid JWT token.");
        }
    }

}
