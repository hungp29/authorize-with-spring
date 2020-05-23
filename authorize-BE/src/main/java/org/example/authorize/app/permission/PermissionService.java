package org.example.authorize.app.permission;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.policy.PolicyPermissionRepository;
import org.example.authorize.component.generator.id.Generator;
import org.example.authorize.entity.Policy;
import org.example.authorize.entity.PolicyPermission;
import org.example.authorize.security.permission.PermissionCondition;
import org.example.authorize.security.permission.PermissionConditions;
import org.example.authorize.security.permission.PermissionGroup;
import org.example.authorize.security.rbac.ConditionPrototype;
import org.example.authorize.utils.PermissionUtils;
import org.example.authorize.utils.SecurityUtils;
import org.example.authorize.utils.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final RequestMappingHandlerMapping handlerMapping;
    private final Generator<String> generator;

    private final PolicyPermissionRepository policyPermissionRepository;

    /**
     * Get all permission.
     *
     * @return return list permission
     */
    public List<PermissionDTO> getPermissions() {
        List<PermissionDTO> permissions = new ArrayList<>();
        // For each endpoint
        handlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            // path and method
            List<String> patterns = new ArrayList<>(requestMappingInfo.getPatternsCondition().getPatterns());
            List<RequestMethod> methods = new ArrayList<>(requestMappingInfo.getMethodsCondition().getMethods());
            if (patterns.size() > 0 && methods.size() > 0) {
                for (int i = 0; i < patterns.size(); i++) {
                    PermissionDTO permissionDTO = buildPermissionObject(patterns.get(i), methods.get(i), handlerMethod);
                    if (null != permissionDTO) {
                        permissions.add(permissionDTO);
                    }
                }
            }
        });

        return permissions.stream()
                .sorted(Comparator.comparing(PermissionDTO::getPermission))
                .collect(Collectors.toList());
    }

    /**
     * Build permission object base on endpoint, request method and handler method.
     *
     * @param endpoint      endpoint of API
     * @param requestMethod request method of API
     * @param handlerMethod handler method of API
     * @return return permission object
     */
    private PermissionDTO buildPermissionObject(String endpoint, RequestMethod requestMethod, HandlerMethod handlerMethod) {
        PermissionDTO permissionDTO = null;
        if (!SecurityUtils.isMatchWhiteList(requestMethod, endpoint)) {
            // Get permission group and conditions
            PermissionGroup permissionGroupClass = handlerMethod.getBeanType().getAnnotation(PermissionGroup.class);
            PermissionGroup permissionGroupMethod = handlerMethod.getMethod().getAnnotation(PermissionGroup.class);
            PermissionConditions permissionConditions = handlerMethod.getMethod().getAnnotation(PermissionConditions.class);

            permissionDTO = new PermissionDTO();

            // The value of permission is concat request method + ':' + endpoint.
            // Ex: POST:/api/v1/permissions
            String permission = requestMethod.name() + Constants.COLON + endpoint;
            permissionDTO.setPermission(permission);

            // Set permission group
            if (null != permissionGroupMethod) {
                permissionDTO.setGroup(permissionGroupMethod.value());
            } else if (null != permissionGroupClass) {
                permissionDTO.setGroup(permissionGroupClass.value());
            }

            // Set permission type
            permissionDTO.setType(PermissionUtils.getDefaultPermissionType(requestMethod));

            // Set permission condition information
            if (null != permissionConditions) {
                // Set permission name
                permissionDTO.setName(permissionConditions.value());

                // Overwrite permission type if it's determined
                if (!PermissionUtils.isEmpty(permissionConditions.type())) {
                    permissionDTO.setType(permissionConditions.type());
                }

                // Get permission condition
                PermissionCondition[] conditions = permissionConditions.conditions();
                if (conditions.length > 0) {
                    List<ConditionPrototype> conditionPrototypes = Arrays.stream(conditions)
                            .map(PermissionUtils::getConditionPrototype)
                            .collect(Collectors.toList());
                    permissionDTO.setConditions(conditionPrototypes);
                }
            }
        }
        return permissionDTO;
    }

    /**
     * Granting permissions for policy.
     *
     * @param policy         the policy contains permission
     * @param permissionDTOs list new permission
     * @return return list policy permission is saved successfully
     */
    @Transactional
    public List<PolicyPermission> grantPermissionForPolicy(Policy policy, Collection<PermissionDTO> permissionDTOs) {
        List<PolicyPermission> savedPolicyPermission = null;
        if (null != policy && !CollectionUtils.isEmpty(permissionDTOs)) {
            List<PolicyPermission> newPolicyPermissions = new ArrayList<>();
            permissionDTOs.forEach(permissionDTO -> {
                PolicyPermission policyPermission = new PolicyPermission();
                policyPermission.setPermission(permissionDTO.getPermission());
                policyPermission.setPolicy(policy);
                newPolicyPermissions.add(policyPermission);
            });

            savedPolicyPermission = policyPermissionRepository.saveAll(newPolicyPermissions);
        }
        return savedPolicyPermission;
    }

    /**
     * Forfeit permissions of Policy.
     *
     * @param policyPermissions list permission of policy will be forfeit
     */
    @Transactional
    public void forfeitPermissionForPolicy(List<PolicyPermission> policyPermissions) {
        if (!CollectionUtils.isEmpty(policyPermissions)) {
            policyPermissionRepository.deleteInBatch(policyPermissions);
        }
    }

    /**
     * Forfeit one permission of policy.
     *
     * @param policyPermission the permission will be forfeit
     */
    @Transactional
    public void forfeitPermissionForPolicy(PolicyPermission policyPermission) {
        if (null != policyPermission) {
            policyPermissionRepository.delete(policyPermission);
        }
    }

    /**
     * Update permission for policy.
     *
     * @param policy         policy need to update permission
     * @param permissionDTOs list permission
     */
    @Transactional
    public void updatePermissionForPolicy(Policy policy, List<PermissionDTO> permissionDTOs) {
        // If full access policy is exist, checking all permission to detect new permissions and out of date permissions
        List<PolicyPermission> clonePolicyPermissions = new ArrayList<>(policy.getPolicyPermissions());

        if (!CollectionUtils.isEmpty(clonePolicyPermissions)) {
            if (!CollectionUtils.isEmpty(permissionDTOs)) {
                Map<String, PermissionDTO> permissionDTOMap = permissionDTOs.stream()
                        .collect(Collectors.toMap(PermissionDTO::getPermission, permissionDTO -> permissionDTO));

                // Filter permissions is already exist and not out of date
                int index = 0;
                while (index < clonePolicyPermissions.size()) {
                    String policyPermissionKey = clonePolicyPermissions.get(index).getPermission();
                    if (permissionDTOMap.containsKey(policyPermissionKey)) {
                        // In this case, the permission is already exist, so we not need add/delete these permissions
                        permissionDTOMap.remove(policyPermissionKey);
                        clonePolicyPermissions.remove(index);
                    } else {
                        index++;
                    }
                }
                // Delete the old permissions, that is not exist in application
                this.forfeitPermissionForPolicy(clonePolicyPermissions);
                // Remove permission is deleted in list permission of full access policy
                policy.getPolicyPermissions().removeAll(clonePolicyPermissions);

                // Add new permission
                List<PolicyPermission> newPolicyPermission = this.grantPermissionForPolicy(policy, permissionDTOMap.values());
                // Add new permission is saved successfully to policyFullAccess.
                if (!CollectionUtils.isEmpty(newPolicyPermission)) {
                    policy.getPolicyPermissions().addAll(newPolicyPermission);
                }
            } else {
                // Forfeit all permission because app don't have any permission (list permission dto is empty)
                this.forfeitPermissionForPolicy(clonePolicyPermissions);
            }
        } else {
            // Granting new permissions for full access policy if it's not have any permission before
            this.grantPermissionForPolicy(policy, permissionDTOs);
        }
    }
}
