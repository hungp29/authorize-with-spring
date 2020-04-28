package org.example.authorize.security.userdetails;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Interface that allows for retrieving a UserDetails object based on an
 * OTP Authentication object.
 *
 * @param <T>
 */
public interface OTPUserDetailsService<T extends Authentication> {

    /**
     * @param otpToken The otp authentication token
     * @return UserDetails for the given authentication token, never null.
     * @throws UsernameNotFoundException if no user details can be found for the given
     *                                   authentication token
     */
    UserDetails loadUserDetailsForOTP(T otpToken) throws UsernameNotFoundException;
}
