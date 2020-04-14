package org.example.authorize.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String superRoleName;

    private String policyFullAccess;

    private SuperAccount superAccount;

    @Data
    public static class SuperAccount {

        private String username;

        private String password;
    }
}
