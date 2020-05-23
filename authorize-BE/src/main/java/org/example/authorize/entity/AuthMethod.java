package org.example.authorize.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdPrefixValue;
import org.example.authorize.enums.AuthType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Auth method entity.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@IdPrefixValue("AUM")
public class AuthMethod extends Audit<String> {

    @ManyToOne
    @JoinColumn(name = "principal_id", nullable = false)
    private Principal principal;

    @NotNull
    @Column(columnDefinition = "enum('USERNAME_PASSWORD', 'EMAIL_PASSWORD')", nullable = false)
    private AuthType authType;

    @Column(columnDefinition = "text")
    private String determineId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "auth_method_data_id", nullable = false)
    private AuthMethodData authMethodData;

}
