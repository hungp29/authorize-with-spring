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
     * Find Auth method by auth type and determine.
     *
     * @param authType    auth type
     * @param determineId determine id (username, email, phone number)
     * @return return authentication method
     */
    Optional<AuthMethod> findByAuthTypeAndDetermineId(AuthType authType, String determineId);

    /**
     * Find Auth method by determine.
     *
     * @param determineId determine id (username, email, phone number)
     * @return return AuthMethod instance if it exist
     */
    Optional<AuthMethod> findByDetermineId(String determineId);

    /**
     * Find Auth method by determine id and list auth type.
     *
     * @param determineId determine id (username, email, phone number)
     * @param authTypes   list auth type
     * @return return auth method
     */
    Optional<AuthMethod> findByDetermineIdAndAuthTypeIn(String determineId, AuthType... authTypes);
}
