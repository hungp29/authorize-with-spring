package org.example.authorize.security.rbac.condition;

import org.example.authorize.security.rbac.AbstractAccessCondition;
import org.example.authorize.security.rbac.ConditionPrototype;
import org.example.authorize.security.rbac.RequestValueResolver;

public class OwnedResourceCondition extends AbstractAccessCondition<Boolean> {

    public OwnedResourceCondition(RequestValueResolver requestedValueResolver) {
        super(requestedValueResolver);
    }

    @Override
    public ConditionPrototype getConditionPrototype() {
        ConditionPrototype conditionPrototype = new ConditionPrototype();
        conditionPrototype.setOperatorName("owned_resource");
        return conditionPrototype;
    }

    @Override
    public boolean validate(Boolean permittedValue) {
        return permittedValue;
    }
}
