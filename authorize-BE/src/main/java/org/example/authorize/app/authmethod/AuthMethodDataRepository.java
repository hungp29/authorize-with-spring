package org.example.authorize.app.authmethod;

import org.example.authorize.entity.AuthMethodData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthMethodDataRepository extends JpaRepository<AuthMethodData, String> {
}
