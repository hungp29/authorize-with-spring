package org.example.authorize.rbac.condition;

import org.example.authorize.rbac.AbstractAccessCondition;
import org.example.authorize.rbac.ConditionPrototype;
import org.example.authorize.rbac.RequestValueResolver;

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
