package org.example.authorize.config;

import org.example.authorize.security.UserPrincipal;
import org.example.authorize.utils.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Configuration audit for JPA.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**
     * Implementation AuditorAware.
     */
    public static class AuditorAwareImpl implements AuditorAware<String> {

        @Override
        public Optional<String> getCurrentAuditor() {
            // Set SYSTEM value to default
            AtomicReference<String> currentAuditor = new AtomicReference<>(Constants.SYSTEM);
            // If Authentication is not null then get auditor from Authentication
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional.ofNullable(auth)
                    .filter(Authentication::isAuthenticated)
                    .map(Authentication::getPrincipal)
                    .ifPresent(principalObj -> {
                        if (principalObj instanceof UserPrincipal) {
                            currentAuditor.set(((UserPrincipal) principalObj).getId());
                        } else if (principalObj instanceof String) {
                            currentAuditor.set((String) principalObj);
                        }
                    });

            return Optional.of(currentAuditor.get());
        }
    }
}
