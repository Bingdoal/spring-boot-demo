package springboot.test.service;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import springboot.test.dto.auth.JwtPayloadDto;
import springboot.test.dto.auth.TokenDto;
import springboot.test.utils.SecurityUtils;

import javax.security.auth.message.AuthException;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtTokenService implements Serializable {

    public TokenDto generateToken(JwtPayloadDto payload) {
        Map<String, Object> claims = new HashMap<>() {{
            put("username", payload.getUsername());
        }};
        long expirationTime = Instant.now().toEpochMilli() + SecurityUtils.EXPIRATION;
        Date expireDate = new Date(expirationTime);
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SecurityUtils.SECRET)
                .compact();
        log.debug("User: {}, Expired at: {}", payload.getUsername(), expireDate);
        return new TokenDto(token, expireDate.getTime());
    }

    public Map<String, Object> validateToken(String token) throws AuthException, SignatureException, ExpiredJwtException, IllegalArgumentException {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SecurityUtils.SECRET)
                    .parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (SignatureException | ExpiredJwtException | IllegalArgumentException e) {
            throw e;
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new AuthException("Invalid JWT token.");
        }
    }

}
