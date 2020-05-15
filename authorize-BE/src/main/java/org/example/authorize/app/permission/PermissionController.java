package org.example.authorize.app.permission;

import lombok.RequiredArgsConstructor;
import org.example.authorize.response.WResponseEntity;
import org.example.authorize.version.APIVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Permission Controller
 */
@APIVersion("1.0")
@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public WResponseEntity<List<PermissionDTO>> getPermissions() {
        return WResponseEntity.success(permissionService.getPermissions());
    }
}
