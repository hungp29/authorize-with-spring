package org.example.authorize.security.rbac;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public abstract class AbstractRequestResolver<T> implements RequestValueResolver<T> {

    protected final Authentication authentication;
    protected final Object[] arguments;

    public abstract T resolve();
}
