package org.example.authorize.app.authmethod;

import org.example.authorize.entity.AuthMethod;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Auth method repository.
 */
public interface AuthMethodRepository extends JpaRepository<AuthMethod, String> {
}
