package org.example.authorize.app.principal.attempt;

import org.example.authorize.entity.PrincipalAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrincipalAttemptRepository extends JpaRepository<PrincipalAttempt, String> {
}
