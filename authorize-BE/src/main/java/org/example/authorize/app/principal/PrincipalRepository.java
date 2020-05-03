package org.example.authorize.app.principal;

import org.example.authorize.entity.Principal;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Principal Repository.
 */
public interface PrincipalRepository extends JpaRepository<Principal, String> {
}
