package org.example.authorize.security.jwt;

import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * Store tokens.
 */
public interface TokenStore {
    /**
     * Reads Authentication instance by access token.
     *
     * @param accessToken the access token
     * @return Authentication instance
     */
    Authentication readAuthentication(AccessToken accessToken);

    /**
     * Reads Authentication instance by access token value.
     *
     * @param token the access token value
     * @return Authentication instance
     */
    Authentication readAuthentication(String token);

    /**
     * Store access token and authentication information.
     *
     * @param token          the access token
     * @param authentication the authentication
     */
    void storeAccessToken(AccessToken token, Authentication authentication);

    /**
     * Reads access token by it's value.
     *
     * @param token the access token value
     * @return AccessToken instance
     */
    AccessToken readAccessToken(String token);

    /**
     * Remove access token.
     *
     * @param accessToken the access token need to remove
     */
    void removeAccessToken(AccessToken accessToken);

    /**
     * Store refresh token.
     *
     * @param refreshToken   the refresh token
     * @param authentication the authentication
     */
    void storeRefreshToken(RefreshToken refreshToken, Authentication authentication);

    /**
     * Reads refresh token.
     *
     * @param refreshToken the refresh token
     * @return Refresh token instance
     */
    RefreshToken readRefreshToken(String refreshToken);

    /**
     * Reads authentication information base on refresh token.
     *
     * @param refreshToken the refresh token
     * @return Authentication instance
     */
    Authentication readAuthenticationForRefreshToken(RefreshToken refreshToken);

    /**
     * Removes refresh token.
     *
     * @param refreshToken the refresh token need to remove
     */
    void removeRefreshToken(RefreshToken refreshToken);

    /**
     * Removes access token is generated by refresh token.
     *
     * @param refreshToken the refresh token
     */
    void removeAccessTokenUsingRefreshToken(RefreshToken refreshToken);

    /**
     * Gets access token base on authentication.
     *
     * @param authentication the authentication instance
     * @return AccessToken instance
     */
    AccessToken getAccessToken(Authentication authentication);

    /**
     * Finds all access token by username.
     *
     * @param username the username
     * @return Collection of AccessToken
     */
    Collection<AccessToken> findTokensByUserName(String username);
}