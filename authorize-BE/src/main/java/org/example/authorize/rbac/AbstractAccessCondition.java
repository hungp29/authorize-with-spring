package org.example.authorize.rbac;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAccessCondition<T> implements AccessCondition<T> {

    /**
     * This resolver will resolve to the request value to be supplied to the validate function.
     */
    @Getter
    protected final RequestValueResolver<T> requestedValueResolver;

    public abstract ConditionPrototype getConditionPrototype();

    public abstract boolean validate(T permittedValue);
}
