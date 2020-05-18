package org.example.authorize.config;

import lombok.RequiredArgsConstructor;
import org.example.authorize.config.prop.ApplicationProperties;
import org.example.authorize.component.version.VersionRequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * App Configuration.
 */
@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

    private final ApplicationProperties appProps;

    /**
     * Configuration Version RequestMappingHandlerMapping.
     *
     * @return return WebMvcRegistrations instance
     */
    @Bean
    public WebMvcRegistrations webMvcRegistrationsHandlerMapping() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                VersionRequestMappingHandlerMapping versionMapping = new VersionRequestMappingHandlerMapping();
                versionMapping.setAcceptMediaType(appProps.getVersionMediaType());
                return versionMapping;
            }
        };
    }
}
