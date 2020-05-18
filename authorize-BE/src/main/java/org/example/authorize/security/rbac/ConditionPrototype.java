package org.example.authorize.security.rbac;

import lombok.Data;

@Data
public class ConditionPrototype {

    /**
     * Distinguishable name of this condition type.
     */
    private String operatorName;

    /**
     * Type of condition value. This is to guide the frontend to better display the input.
     */
    private ConditionValueType conditionValueType;

    /**
     * Constraint for the condition value, encoded to string. This is to guide the frontend to better display the input.
     */
    private String valueConstraint;
}
