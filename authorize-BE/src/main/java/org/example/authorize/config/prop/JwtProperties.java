package org.example.authorize.config.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Jwt properties.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt", ignoreUnknownFields = false)
public class JwtProperties {

    private String signingKey;

    private int accessTokenValiditySeconds;

    private int refreshTokenValiditySeconds;

    private String prefix = "";

    private String name;
}
