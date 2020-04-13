package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
public class Policy extends Audit<String> {

    @Id
    @Size(max = 32)
    private String id;

    @Size(max = 255)
    @Column(nullable = false)
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
