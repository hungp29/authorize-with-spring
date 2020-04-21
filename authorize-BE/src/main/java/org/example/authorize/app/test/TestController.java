package org.example.authorize.app.test;

import org.example.authorize.rbac.condition.OwnedResourceCondition;
import org.example.authorize.rbac.resolver.TestResolver;
import org.example.authorize.security.permission.PermissionCondition;
import org.example.authorize.security.permission.PermissionConditions;
import org.example.authorize.security.permission.PermissionGroup;
import org.example.authorize.utils.constants.PermissionGroupConstants;
import org.example.authorize.utils.generator.id.Generator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PermissionGroup(PermissionGroupConstants.TEST)
public class TestController {

    private final Generator<String> generator;

    public TestController(Generator<String> generator) {
        this.generator = generator;
    }

    @PermissionConditions(value = "Test method", conditions = {
            @PermissionCondition(condition = OwnedResourceCondition.class, resolver = TestResolver.class)
    })
    @GetMapping("/test")
    public String test() {
        return generator.generate();
    }
}
