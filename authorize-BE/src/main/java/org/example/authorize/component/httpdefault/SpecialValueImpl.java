package org.example.authorize.component.httpdefault;

import org.example.authorize.security.UserPrincipal;
import org.example.authorize.utils.ObjectUtils;
import org.example.authorize.utils.constants.Constants;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Special Value Implementation.
 */
public class SpecialValueImpl {

    private SpecialValueImpl() {
    }

    /**
     * Get Special Value.
     *
     * @param specialValue special type
     * @return special value
     */
    public static Object getSpecialValue(SpecialValue specialValue) {
        switch (specialValue) {
            case CURRENT_USER_ID:
                return getCurrentUserId();
            default:
                return null;
        }
    }

    /**
     * Get current user id (user is login).
     *
     * @return id of user
     */
    public static String getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserPrincipal) principal).getId();
        } else if (!principal.equals("anonymousUser")) {
            return (String) principal;
        }
        return Constants.EMPTY_STRING;
    }
}
