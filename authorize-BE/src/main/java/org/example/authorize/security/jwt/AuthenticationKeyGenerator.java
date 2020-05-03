package org.example.authorize.security.jwt;

import org.example.authorize.exception.AuthenticationKeyException;
import org.example.authorize.security.UserPrincipal;
import org.springframework.security.core.Authentication;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Basic key generator taking into account the username (principal name) if they exist.
 */
public class AuthenticationKeyGenerator {

    private static final String PRINCIPAL_ID = "principal_id";
//    private static final String AUTH_METHOD_ID = "auth_method_id";

    /**
     * Extracts keys from authentication instance.
     *
     * @param authentication the Authentication instance
     * @return the key has been hash
     */
    public String extractKey(Authentication authentication) {
        Map<String, String> values = new LinkedHashMap<String, String>();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        values.put(PRINCIPAL_ID, principal.getId());
        return generateKey(values);
    }

    /**
     * Generation key.
     *
     * @param values the values
     * @return key
     */
    private String generateKey(Map<String, String> values) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(values.toString().getBytes(StandardCharsets.UTF_8));
            return String.format("%032x", new BigInteger(1, bytes));
        } catch (NoSuchAlgorithmException ex) {
            throw new AuthenticationKeyException("MD5 algorithm not available.  Fatal (should be in the JDK).", ex);
        }
    }
}
