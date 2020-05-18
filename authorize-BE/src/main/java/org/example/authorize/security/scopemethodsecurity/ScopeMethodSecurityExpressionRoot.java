package org.example.authorize.security.scopemethodsecurity;

import org.aopalliance.intercept.MethodInvocation;
import org.example.authorize.exception.AccessConditionException;
import org.example.authorize.security.rbac.AccessCondition;
import org.example.authorize.security.rbac.RequestValueResolver;
import org.example.authorize.security.BGrantedAuthority;
import org.example.authorize.security.UserPrincipal;
import org.example.authorize.security.permission.PermissionCondition;
import org.example.authorize.security.permission.PermissionConditions;
import org.example.authorize.utils.ObjectUtils;
import org.example.authorize.utils.constants.Constants;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Scope method security expression root.
 */
public class ScopeMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private static final String KEY_PATH = "PATH";
    private static final String KEY_REQUEST_METHOD = "REQUEST_METHOD";

    private Object filterObject;
    private Object returnObject;
    private final List<Class<? extends Annotation>> supportRequestMappingClasses;

    private final List<AccessCondition<?>> accessConditions;
    private final String needPermission;

    public ScopeMethodSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        super(authentication);
        supportRequestMappingClasses = getListRequestMappingSupport();
        needPermission = buildNeedPermissionFromMethod(invocation);
        accessConditions = newInstanceAccessConditions(invocation);
    }

    /**
     * New instance access conditions.
     *
     * @param invocation MethodInvocation instance, it have PermissionConditions annotation
     * @return list AccessCondition
     */
    private List<AccessCondition<?>> newInstanceAccessConditions(MethodInvocation invocation) {
        List<AccessCondition<?>> accessConditions = new ArrayList<>();
        PermissionConditions permissionConditions = ObjectUtils.getAnnotation(invocation.getMethod(), PermissionConditions.class);
        if (null != permissionConditions) {
            try {
                PermissionCondition[] conditions = permissionConditions.conditions();
                for (PermissionCondition condition : conditions) {
                    // Create new instance for RequestValueResolver
                    Class<? extends RequestValueResolver<?>> resolverClass = condition.resolver();
                    Constructor<? extends RequestValueResolver<?>> resolverConstructor = resolverClass.getConstructor(Authentication.class, Object[].class);
                    RequestValueResolver<?> requestValueResolver = resolverConstructor.newInstance(authentication, invocation.getArguments());

                    // Create new instance for AccessCondition
                    Class<? extends AccessCondition<?>> conditionClass = condition.condition();
                    Constructor<? extends AccessCondition<?>> conditionConstructor = conditionClass.getConstructor(RequestValueResolver.class);
                    AccessCondition<?> accessCondition = conditionConstructor.newInstance(requestValueResolver);

                    // Add new instance of AccessCondition is created above to list
                    accessConditions.add(accessCondition);
                }
            } catch (NoSuchMethodException e) {
                throw new AccessConditionException("Cannot get Constructor of AccessCondition or RequestValueResolver class", e);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                throw new AccessConditionException("Error occurs while creating new instance of AccessCondition or RequestValueResolver", e);
            }
        }
        return accessConditions;
    }

    /**
     * Build permission id from API method.
     *
     * @param invocation MethodInvocation instance, it store Method data
     * @return return permission id
     */
    private String buildNeedPermissionFromMethod(MethodInvocation invocation) {
        String permission = null;
        Method method = invocation.getMethod();
        // Only processing if Class is Controller or RestController
        if (ObjectUtils.hasAnnotation(method.getDeclaringClass(), RestController.class) ||
                ObjectUtils.hasAnnotation(method.getDeclaringClass(), Controller.class)) {
            // Get Annotation RequestMapping of Controller to get URL Controller
            RequestMapping requestMapping = ObjectUtils.getAnnotation(method.getDeclaringClass(), RequestMapping.class);

            if (null != requestMapping) {
                // Get URL of Controller from function value of RequestMapping
                String[] urlsController = requestMapping.value();
                // Get path and method of function of API
                Map<String, String> pathAndRequestMethod = getPathAndRequestMethodOfAPIMethod(method);

                // Build permission id: <RequestMethod>:<URL Controller><URL API>
                // Ex:                  GET:/api/v1/test
                if (urlsController.length == 1 && !CollectionUtils.isEmpty(pathAndRequestMethod)) {
                    permission = pathAndRequestMethod.get(KEY_REQUEST_METHOD) + Constants.COLON + urlsController[0] + pathAndRequestMethod.get(KEY_PATH);
                }
            }
        }
        return permission;
    }

    /**
     * Get path and request method of API.
     *
     * @param apiMethod the method of API
     * @return return Map object have path and RequestMethod of API
     */
    private Map<String, String> getPathAndRequestMethodOfAPIMethod(Method apiMethod) {
        if (!CollectionUtils.isEmpty(supportRequestMappingClasses)) {
            // Get all annotation of API method
            Annotation[] annotations = apiMethod.getDeclaredAnnotations();
            // Loop all above annotation
            for (Annotation annotation : annotations) {
                // Loop all support request mapping: GetMapping, PostMapping, ...
                for (Class<? extends Annotation> supportClass : supportRequestMappingClasses) {
                    // Get first annotation meet the condition
                    if (supportClass.isAssignableFrom(annotation.getClass())) {
                        Map<String, String> requestMap = new HashMap<>();
                        // Cast annotation to GetMapping or PostMapping, ... (annotations is supported)
                        Object httpMapping = supportClass.cast(annotation);
                        // Get paths from request mapping
                        String[] paths = ObjectUtils.getValueOfFieldByMethod(httpMapping, "value", String[].class);
                        // Get RequestMethod from request mapping
                        RequestMethod[] requestMethods = null;
                        RequestMapping requestMapping = ObjectUtils.getAnnotation(httpMapping, RequestMapping.class);
                        if (null != requestMapping) {
                            requestMethods = requestMapping.method();
                        }

                        // If both path and RequestMethod have one value, create Map object to store path and request method,
                        // otherwise continue loop
                        if (null != paths && paths.length == 1 && null != requestMethods && requestMethods.length == 1) {
                            requestMap.put(KEY_PATH, paths[0]);
                            requestMap.put(KEY_REQUEST_METHOD, requestMethods[0].toString());
                            return requestMap;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get List Request Mapping Support.
     *
     * @return list request mapping support (PostMapping, GetMapping, ...)
     */
    private List<Class<? extends Annotation>> getListRequestMappingSupport() {
        List<Class<? extends Annotation>> supportClasses = new ArrayList<>();
        supportClasses.add(DeleteMapping.class);
        supportClasses.add(GetMapping.class);
        supportClasses.add(PatchMapping.class);
        supportClasses.add(PostMapping.class);
        supportClasses.add(PutMapping.class);
        return supportClasses;
    }

    /**
     * Check permission and conditions for API.
     *
     * @return return true if user is granted permission and all condition is matched
     */
    public boolean checkPermission() {
        if (null == authentication) {
            return false;
        }
        if (CollectionUtils.isEmpty(accessConditions)) {
            return true;
        }

        // Get principal of user
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        // If user don't have any authorities then deny access
        if (CollectionUtils.isEmpty(userPrincipal.getAuthorities())) {
            return false;
        }

        // Check permission and condition
        return userPrincipal.getAuthorities().stream().anyMatch(authority -> {
            BGrantedAuthority bGrantedAuthority = (BGrantedAuthority) authority;

            // If user don't have any permission then deny access
            if (CollectionUtils.isEmpty(bGrantedAuthority.getPolicyPermissions())) {
                return false;
            }
            // Check user have permission to access this API
            List<BGrantedAuthority.PolicyPermission> policyPermissions = bGrantedAuthority.getPolicyPermissions()
                    .stream()
                    .filter(policyPermission -> policyPermission.getPermission().equals(needPermission))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(policyPermissions)) {
                return false;
            }

            // Get policy id to filter condition belong to this id
            List<String> policyIds = policyPermissions.stream().map(BGrantedAuthority.PolicyPermission::getPolicyId)
                    .distinct()
                    .collect(Collectors.toList());

            return policyIds.stream().anyMatch(policyId -> {
                // Get list condition for policies
                List<BGrantedAuthority.PolicyCondition> policyConditions = bGrantedAuthority.getPolicyConditions()
                        .stream()
                        .filter(policyCondition -> policyCondition.getPolicyId().equals(policyId))
                        .collect(Collectors.toList());

                // Accept access when API don't determine any condition
                if (CollectionUtils.isEmpty(policyConditions) || CollectionUtils.isEmpty(accessConditions)) {
                    return true;
                }

                // Get list operators from conditions
                List<String> operators = policyConditions.stream().map(BGrantedAuthority.PolicyCondition::getOperator)
                        .collect(Collectors.toList());

                // Filter condition need check of policy
                List<AccessCondition> needCheckConditions = accessConditions.stream()
                        .filter(accessCondition -> operators.contains(accessCondition.getConditionPrototype().getOperatorName()))
                        .collect(Collectors.toList());

                // If don't have any condition need to check then accept access
                if (CollectionUtils.isEmpty(needCheckConditions)) {
                    return true;
                }

                // All condition have to valid then user can be access API
                return needCheckConditions.stream().allMatch(accessCondition -> accessCondition.validate(accessCondition.getRequestedValueResolver().resolve()));
            });
        });
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    public List<Class<? extends Annotation>> getSupportRequestMappingClasses() {
        return supportRequestMappingClasses;
    }

    @Override
    public Object getThis() {
        return this;
    }
}
