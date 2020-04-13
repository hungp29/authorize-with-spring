package org.example.authorize.app.permission;

import lombok.Data;
import org.example.authorize.rbac.ConditionPrototype;
import org.example.authorize.security.permission.PermissionType;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermissionDTO {

    private String id;

    private String group;

    private String name;

    private PermissionType type;

    private String permission;

    private List<ConditionPrototype> conditions = new ArrayList<>();

}
