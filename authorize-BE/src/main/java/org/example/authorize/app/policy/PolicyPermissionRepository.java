package org.example.authorize.app.policy;

import org.example.authorize.entity.PolicyPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyPermissionRepository extends JpaRepository<PolicyPermission, String> {
}
