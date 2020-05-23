package org.example.authorize.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdPrefixValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Policy Condition entity.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@IdPrefixValue("POC")
public class PolicyCondition extends Audit<String> {

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @NotNull
    @Column(columnDefinition = "text", nullable = false)
    private String operator;

    @Column(columnDefinition = "text")
    private String conditionValue;
}
