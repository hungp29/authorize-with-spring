package org.example.authorize.app.permission;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
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
//                    PermissionDTO permissionDTO = getPermission(patterns.get(i), methods.get(i), handlerMethod);
//                    if (null != permissionDTO) {
//                        permissions.add(permissionDTO);
//                    }
                }
            }
        });

//        return permissions.stream().
//                sorted(Comparator.comparing(PermissionDTO::getPermissionId)).
//                collect(Collectors.toList());
        return null;
    }
}
