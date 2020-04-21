package org.example.authorize.app.authmethod;

import lombok.RequiredArgsConstructor;
import org.example.authorize.entity.AuthMethod;
import org.example.authorize.enums.AuthType;
import org.example.authorize.utils.PasswordEncode;
import org.example.authorize.utils.generator.id.Generator;
import org.springframework.stereotype.Service;

/**
 * Auth method service.
 */
@Service
@RequiredArgsConstructor
public class AuthMethodService {

    private final Generator<String> generator;
    private final PasswordEncode passwordEncode;

    /**
     * Create auth method USERNAME and PASSWORD.
     *
     * @param username username
     * @param password password
     * @return return auth method
     */
    public AuthMethod createAuthMethodByUsernameAndPassword(String username, String password) {
        AuthMethod authMethod = new AuthMethod();
        authMethod.setId(generator.generate());
        authMethod.setAuthType(AuthType.USERNAME_PASSWORD);
        authMethod.setAuthData1(username);
        authMethod.setAuthData2(passwordEncode.encode(password));
        return authMethod;
    }
}
