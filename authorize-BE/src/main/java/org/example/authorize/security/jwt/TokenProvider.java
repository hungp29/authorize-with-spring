package org.example.authorize.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.authorize.config.prop.JwtProperties;
import org.example.authorize.exception.InvalidTokenException;
import org.example.authorize.exception.KeyNotFoundException;
import org.example.authorize.exception.TokenExpirationException;
import org.example.authorize.security.UserPrincipal;
import org.example.authorize.security.authentoken.JWTAuthenticationToken;
import org.example.authorize.utils.JWTUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Token Provider class. This class use to create, read access and refresh tokens.
 */
@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    public static final String C_AUTHORITIES_KEY = "auth";
    public static final String C_USERNAME_KEY = "username";

    private final JwtProperties jwtProperties;

    private Key key;
    private boolean reuseRefreshToken = true;
    private long tokenValidityInMilliseconds;
    private long refreshTokenValidityInMilliseconds;
    private String name;

    private final TokenStore tokenStore;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes;
        String secret = jwtProperties.getSigningKey();
        if (!StringUtils.isEmpty(secret)) {
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            key = Keys.hmacShaKeyFor(keyBytes);
        } else {
            throw new KeyNotFoundException("JWT secret key not found");
        }

        this.tokenValidityInMilliseconds = jwtProperties.getAccessTokenValiditySeconds() * 1000;
        this.refreshTokenValidityInMilliseconds = jwtProperties.getRefreshTokenValiditySeconds() * 1000;
        this.name = jwtProperties.getName();
    }

    /**
     * Creates token.
     *
     * @param authentication Authentication instance
     * @param validity       expiration time
     * @return the token
     */
    private String createToken(Authentication authentication, Date validity) {
        List<String> authorities = authentication.getAuthorities().stream().
                map(GrantedAuthority::getAuthority).
                collect(Collectors.toList());

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        // Prepare claims
        Map<String, Object> claims = new HashMap<>();
        claims.put(C_USERNAME_KEY, principal.getUsername());
        claims.put(C_AUTHORITIES_KEY, authorities);

        return JWTUtils.encode(name, claims, key, validity);
    }

    /**
     * Get Authentication from access token value.
     *
     * @param token the access token
     * @return Authentication instance.
     */
    public Authentication getAuthentication(String token) {
        Claims claims = JWTUtils.decode(token, key);

        List<?> lstAuth = claims.get(C_AUTHORITIES_KEY, List.class);
        String username = claims.get(C_USERNAME_KEY, String.class);

        Collection<? extends GrantedAuthority> authorities = lstAuth.stream()
                .map(String.class::cast)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        User principal = new User(username, "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * Validation access token.
     *
     * @param accessTokenValue the access token value.
     * @return true or false
     */
    public boolean validateToken(String accessTokenValue) {
        AccessToken accessToken = readAccessToken(accessTokenValue);
        return null != accessToken && JWTUtils.verify(accessToken.getToken(), key);
    }

    /**
     * Create access token and store in redis.
     *
     * @param authentication authentication instance
     * @return AccessToken instance
     */
    public AccessToken createAccessToken(Authentication authentication, boolean rememberMe) {
        AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
        RefreshToken refreshToken = null;
        if (null != existingAccessToken) {
            if (existingAccessToken.isExpired()) {
                if (null != existingAccessToken.getRefreshToken()) {
                    refreshToken = existingAccessToken.getRefreshToken();
                    // The token store could remove the refresh token when the
                    // access token is removed, but we want to
                    // be sure...
                    tokenStore.removeRefreshToken(refreshToken);
                }
                tokenStore.removeAccessToken(existingAccessToken);
            } else {
                // Re-store the access token in case the authentication has changed
                tokenStore.storeAccessToken(existingAccessToken, authentication);
                return existingAccessToken;
            }
        }

        if (null == refreshToken) {
            refreshToken = createRefreshToken(authentication, rememberMe);
        } else {
            if (System.currentTimeMillis() > refreshToken.getExpiration().getTime()) {
                refreshToken = createRefreshToken(authentication, rememberMe);
            }
        }

        AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        // In case it was modified
        refreshToken = accessToken.getRefreshToken();
        if (null != refreshToken) {
            tokenStore.storeRefreshToken(refreshToken, authentication);
        }
        return accessToken;
    }

    /**
     * Refresh the access token by refresh token value.
     *
     * @param refreshTokenValue the refresh token value
     * @return AccessToken instance
     */
    public AccessToken refreshAccessToken(String refreshTokenValue) {
        RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
        if (null == refreshToken) {
            throw new InvalidTokenException("Invalid refresh token: " + refreshTokenValue);
        }

        Authentication authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
        if (null != authenticationManagerBuilder) {
            // The client has already been authenticated, but the user authentication might be old now, so give it a
            // chance to re-authenticate.
            Authentication user = new JWTAuthenticationToken(authentication.getName(), "", authentication);
            user = authenticationManagerBuilder.getObject().authenticate(user);
            authentication = user;
        }

        // clear out any access tokens already associated with the refresh
        // token.
        tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);

        if (isExpired(refreshToken)) {
            tokenStore.removeRefreshToken(refreshToken);
            throw new TokenExpirationException("Refresh token is expired: " + refreshToken);
        }

        if (!reuseRefreshToken) {
            tokenStore.removeRefreshToken(refreshToken);
            refreshToken = createRefreshToken(authentication, true);
        }

        AccessToken accessToken = createAccessToken(authentication, refreshToken);
        tokenStore.storeAccessToken(accessToken, authentication);
        if (!reuseRefreshToken) {
            tokenStore.storeRefreshToken(accessToken.getRefreshToken(), authentication);
        }
        return accessToken;
    }

    /**
     * Check refresh token is expiration.
     *
     * @param refreshToken the refresh token
     * @return true or false
     */
    protected boolean isExpired(RefreshToken refreshToken) {
        return null == refreshToken || null == refreshToken.getExpiration()
                || System.currentTimeMillis() > refreshToken.getExpiration().getTime();
    }

    /**
     * Get AccessToken instance by Authentication instance.
     *
     * @param authentication the Authentication instance
     * @return AccessToken instance
     */
    public AccessToken getAccessToken(Authentication authentication) {
        return tokenStore.getAccessToken(authentication);
    }

    /**
     * Reads access token by it's value.
     *
     * @param accessToken the access token value
     * @return AccessToken value
     */
    public AccessToken readAccessToken(String accessToken) {
        return tokenStore.readAccessToken(accessToken);
    }

    /**
     * Load Authentication by access token value.
     *
     * @param accessTokenValue the access token value
     * @return Authentication instance
     */
    public Authentication loadAuthentication(String accessTokenValue) {
        AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
        if (null == accessToken) {
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        } else if (accessToken.isExpired()) {
            tokenStore.removeAccessToken(accessToken);
            throw new TokenExpirationException("Access token expired: " + accessTokenValue);
        }

        Authentication result = tokenStore.readAuthentication(accessToken);
        if (null == result) {
            // in case of race condition
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        }
        return result;
    }

    /**
     * Revoke access token.
     *
     * @param tokenValue the access token
     * @return true or false
     */
    public boolean revokeToken(String tokenValue) {
        AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
        if (null == accessToken) {
            return false;
        }
        if (null != accessToken.getRefreshToken()) {
            tokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        tokenStore.removeAccessToken(accessToken);
        return true;
    }

    /**
     * Creates new RefreshToken instance.
     *
     * @param authentication the Authentication instance
     * @return new RefreshToken instance
     */
    private RefreshToken createRefreshToken(Authentication authentication, boolean rememberMe) {
        Date expiration;
        if (!rememberMe) {
            if (tokenValidityInMilliseconds > 0) {
                expiration = new Date(System.currentTimeMillis() + tokenValidityInMilliseconds);
            } else {
                expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
            }
        } else {
            if (refreshTokenValidityInMilliseconds > 0) {
                expiration = new Date(System.currentTimeMillis() + refreshTokenValidityInMilliseconds);
            } else {
                expiration = new Date(System.currentTimeMillis() + 7200 * 1000);
            }
        }
        return new DefaultRefreshToken(createToken(authentication, expiration), expiration);
    }

    /**
     * Creates new AccessToken instance.
     *
     * @param authentication the Authentication instance
     * @param refreshToken   the RefreshToken instance
     * @return new AccessToken instance
     */
    private AccessToken createAccessToken(Authentication authentication, RefreshToken refreshToken) {
        Date expiration;
        if (tokenValidityInMilliseconds > 0) {
            expiration = new Date(System.currentTimeMillis() + tokenValidityInMilliseconds);
        } else {
            expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        }

        DefaultAccessToken token = new DefaultAccessToken(createToken(authentication, expiration), expiration);
        token.setRefreshToken(refreshToken);

        return token;
    }
}
