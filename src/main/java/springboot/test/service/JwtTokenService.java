package springboot.test.service;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import springboot.test.dto.JwtPayloadDto;
import springboot.test.dto.TokenDto;
import springboot.test.utils.SecurityUtil;

import javax.security.auth.message.AuthException;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService implements Serializable {

    public TokenDto generateToken(JwtPayloadDto payload) {
        Map<String, Object> claims = new HashMap<>() {{
            put("username", payload.getUsername());
        }};
        long expirationTime = Instant.now().toEpochMilli() + SecurityUtil.EXPIRATION;
        Date expireDate = new Date(expirationTime);
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, SecurityUtil.SECRET)
                .compact();
        return new TokenDto(token, expireDate.getTime());
    }

    public Map<String, Object> validateToken(String token) throws AuthException, SignatureException, ExpiredJwtException, IllegalArgumentException {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SecurityUtil.SECRET)
                    .parseClaimsJws(token);
            return claimsJws.getBody();
        } catch (SignatureException | ExpiredJwtException | IllegalArgumentException e) {
            throw e;
        } catch (MalformedJwtException | UnsupportedJwtException e) {
            throw new AuthException("Invalid JWT token.");
        }
    }

}
