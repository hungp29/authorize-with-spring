package org.example.authorize.config;

import org.example.authorize.version.VersionRequestMappingHandlerMapping;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * App Configuration.
 */
@Configuration
public class AppConfiguration {

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
                return new VersionRequestMappingHandlerMapping();
            }
        };
    }
}
