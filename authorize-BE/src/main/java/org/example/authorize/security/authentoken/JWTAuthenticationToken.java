package org.example.authorize.security.authentoken;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * JWT Authentication Token.
 */
public class JWTAuthenticationToken extends PreAuthenticatedAuthenticationToken {

    private Authentication authentication;

    public JWTAuthenticationToken(Object aPrincipal, Object aCredentials) {
        super(aPrincipal, aCredentials);
    }

    public JWTAuthenticationToken(Object aPrincipal, Object aCredentials, Authentication authentication) {
        super(aPrincipal, aCredentials);
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}
