package org.example.authorize.app.permission;

import org.example.authorize.rbac.ConditionPrototype;
import org.example.authorize.security.permission.PermissionCondition;
import org.example.authorize.security.permission.PermissionConditions;
import org.example.authorize.security.permission.PermissionGroup;
import org.example.authorize.utils.PermissionUtils;
import org.example.authorize.utils.SecurityUtils;
import org.example.authorize.utils.constants.Constants;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final RequestMappingHandlerMapping handlerMapping;

    public PermissionService(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    /**
     * Get all endpoint.
     *
     * @return list endpoint
     */
    public List<PermissionDTO> getAllPermission() {
        List<PermissionDTO> permissions = new ArrayList<>();
        // For each endpoint
        handlerMapping.getHandlerMethods().forEach((requestMappingInfo, handlerMethod) -> {
            // path and method
            List<String> patterns = new ArrayList<>(requestMappingInfo.getPatternsCondition().getPatterns());
            List<RequestMethod> methods = new ArrayList<>(requestMappingInfo.getMethodsCondition().getMethods());
            if (patterns.size() > 0 && methods.size() > 0) {
                for (int i = 0; i < patterns.size(); i++) {
                    PermissionDTO permissionDTO = getPermission(patterns.get(i), methods.get(i), handlerMethod);
                    if (null != permissionDTO) {
                        permissions.add(permissionDTO);
                    }
                }
            }
        });

        return permissions.stream()
                .sorted(Comparator.comparing(PermissionDTO::getPermissionId))
                .collect(Collectors.toList());
    }

    private PermissionDTO getPermission(String endpoint, RequestMethod requestMethod, HandlerMethod handlerMethod) {
        PermissionDTO permissionDTO = null;
        if (!SecurityUtils.isMatchWhiteList(requestMethod, endpoint)) {
            // Get permission group and conditions
            PermissionGroup permissionGroupClass = handlerMethod.getBeanType().getAnnotation(PermissionGroup.class);
            PermissionGroup permissionGroupMethod = handlerMethod.getMethod().getAnnotation(PermissionGroup.class);
            PermissionConditions permissionConditions = handlerMethod.getMethod().getAnnotation(PermissionConditions.class);

            permissionDTO = new PermissionDTO();
            permissionDTO.setPermissionId(requestMethod.name() + Constants.COLON + endpoint);

            // Set permission group
            if (null != permissionGroupMethod) {
                permissionDTO.setPermissionGroup(permissionGroupMethod.value());
            } else if (null != permissionGroupClass) {
                permissionDTO.setPermissionGroup(permissionGroupClass.value());
            }

            // Set permission type
            permissionDTO.setPermissionType(PermissionUtils.getDefaultPermissionType(requestMethod));

            // Set permission condition information
            if (null != permissionConditions) {
                // Set permission name
                permissionDTO.setPermissionName(permissionConditions.value());

                // Overwrite permission type if it's determined
                if (!StringUtils.isEmpty(permissionConditions.type())) {
                    permissionDTO.setPermissionType(permissionConditions.type());
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


}
