package org.example.authorize.app.role;

import lombok.RequiredArgsConstructor;
import org.example.authorize.entity.Role;
import org.example.authorize.exception.SaveEntityException;
import org.example.authorize.component.generator.id.Generator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Role service.
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    private final Generator<String> generator;

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
     * Find super role.
     *
     * @return return super role if it's exist
     */
    public Optional<Role> findSuperRole() {
        return roleRepository.findByReadOnlyTrueAndSystemRoleFalse();
    }

    /**
     * Create new one or update role.
     *
     * @param role the role object
     * @return return new role is created
     */
    @Transactional
    public Role save(Role role) {
        if (null != role) {
            return roleRepository.save(role);
        }
        throw new SaveEntityException("Role is empty, cannot save it");
    }

    /**
     * Create new role but don't save it.
     *
     * @param name name of role
     * @return return role is created.
     */
    public Role createRole(String name) {
        Role role = new Role();
        role.setName(name);
        role.setReadOnly(false);
        role.setSystemRole(false);
        return role;
    }

    /**
     * Create new role but don't save it.
     *
     * @param name     name of role
     * @param readOnly set read only for role or not
     * @return return role is created.
     */
    public Role createRole(String name, boolean readOnly) {
        Role role = createRole(name);
        role.setReadOnly(readOnly);
        return role;
    }

    /**
     * Create new role but don't save it.
     *
     * @param name       name of role
     * @param readOnly   set read only for role or not
     * @param systemRole set system role or not
     * @return return role is created.
     */
    public Role createRole(String name, boolean readOnly, boolean systemRole) {
        Role role = createRole(name);
        role.setReadOnly(readOnly);
        role.setSystemRole(systemRole);
        return role;
    }
}
