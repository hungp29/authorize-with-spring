package org.example.authorize.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Policy extends BaseEntity {

    @Column
    private String policyName;

    @Column
    private boolean readOnly;

    @OneToMany(mappedBy = "policy")
    private List<PolicyPermission> policyPermissions;

    @OneToMany(mappedBy = "policy")
    private List<PolicyCondition> policyConditions;

    @ManyToMany
    @JoinTable(
            name = "role_policy",
            joinColumns = @JoinColumn(name = "policy_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
