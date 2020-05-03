package org.example.authorize.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "velocity", ignoreUnknownFields = false)
public class VelocityProperties {

    private String resourceLoader;
    private String classLoader;
    private String templatePath;
    private String suffix;
}
