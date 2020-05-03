package org.example.authorize.app.common;

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
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
