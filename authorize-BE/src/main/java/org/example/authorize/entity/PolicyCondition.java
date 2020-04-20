package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Policy Condition entity.
 */
@Entity
@Data
public class PolicyCondition extends Audit<String> {

    @Id
    @Size(max = 32)
    private String id;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @NotNull
    @Column(columnDefinition = "text", nullable = false)
    private String operator;

    @Column(columnDefinition = "text")
    private String conditionValue;
}
