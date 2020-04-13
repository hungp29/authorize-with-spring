package org.example.authorize.app.role;

import org.example.authorize.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    /**
     * Find Super Admin role.
     *
     * @return return Super Admin role if it's exist
     */
    Optional<Role> findByReadOnlyTrueAndSystemRoleFalse();

    /**
     * Find role by id.
     *
     * @param id the id of role
     * @return return the role has id equals
     */
    Optional<Role> findById(String id);
}
