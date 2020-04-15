package org.example.authorize.app.authmethod;

import org.example.authorize.entity.AuthMethod;
import org.example.authorize.enums.AuthType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Auth method repository.
 */
public interface AuthMethodRepository extends JpaRepository<AuthMethod, String> {

    /**
     * Find Auth method by auth type and auth data 1.
     *
     * @param authType  auth type
     * @param authData1 auth data
     * @return return authentication method
     */
    Optional<AuthMethod> findByAuthTypeAndAuthData1(AuthType authType, String authData1);
}
