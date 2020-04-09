package org.example.authorize.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class PolicyCondition extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @NotNull
    @Column(columnDefinition = "text", nullable = false)
    private String operator;

    @Column(columnDefinition = "text")
    private String conditionValue;
}
