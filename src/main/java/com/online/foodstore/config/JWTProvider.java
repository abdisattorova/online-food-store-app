package com.online.foodstore.config;

import com.online.foodstore.exception.UnauthorizedException;
import com.online.foodstore.utils.ErrorCodes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import static com.online.foodstore.utils.ErrorMessages.JWT_EXPIRED;

@Service
public class JWTProvider {
    public static String secretKey = "g7GFm3DrP8g+k5J+2aECqA6Olq/Z4KnyTZo2eozlJaOe0boB/i5eaOFuMbeI5a9my2crbDN+oZBrfuBfva3oOw==";
    static long expireTime = 36_000_000;

    public static String generate(String username, Map<String, Object> claims) {
        Date expireDate = new Date(System.currentTimeMillis() + expireTime);

        return Jwts.builder().setIssuedAt(new Date()).setExpiration(expireDate).setSubject(username).addClaims(claims).signWith(getSigningKey()).compact();
    }

    public static boolean isValid(String token) {
        return getUsername(token) != null && isNotExpired(token);
    }

    public static String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public static boolean isNotExpired(String token) {
        return getClaims(token).getExpiration().after(new Date(System.currentTimeMillis()));
    }

    public static Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new UnauthorizedException(JWT_EXPIRED, ErrorCodes.ERROR);
        }
    }

    private static Key getSigningKey() {
        var decoded = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(decoded);
    }
}
