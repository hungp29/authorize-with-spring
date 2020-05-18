package org.example.authorize.app.test;

import org.example.authorize.component.aspect.trackingparam.LogArgument;
import org.example.authorize.component.aspect.trackingparam.LogReturning;
import org.example.authorize.component.version.APIVersion;
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
public class TestController {

    @LogArgument
    @LogReturning
    @PermissionConditions(value = "Test API", conditions = {
            @PermissionCondition(condition = OwnedResourceCondition.class, resolver = TestResolver.class)
    })
    @GetMapping(URLConstants.M_TEST)
    public WResponseEntity<String> test(@PathVariable int value) {
        return WResponseEntity.success("This is test API " + value);
    }
}
