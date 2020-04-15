package org.example.authorize.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.authorize.config.prop.JwtProperties;
import org.example.authorize.exception.TokenKeyInvalidException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Token Provider class. This class use to create, read access and refresh tokens.
 */
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";

    private final JwtProperties jwtProperties;

    private Key key;
    private boolean reuseRefreshToken = true;
    private long tokenValidityInMilliseconds;
    private long refreshTokenValidityInMilliseconds;

    @PostConstruct
    public void initProperties() {
        byte[] keyBytes;
        String secret = jwtProperties.getSigningKey();
        if (!StringUtils.isEmpty(secret)) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            key = Keys.hmacShaKeyFor(keyBytes);
        } else {
            throw new TokenKeyInvalidException("JWT secret key not found");
        }

        this.tokenValidityInMilliseconds = jwtProperties.getAccessTokenValiditySeconds() * 1000;
        this.refreshTokenValidityInMilliseconds = jwtProperties.getRefreshTokenValiditySeconds() * 1000;
    }

    /**
     * Get Authentication from access token value.
     *
     * @param token the access token
     * @return Authentication instance.
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).
                        map(SimpleGrantedAuthority::new).
                        collect(Collectors.toList());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
