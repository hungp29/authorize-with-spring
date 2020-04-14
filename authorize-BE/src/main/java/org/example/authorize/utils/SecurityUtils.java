package org.example.authorize.utils;

import org.example.authorize.config.SecurityConfiguration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

public class SecurityUtils {

    /**
     * Prevents new instance.
     */
    private SecurityUtils() {
    }

    /**
     * Checking the path is match WHITE list or not.
     *
     * @param requestMethod request method of path
     * @param path          the path need to check
     * @return return true if path and request method are match
     */
    public static boolean isMatchWhiteList(RequestMethod requestMethod, String path) {
        String[] patterns;
        // Check for GET, POST, PUT, DELETE method
        switch (requestMethod) {
            case GET:
                patterns = SecurityConfiguration.GET_WHITE_LIST;
                break;
            case POST:
                patterns = SecurityConfiguration.POST_WHITE_LIST;
                break;
            case PUT:
                patterns = SecurityConfiguration.PUT_WHITE_LIST;
                break;
            case DELETE:
                patterns = SecurityConfiguration.DELETE_WHITE_LIST;
                break;
            default:
                patterns = new String[]{};
                break;
        }
        AntPathMatcher matcher = new AntPathMatcher();

        for (String pattern : patterns) {
            if (matcher.match(pattern, path)) {
                return true;
            }
        }

        // Check for all method
        patterns = SecurityConfiguration.WHITE_LIST;
        for (String pattern : patterns) {
            if (matcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}
