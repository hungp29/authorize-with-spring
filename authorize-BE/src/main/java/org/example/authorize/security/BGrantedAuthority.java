package org.example.authorize.security;

import lombok.Data;
import org.example.authorize.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom granted authority.
 */
@Data
public class BGrantedAuthority implements GrantedAuthority {

    private String name;

    private List<PolicyPermission> policyPermissions;

    private List<PolicyCondition> policyConditions;

    public BGrantedAuthority(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }

    /**
     * Nested class PolicyPermission.
     */
    @Data
    public static class PolicyPermission implements Serializable {

        private String id;

        private String policyId;

        private String permission;

        public static PolicyPermission create(String id, String policyId, String permission) {
            PolicyPermission policyPermission = new PolicyPermission();
            policyPermission.setId(id);
            policyPermission.setPolicyId(policyId);
            policyPermission.setPermission(permission);
            return policyPermission;
        }
    }

    /**
     * Nested class PolicyCondition.
     */
    @Data
    public static class PolicyCondition implements Serializable {

        private String id;

        private String policyId;

        private String operator;

        private String conditionValue;

        public static PolicyCondition create(String id, String policyId, String operator, String conditionValue) {
            PolicyCondition policyCondition = new PolicyCondition();
            policyCondition.setId(id);
            policyCondition.setPolicyId(policyId);
            policyCondition.setOperator(operator);
            policyCondition.setConditionValue(conditionValue);
            return policyCondition;
        }
    }

    /**
     * Create GrantedAuthority object by Role.
     *
     * @param role the role
     * @return return GrantedAuthority object
     */
    public static BGrantedAuthority create(Role role) {
        Assert.notNull(role, "The role cannot be null");
        BGrantedAuthority authority = new BGrantedAuthority(role.getName());

        if (!CollectionUtils.isEmpty(role.getPolicies())) {
            List<PolicyPermission> policyPermissions = new ArrayList<>();
            List<PolicyCondition> policyConditions = new ArrayList<>();
            role.getPolicies().forEach(policy -> {
                // Convert Policy Permission to Nested class Policy Permission of BGrantedAuthority class
                if (!CollectionUtils.isEmpty(policy.getPolicyPermissions())) {
                    policy.getPolicyPermissions().forEach(policyPermission -> {
                        policyPermissions
                                .add(PolicyPermission.create(policyPermission.getId(), policy.getId(),
                                        policyPermission.getPermission()));
                    });
                }

                // Convert Policy Condition to Nested class Policy Condition of BGrantedAuthority class
                if (!CollectionUtils.isEmpty(policy.getPolicyConditions())) {
                    policy.getPolicyConditions().forEach(policyCondition -> {
                        policyConditions
                                .add(PolicyCondition.create(policyCondition.getId(), policy.getId(),
                                        policyCondition.getOperator(), policyCondition.getConditionValue()));
                    });
                }
            });

            authority.setPolicyPermissions(policyPermissions);
            authority.setPolicyConditions(policyConditions);
        }
        return authority;
    }
}
