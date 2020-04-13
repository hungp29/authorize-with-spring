package org.example.authorize.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authorize.app.permission.PermissionDTO;
import org.example.authorize.app.permission.PermissionService;
import org.example.authorize.app.policy.PolicyService;
import org.example.authorize.app.role.RoleService;
import org.example.authorize.config.prop.ApplicationProperties;
import org.example.authorize.entity.Policy;
import org.example.authorize.entity.PolicyPermission;
import org.example.authorize.entity.Role;
import org.example.authorize.generator.Generator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Startup event class.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class StartupEvent {

    private final ApplicationProperties appProps;

    private final PermissionService permissionService;
    private final RoleService roleService;
    private final PolicyService policyService;
    private final Generator<String> generator;

    /**
     * Once the app is ready, it will check for the existence of an Super Admin account,
     * if it doesn't exist, create a new one.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onStartApp() {
        log.debug("Checking the existence of the super account");

        // Get policy full access
        Policy policyFullAccess = getPolicyFullAccess();

        // Generating id of super role
        String superRoleId = generator.generate(appProps.getSuperRoleName());
        Role superRole = roleService.findRoleById(superRoleId).orElse(null);

        // Create new super role if it's not exist
        if (null == superRole) {
            log.debug("Super Role don't exist. Creating new one.");
            superRole = new Role();
            superRole.setId(generator.generate(superRoleId));
            superRole.setReadOnly(true);
            superRole.setSystemRole(false);
            superRole.setName(appProps.getSuperRoleName());
            superRole = roleService.save(superRole);

            // Attach role to policy
            policyFullAccess.setRoles(Collections.singletonList(superRole));
            policyFullAccess = policyService.save(policyFullAccess);
        }
    }

    /**
     * Get policy full access.
     *
     * @return return policy full access
     */
    private Policy getPolicyFullAccess() {
        // Get all API need to permission to access
        List<PermissionDTO> permissions = permissionService.getPermissions();

        // Generating id of policy full access
        String policyFullAccessId = generator.generate(appProps.getPolicyFullAccess());
        Policy policyFullAccess = policyService.findPolicyById(policyFullAccessId).orElse(null);

        // Create new policy full access if it's not exist
        if (null == policyFullAccess) {
            log.debug("Creating new full access policy");
            policyFullAccess = new Policy();
            policyFullAccess.setId(policyFullAccessId);
            policyFullAccess.setReadOnly(true);
            policyFullAccess.setPolicyName(appProps.getPolicyFullAccess());
            policyFullAccess = policyService.save(policyFullAccess);

            // Grant permission for new policy
            permissionService.grantPermissionForPolicy(policyFullAccess, permissions);
        } else {
            List<PolicyPermission> policyPermissions = policyFullAccess.getPolicyPermissions();
        }

        return policyFullAccess;
    }
}
