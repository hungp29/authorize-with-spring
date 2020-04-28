package org.example.authorize.security.userdetails;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;

public interface TokenUserDetailsService<T extends Authentication> extends AuthenticationUserDetailsService<T> {
}
