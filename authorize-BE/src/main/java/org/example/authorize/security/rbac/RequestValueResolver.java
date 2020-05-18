package org.example.authorize.security.rbac;

public interface RequestValueResolver<T> {
    T resolve();
}
