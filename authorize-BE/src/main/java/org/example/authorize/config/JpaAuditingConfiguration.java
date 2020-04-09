package org.example.authorize.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration audit for JPA.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}
