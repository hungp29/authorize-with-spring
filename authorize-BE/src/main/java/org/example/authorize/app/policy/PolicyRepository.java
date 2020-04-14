package org.example.authorize.app.policy;

import org.example.authorize.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<Policy, String> {

    /**
     * Find policy by id.
     *
     * @param id the id of policy
     * @return return policy if it's exist
     */
    Optional<Policy> findById(String id);

    /**
     * Find policy by name.
     *
     * @param name the name of policy
     * @return return policy if it's exist
     */
    Optional<Policy> findByName(String name);
}
