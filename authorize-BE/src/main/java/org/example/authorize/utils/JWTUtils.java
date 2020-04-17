package org.example.authorize.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * JWT Utils.
 */
@Slf4j
public class JWTUtils {

    /**
     * Prevent new instance.
     */
    private JWTUtils() {
    }

    /**
     * Encode by JWT.
     *
     * @param subject    subject
     * @param claims     claims data
     * @param key        key to encode data
     * @param expiration expiration of token.
     * @return return JWT token
     */
    public static String encode(String subject, Map<String, Object> claims, Key key, Date expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .addClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .compact();
    }

    /**
     * Decode token JWT.
     *
     * @param token JWT token
     * @param key   key to decode token
     * @return return Claims object
     */
    public static Claims decode(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Verify token.
     *
     * @param token token need to verify
     * @param key   the key to validate token
     * @return return true if token is valid, otherwise don't trust this token
     */
    public static boolean verify(String token, Key key) {
        try {
            if (null != token) {
                Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                return true;
            }
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature trace: ", e);
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token trace: ", e);
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token trace: ", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT token compact of handler are invalid trace: ", e);
        }
        return false;
    }
}
