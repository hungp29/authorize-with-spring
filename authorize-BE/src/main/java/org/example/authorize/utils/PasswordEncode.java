package org.example.authorize.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Password encode class.
 */
@Component
public class PasswordEncode {

    private PasswordEncoder passwordEncoder;

    public PasswordEncode() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Encode password.
     *
     * @param value the password need to encode
     * @return return the password
     */
    public String encode(String value) {
        Assert.notNull(value, "The password have to value");
        return passwordEncoder.encode(value);
    }
}
