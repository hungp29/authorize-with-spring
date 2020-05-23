package org.example.authorize.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdPrefixValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Auth Method Data.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@IdPrefixValue("AMD")
public class AuthMethodData extends Audit<String> {

    @Column(name = "auth_data_1", columnDefinition = "text")
    private String authData1;

    @Column(name = "auth_data_2", columnDefinition = "text")
    private String authData2;

    @Column(name = "auth_data_3", columnDefinition = "text")
    private String authData3;

    @Column
    private LocalDateTime expireDate;

    @OneToMany(mappedBy = "authMethodData")
    private List<AuthMethod> authMethods;
}
