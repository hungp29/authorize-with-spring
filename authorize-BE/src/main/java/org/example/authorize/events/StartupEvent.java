package org.example.authorize.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authorize.app.account.AccountService;
import org.example.authorize.app.permission.PermissionDTO;
import org.example.authorize.app.permission.PermissionService;
import org.example.authorize.app.policy.PolicyService;
import org.example.authorize.app.principal.PrincipalService;
import org.example.authorize.app.role.RoleService;
import org.example.authorize.config.prop.ApplicationProperties;
import org.example.authorize.entity.Account;
import org.example.authorize.entity.Policy;
import org.example.authorize.entity.Principal;
import org.example.authorize.entity.Role;
import org.example.authorize.utils.generator.Generator;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
    private final AccountService accountService;
    private final PrincipalService principalService;

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
        Role superRole = roleService.findSuperRole().orElse(null);

        // Create new super role if it's not exist
        if (null == superRole) {
            log.debug("Super Role don't exist. Creating new one.");
            superRole = roleService.createRole(appProps.getSuperRoleName(), true, false);
            superRole = roleService.save(superRole);
        }
        // Attach role to policy
        policyService.attachRoleToPolicy(policyFullAccess, superRole);

        // Create new super account if it's not exist
        List<Account> superAccounts = accountService.findAccountByRole(superRole);
        if (CollectionUtils.isEmpty(superAccounts)) {
            Account superAccount = accountService.createAndSaveByUsernameAndPassword(appProps.getSuperAccount().getUsername(), appProps.getSuperAccount().getPassword());

            // Set super role and active super account
            Principal superAccountPrincipal = superAccount.getPrincipal();
            superAccountPrincipal.setRoles(Collections.singletonList(superRole));
            superAccountPrincipal.setDisabled(false);
            principalService.save(superAccountPrincipal);
        }
    }

    /**
     * Get policy full access. If application have new API or some API is out of date then they will be update to full access policy.
     *
     * @return return policy full access
     */
    private Policy getPolicyFullAccess() {
        // Get all API need to permission to access
        List<PermissionDTO> permissionDTOs = permissionService.getPermissions();

        // Generating id of policy full access
        Policy policyFullAccess = policyService.findPolicyByName(appProps.getPolicyFullAccess()).orElse(null);

        if (null == policyFullAccess) {
            // Create new policy full access if it's not exist
            log.debug("Creating new full access policy");
            policyFullAccess = policyService.createPolicy(appProps.getPolicyFullAccess(), true);
            policyFullAccess = policyService.save(policyFullAccess);

            // Grant permission for new policy
            permissionService.grantPermissionForPolicy(policyFullAccess, permissionDTOs);
        } else {
            // Update permissions for policy
            permissionService.updatePermissionForPolicy(policyFullAccess, permissionDTOs);
        }

        return policyFullAccess;
    }
}
