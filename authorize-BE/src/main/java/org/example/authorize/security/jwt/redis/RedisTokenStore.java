package org.example.authorize.security.jwt.redis;

import lombok.RequiredArgsConstructor;
import org.example.authorize.config.prop.JwtProperties;
import org.example.authorize.security.jwt.AccessToken;
import org.example.authorize.security.jwt.AuthenticationKeyGenerator;
import org.example.authorize.security.jwt.RefreshToken;
import org.example.authorize.security.jwt.TokenStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Using this class to store tokens in redis.
 */
@Component
@RequiredArgsConstructor
public class RedisTokenStore implements TokenStore, InitializingBean {

    private static final String ACCESS = "access:";
    private static final String AUTH_TO_ACCESS = "auth_to_access:";
    private static final String AUTH = "auth:";
    private static final String REFRESH_AUTH = "refresh_auth:";
    private static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    private static final String REFRESH = "refresh:";
    private static final String REFRESH_TO_ACCESS = "refresh_to_access:";
    private static final String UNAME_TO_ACCESS = "uname_to_access:";

    private String prefix = "";
    private AuthenticationKeyGenerator authenticationKeyGenerator = new AuthenticationKeyGenerator();
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtProperties jwtProperties;

    /**
     * Get username from Authentication instance.
     *
     * @param authentication Authentication instance
     * @return username
     */
    private static String getUsername(Authentication authentication) {
        return null == authentication ? "" : authentication.getName();
    }

    /**
     * Add prefix value before the key.
     *
     * @param key the key redis
     * @return the key have prefix
     */
    private String redisKey(String key) {
        return prefix + key;
    }

    /**
     * Convert object to class corresponding.
     *
     * @param obj   the object to convert
     * @param clazz class need to convert
     * @param <T>   class
     * @return instance of T class
     */
    private <T> T convertClass(Object obj, Class<T> clazz) {
        if (clazz.isInstance(obj)) {
            return clazz.cast(obj);
        }
        return null;
    }

    /**
     * Read Authentication object from redis by access token object.
     *
     * @param accessToken the access token
     * @return return Authentication object if it's stored by access token determine
     */
    @Override
    public Authentication readAuthentication(AccessToken accessToken) {
        return readAuthentication(accessToken.getValue());
    }

    /**
     * Read Authentication object from redis by access token value.
     *
     * @param token the access token value
     * @return return Authentication object if it's store by access token value determine
     */
    @Override
    public Authentication readAuthentication(String token) {
        return convertClass(redisTemplate.opsForValue().get(redisKey(AUTH + token)), Authentication.class);
    }

    /**
     * Store Authentication, Access Token, Refresh Token objects to redis and set expire time if it's exist.
     *
     * @param token          the access token
     * @param authentication the authentication
     */
    @Override
    public void storeAccessToken(AccessToken token, Authentication authentication) {
        String accessKey = redisKey(ACCESS + token.getValue());
        String authKey = redisKey(AUTH + token.getValue());
        String authToAccessKey = redisKey(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication));
        String usernameKey = redisKey(UNAME_TO_ACCESS + getUsername(authentication));

        redisTemplate.opsForValue().set(accessKey, token);
        redisTemplate.opsForValue().set(authKey, authentication);
        redisTemplate.opsForValue().set(authToAccessKey, token);
        redisTemplate.opsForSet().add(usernameKey, token);

