package org.example.authorize.utils;

import org.example.authorize.config.SecurityConfiguration;
import org.example.authorize.entity.Account;
import org.example.authorize.utils.constants.Constants;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
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

    /**
     * Format role name by replace space with underscore.
     *
     * @param name the name of role
     * @return return the name is formatted
     */
    public static String formatRoleName(String name) {
        if (!StringUtils.isEmpty(name)) {
            return name.trim().replaceAll(Constants.SPACE, Constants.UNDERSCORE);
        }
        return Constants.EMPTY_STRING;
    }

    /**
     * Checking account have principal information.
     *
     * @param account the account need to checking
     * @return return true if account have principal info, otherwise return false
     */
    public static boolean hasPrincipal(Account account) {
        return null != account && null != account.getPrincipal();
    }

    /**
     * Checking account have auth method.
     *
     * @param account the account need to checking
     * @return return true if account have any auth method, otherwise return false
     */
    public static boolean hasAuthMethod(Account account) {
        return null != account && null != account.getPrincipal() && !CollectionUtils.isEmpty(account.getPrincipal().getAuthMethods());
    }

    /**
     * Checking account have role.
     *
     * @param account the account need to checking
     * @return return true if account have any role, otherwise return false
     */
    public static boolean hasRole(Account account) {
        return null != account && null != account.getPrincipal() && !CollectionUtils.isEmpty(account.getPrincipal().getRoles());
    }
}
