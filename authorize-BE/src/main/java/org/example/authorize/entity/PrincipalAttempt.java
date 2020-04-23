package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;

import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Principal Attempt.
 */
@Entity
@Data
public class PrincipalAttempt extends Audit<String> {

    @Id
    @Size(max = 32)
    private String id;

    @OneToOne
    @JoinColumn(name = "principal_id", nullable = false)
    private Principal principal;

    @Column
    private short attemptCount;
}
