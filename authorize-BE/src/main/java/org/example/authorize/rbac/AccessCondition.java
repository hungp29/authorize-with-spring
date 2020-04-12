package org.example.authorize.rbac;

public interface AccessCondition<T> {

    ConditionPrototype getConditionPrototype();

    boolean validate(T permittedValue);
}
