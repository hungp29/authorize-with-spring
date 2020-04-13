package org.example.authorize.app.policy;

import lombok.RequiredArgsConstructor;
import org.example.authorize.entity.Policy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Policy Service.
 */
@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    /**
     * Save new policy.
     *
     * @param policy the new policy
     * @return return the policy is created successfully
     */
    @Transactional
    public Policy save(Policy policy) {
        return policyRepository.save(policy);
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
}
