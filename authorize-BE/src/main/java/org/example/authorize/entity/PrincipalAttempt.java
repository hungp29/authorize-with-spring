package org.example.authorize.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdPrefixValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Principal Attempt.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@IdPrefixValue("PRA")
public class PrincipalAttempt extends Audit<String> {

    @OneToOne
    @JoinColumn(name = "principal_id", nullable = false)
    private Principal principal;

    @Column
    private short attemptCount;
}
