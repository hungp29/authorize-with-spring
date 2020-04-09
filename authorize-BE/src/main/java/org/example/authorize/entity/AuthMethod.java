package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.enums.AuthType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class AuthMethod extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "principal_id", nullable = false)
    private Principal principal;

    @NotNull
    @Column(columnDefinition = "enum('EMAIL_PASSWORD')", nullable = false)
    private AuthType authType;

    @Column(name = "auth_data_1", columnDefinition = "text")
    private String authData1;

    @Column(name = "auth_data_2", columnDefinition = "text")
    private String authData2;

    @Column(name = "auth_data_3", columnDefinition = "text")
    private String authData3;
}
