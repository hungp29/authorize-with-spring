package org.example.authorize.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Application Properties.
 */
@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String name;

    private String superRoleName;

    private String policyFullAccess;

    private SuperAccount superAccount;

    private short loginAttemptsThreshold;

    private String versionMediaType;

    /**
     * Super account properties.
     */
    @Data
    public static class SuperAccount {

        private String username;

        private String password;
    }
}
