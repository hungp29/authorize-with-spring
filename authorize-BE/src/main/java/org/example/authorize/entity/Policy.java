package org.example.authorize.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdPrefixValue;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Policy entity.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@IdPrefixValue("POL")
public class Policy extends Audit<String> {

    @Size(max = 255)
    @Column(nullable = false)
    private String name;

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