        if (null != token.getExpiration()) {
            int seconds = token.getExpiresIn();
            redisTemplate.expire(accessKey, seconds, TimeUnit.SECONDS);
            redisTemplate.expire(authKey, seconds, TimeUnit.SECONDS);
            redisTemplate.expire(authToAccessKey, seconds, TimeUnit.SECONDS);
            redisTemplate.expire(usernameKey, seconds, TimeUnit.SECONDS);
        }
        RefreshToken refreshToken = token.getRefreshToken();
        if (null != refreshToken && null != refreshToken.getValue()) {
            String refreshToAccessKey = redisKey(REFRESH_TO_ACCESS + token.getRefreshToken().getValue());
            String accessToRefreshKey = redisKey(ACCESS_TO_REFRESH + token.getValue());

            redisTemplate.opsForValue().set(refreshToAccessKey, token.getValue());
            redisTemplate.opsForValue().set(accessToRefreshKey, token.getRefreshToken().getValue());
            if (null != refreshToken.getExpiration()) {
                int seconds = Long.valueOf((refreshToken.getExpiration().getTime() - System.currentTimeMillis()) / 1000L)
                        .intValue();
                redisTemplate.expire(refreshToAccessKey, seconds, TimeUnit.SECONDS);
                redisTemplate.expire(accessToRefreshKey, seconds, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * Read Access Token from redis by token value.
     *
     * @param token the access token value
     * @return return Access Token
     */
    @Override
    public AccessToken readAccessToken(String token) {
        return convertClass(redisTemplate.opsForValue().get(redisKey(ACCESS + token)), AccessToken.class);
    }

    /**
     * Remove Access Token from redis.
     *
     * @param accessToken the access token need to remove
     */
    @Override
    public void removeAccessToken(AccessToken accessToken) {
        removeAccessToken(accessToken.getValue());
    }

    /**
     * Removes access token base on token value.
     *
     * @param tokenValue the access token
     */
    public void removeAccessToken(String tokenValue) {
        String accessKey = redisKey(ACCESS + tokenValue);
        String authKey = redisKey(AUTH + tokenValue);
        String accessToRefreshKey = redisKey(ACCESS_TO_REFRESH + tokenValue);
        AccessToken access = convertClass(redisTemplate.opsForValue().get(accessKey), AccessToken.class);
        Authentication authentication = convertClass(redisTemplate.opsForValue().get(authKey), Authentication.class);
        redisTemplate.delete(accessKey);
        redisTemplate.delete(accessToRefreshKey);
        redisTemplate.delete(authKey);

        if (null != authentication) {
            String key = authenticationKeyGenerator.extractKey(authentication);
            String authToAccessKey = redisKey(AUTH_TO_ACCESS + key);
            String unameKey = redisKey(UNAME_TO_ACCESS + getUsername(authentication));
            redisTemplate.delete(authToAccessKey);
            redisTemplate.opsForSet().remove(unameKey, access);
            redisTemplate.delete(redisKey(ACCESS + key));
        }
    }

    /**
     * Store refresh token to redis.
     *
     * @param refreshToken   the refresh token
     * @param authentication the authentication
     */
    @Override
    public void storeRefreshToken(RefreshToken refreshToken, Authentication authentication) {
        String refreshKey = redisKey(REFRESH + refreshToken.getValue());
        String refreshAuthKey = redisKey(REFRESH_AUTH + refreshToken.getValue());
        redisTemplate.opsForValue().set(refreshKey, refreshToken);
        redisTemplate.opsForValue().set(refreshAuthKey, authentication);
        if (null != refreshToken.getExpiration()) {
            int seconds = Long.valueOf((refreshToken.getExpiration().getTime() - System.currentTimeMillis()) / 1000L)
                    .intValue();
            redisTemplate.expire(refreshKey, seconds, TimeUnit.SECONDS);
            redisTemplate.expire(refreshAuthKey, seconds, TimeUnit.SECONDS);
        }
    }

    /**
     * Read Refresh Token from redis.
     *
     * @param refreshToken the refresh token
     * @return return Refresh Token
     */
    @Override
    public RefreshToken readRefreshToken(String refreshToken) {
        return convertClass(redisTemplate.opsForValue().get(redisKey(REFRESH + refreshToken)), RefreshToken.class);
    }

    /**
     * Read Authentication object from redis by refresh token.
     *
     * @param refreshToken the refresh token
     * @return return Authentication object
     */
    @Override
    public Authentication readAuthenticationForRefreshToken(RefreshToken refreshToken) {
        return readAuthenticationForRefreshToken(refreshToken.getValue());
    }

    /**
     * Reads authentication information base on access token.
     *
     * @param token the access token
     * @return Authentication instance from redis
     */
    public Authentication readAuthenticationForRefreshToken(String token) {
        return convertClass(redisTemplate.opsForValue().get(redisKey(REFRESH_AUTH + token)), Authentication.class);
    }

    /**
     * Remove Refresh Token.
     *
     * @param refreshToken the refresh token need to remove
     */
    @Override
    public void removeRefreshToken(RefreshToken refreshToken) {
        removeRefreshToken(refreshToken.getValue());
    }

    /**
     * Removes refresh token.
     *
     * @param tokenValue the refresh token
     */
    public void removeRefreshToken(String tokenValue) {
        String refreshKey = redisKey(REFRESH + tokenValue);
        String refreshAuthKey = redisKey(REFRESH_AUTH + tokenValue);
        String refresh2AccessKey = redisKey(REFRESH_TO_ACCESS + tokenValue);
        String access2RefreshKey = redisKey(ACCESS_TO_REFRESH + tokenValue);
        redisTemplate.delete(refreshKey);
        redisTemplate.delete(refreshAuthKey);
        redisTemplate.delete(refresh2AccessKey);
        redisTemplate.delete(access2RefreshKey);
    }

    /**
     * Remove Access Token by using refresh token object.
     *
     * @param refreshToken the refresh token
     */
    @Override
    public void removeAccessTokenUsingRefreshToken(RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    /**
     * Removes access token using refresh token to generation.
     *
     * @param refreshToken the refresh token
     */
    private void removeAccessTokenUsingRefreshToken(String refreshToken) {
        String key = redisKey(REFRESH_TO_ACCESS + refreshToken);
        String accessToken = convertClass(redisTemplate.opsForValue().get(key), String.class);
        redisTemplate.delete(key);
        if (null == accessToken) {
            return;
        }
        removeAccessToken(accessToken);
    }

    /**
     * Get Access Token by Authentication object.
     *
     * @param authentication the authentication instance
     * @return return Access Token
     */
    @Override
    public AccessToken getAccessToken(Authentication authentication) {
        String key = authenticationKeyGenerator.extractKey(authentication);
        AccessToken accessToken = convertClass(redisTemplate.opsForValue().get(redisKey(AUTH_TO_ACCESS + key)), AccessToken.class);
        if (null != accessToken) {
            Authentication storedAuthentication = readAuthentication(accessToken.getValue());
            if ((null == storedAuthentication || !key.equals(authenticationKeyGenerator.extractKey(storedAuthentication)))) {
                // Keep the stores consistent (maybe the same user is
                // represented by this authentication but the details have
                // changed)
                storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }

    /**
     * Find List Access Token of account from redis.
     *
     * @param username the username
     * @return return collection Access Token.
     */
    @Override
    public Collection<AccessToken> findTokensByUserName(String username) {
        String usernameKey = redisKey(UNAME_TO_ACCESS + username);
        Set<Object> results = redisTemplate.opsForSet().members(usernameKey);
        if (null == results || 0 == results.size()) {
            return Collections.emptySet();
        }
        List<AccessToken> accessTokens = new ArrayList<>();
        results.forEach(result -> {
            accessTokens.add(convertClass(result, AccessToken.class));
        });
        return Collections.unmodifiableCollection(accessTokens);
    }

    @Override
    public void afterPropertiesSet() {
        prefix = jwtProperties.getPrefix();
    }
}
