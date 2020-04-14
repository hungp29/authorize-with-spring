package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.enums.AuthType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
public class AuthMethod extends Audit<String> {

    @Id
    @Size(max = 32)
    private String id;

    @ManyToOne
    @JoinColumn(name = "principal_id", nullable = false)
    private Principal principal;

    @NotNull
    @Column(columnDefinition = "enum('USERNAME_PASSWORD', 'EMAIL_PASSWORD')", nullable = false)
    private AuthType authType;

    @Column(name = "auth_data_1", columnDefinition = "text")
    private String authData1;

    @Column(name = "auth_data_2", columnDefinition = "text")
    private String authData2;

    @Column(name = "auth_data_3", columnDefinition = "text")
    private String authData3;
}
