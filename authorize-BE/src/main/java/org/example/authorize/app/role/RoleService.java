package org.example.authorize.app.role;

import lombok.RequiredArgsConstructor;
import org.example.authorize.entity.Role;
import org.example.authorize.generator.Generator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Role service.
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * Find role by id.
     *
     * @param id the id of role
     * @return return the role has id equals
     */
    public Optional<Role> findRoleById(String id) {
        return roleRepository.findById(id);
    }

    /**
     * Create new one or update role.
     *
     * @param role the role object
     * @return return new role is created
     */
    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
