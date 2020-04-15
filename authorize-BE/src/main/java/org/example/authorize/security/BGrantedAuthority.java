package org.example.authorize.security;

import org.example.authorize.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * Custom granted authority.
 */
public class BGrantedAuthority implements GrantedAuthority {

    private String name;

    public BGrantedAuthority(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }

    /**
     * Create GrantedAuthority object by Role.
     *
     * @param role the role
     * @return return GrantedAuthority object
     */
    public static BGrantedAuthority create(Role role) {
        Assert.notNull(role, "The role cannot be null");
        return new BGrantedAuthority(role.getName());
    }
}
