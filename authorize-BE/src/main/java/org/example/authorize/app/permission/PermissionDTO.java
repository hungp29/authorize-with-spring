package org.example.authorize.app.permission;

import lombok.Data;
import org.example.authorize.rbac.ConditionPrototype;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionDTO {

    private String permissionId;

    private String permissionGroup;

    private String permissionName;

    private String permissionType;

    private List<ConditionPrototype> conditions = new ArrayList<>();

}
