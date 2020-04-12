package org.example.authorize.rbac;

public interface RequestValueResolver<T> {
    T resolve();
}
