package org.example.authorize.security.jwt;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Date;

/**
 * Refresh token.
 */
public interface RefreshToken {

    /**
     * The value of token.
     *
     * @return The value of token
     */
    @JsonValue
    String getValue();

    /**
     * The expiration time for refresh token.
     *
     * @return the expiration time
     */
    Date getExpiration();
}
