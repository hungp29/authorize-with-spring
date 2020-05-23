package org.example.authorize.app.policy;

import lombok.RequiredArgsConstructor;
import org.example.authorize.entity.Policy;
import org.example.authorize.entity.Role;
import org.example.authorize.exception.SaveEntityException;
import org.example.authorize.component.generator.id.Generator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Policy Service.
 */
@Service
@RequiredArgsConstructor
public class PolicyService {

    private final Generator<String> generator;

    private final PolicyRepository policyRepository;

    /**
     * Save new policy.
     *
     * @param policy the new policy
     * @return return the policy is created successfully
     */
    @Transactional
    public Policy save(Policy policy) {
        if (null != policy) {
            return policyRepository.save(policy);
        }
        throw new SaveEntityException("Policy is empty, cannot save it");
    }

    /**
     * Find policy by id.
     *
     * @param id the id of policy
     * @return return the policy if it's exist
     */
    public Optional<Policy> findPolicyById(String id) {
        return policyRepository.findById(id);
    }

    /**
     * Find policy by name.
     *
     * @param name the name of policy
     * @return return the policy if it's exist
     */
    public Optional<Policy> findPolicyByName(String name) {
        return policyRepository.findByName(name);
    }

    /**
     * Create new policy but don't save it.
     *
     * @param name name of policy
     * @return return new policy is created
     */
    public Policy createPolicy(String name) {
        Policy policy = new Policy();
        policy.setName(name);
        policy.setReadOnly(false);
        return policy;
    }

    /**
     * Create new policy but don't save it.
     *
     * @param name     name of policy
     * @param readOnly set read only for policy or not
     * @return return new policy is created
     */
    public Policy createPolicy(String name, boolean readOnly) {
        Policy policy = createPolicy(name);
        policy.setReadOnly(readOnly);
        return policy;
    }

    /**
     * Attach role to policy.
     *
     * @param policy the policy
     * @param role   the role to attach
     */
    @Transactional
    public void attachRoleToPolicy(Policy policy, Role role) {
        if (null != policy && null != role) {
            List<Role> roles = policy.getRoles();
            if (CollectionUtils.isEmpty(roles)) {
                roles = new ArrayList<>();
            }

            if (roles.stream().noneMatch(eRole -> eRole.getId().equals(role.getId()))) {
                roles.add(role);
                policy.setRoles(roles);
                save(policy);
            }
        }
    }
}
