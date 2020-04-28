package org.example.authorize.security.authentoken;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * OTP Authentication Token.
 */
public class OTPAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Object credentials;

    public OTPAuthenticationToken(Object principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
    }

    public OTPAuthenticationToken(Object aPrincipal, Object aCredentials,
                                  Collection<? extends GrantedAuthority> anAuthorities) {
        super(anAuthorities);
        this.principal = aPrincipal;
        this.credentials = aCredentials;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
