package org.example.authorize.app.test;

import lombok.RequiredArgsConstructor;
import org.example.authorize.app.policy.PolicyService;
import org.example.authorize.component.aspect.trackingparam.LogArgument;
import org.example.authorize.component.aspect.trackingparam.LogReturning;
import org.example.authorize.component.version.APIVersion;
import org.example.authorize.entity.Policy;
import org.example.authorize.response.WResponseEntity;
import org.example.authorize.security.permission.PermissionCondition;
import org.example.authorize.security.permission.PermissionConditions;
import org.example.authorize.security.permission.PermissionGroup;
import org.example.authorize.security.rbac.condition.OwnedResourceCondition;
import org.example.authorize.security.rbac.resolver.TestResolver;
import org.example.authorize.utils.constants.PermissionGroupConstants;
import org.example.authorize.utils.constants.URLConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@APIVersion("1.0")
@RestController
@PermissionGroup(PermissionGroupConstants.TEST)
@RequestMapping(URLConstants.C_TEST)
@RequiredArgsConstructor
public class TestController {

    private final PolicyService policyService;

    @LogArgument
    @LogReturning
    @PermissionConditions(value = "Test API", conditions = {
            @PermissionCondition(condition = OwnedResourceCondition.class, resolver = TestResolver.class)
    })
    @GetMapping(URLConstants.M_TEST)
    public WResponseEntity<String> test(@PathVariable String value) {
        Policy policy = policyService.findPolicyByName(value).orElse(null);
        String id = null != policy ? policy.getId() : "";
        return WResponseEntity.success("Policy Id " + id);
    }
}
