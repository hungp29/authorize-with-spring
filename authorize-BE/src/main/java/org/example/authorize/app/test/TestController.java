package org.example.authorize.app.test;

import org.example.authorize.rbac.condition.OwnedResourceCondition;
import org.example.authorize.rbac.resolver.TestResolver;
import org.example.authorize.response.WResponseEntity;
import org.example.authorize.security.permission.PermissionCondition;
import org.example.authorize.security.permission.PermissionConditions;
import org.example.authorize.security.permission.PermissionGroup;
import org.example.authorize.utils.constants.PermissionGroupConstants;
import org.example.authorize.utils.constants.URLConstants;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PermissionGroup(PermissionGroupConstants.TEST)
@RequestMapping(URLConstants.C_TEST)
public class TestController {

    @PermissionConditions(value = "Test API", conditions = {
            @PermissionCondition(condition = OwnedResourceCondition.class, resolver = TestResolver.class)
    })
//    @PreAuthorize("hasRole('ROLE_VIEWER')")
    @GetMapping(URLConstants.M_TEST)
    public WResponseEntity<String> test() {
        return WResponseEntity.success("This is test API");
    }
}
