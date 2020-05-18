package org.example.authorize.app.common;

import org.example.authorize.component.aspect.executiontime.LogExecutionTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Base Service.
 */
public class BaseService {

    /**
     * Get Authentication.
     *
     * @return return Authentication instance
     */
    @LogExecutionTime
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
