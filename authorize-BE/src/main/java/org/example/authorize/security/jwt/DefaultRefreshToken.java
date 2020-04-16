package org.example.authorize.security.jwt;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * Default Refresh Token.
 */
@EqualsAndHashCode
public class DefaultRefreshToken implements RefreshToken, Serializable {

    private String value;

    private Date expiration;

    /**
     * Create a new refresh token.
     */
    public DefaultRefreshToken(String value, Date expiration) {
        this.value = value;
        this.expiration = expiration;
    }

    /**
     * Default constructor for JPA and other serialization tools.
     */
    @SuppressWarnings("unused")
    private DefaultRefreshToken() {
        this(null, null);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Date getExpiration() {
        return expiration;
    }

}
