package org.example.authorize.security.jwt;

import java.util.Date;

/**
 * Access Token.
 */
public interface AccessToken {

    /**
     * Gets refresh token.
     *
     * @return RefreshToken instance
     */
    RefreshToken getRefreshToken();

    /**
     * Sets refresh token.
     *
     * @param refreshToken the refresh token
     */
    void setRefreshToken(RefreshToken refreshToken);

    /**
     * Check token is expired.
     *
     * @return true or false
     */
    boolean isExpired();

    /**
     * Get expiration date.
     *
     * @return the expiration date
     */
    Date getExpiration();

    /**
     * Gets time expiration in seconds.
     *
     * @return the time expiration
     */
    int getExpiresIn();

    /**
     * Gets access token.
     *
     * @return the access token
     */
    String getToken();
}
