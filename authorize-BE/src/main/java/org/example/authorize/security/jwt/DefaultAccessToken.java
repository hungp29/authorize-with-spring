package org.example.authorize.security.jwt;

import java.io.Serializable;
import java.util.Date;

/**
 * Default Access Token.
 */
public class DefaultAccessToken implements AccessToken, Serializable {

    private String value;
    private Date expiration;
    private RefreshToken refreshToken;

    public DefaultAccessToken(String value, Date expiration, RefreshToken refreshToken) {
        this.value = value;
        this.expiration = expiration;
        this.refreshToken = refreshToken;
    }

    @Override
    public RefreshToken getRefreshToken() {
        return null;
    }

    @Override
    public void setRefreshToken(RefreshToken refreshToken) {

    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public Date getExpiration() {
        return null;
    }

    @Override
    public int getExpiresIn() {
        return 0;
    }

    @Override
    public String getValue() {
        return null;
    }
}
