package org.example.authorize.security.rbac;

public interface AccessCondition<T> {

    ConditionPrototype getConditionPrototype();

    boolean validate(T permittedValue);

    RequestValueResolver<T> getRequestedValueResolver();
}
