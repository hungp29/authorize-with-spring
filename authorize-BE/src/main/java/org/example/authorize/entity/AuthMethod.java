package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdGenerator;
import org.example.authorize.enums.AuthType;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Auth method entity.
 */
@Entity
@Data
public class AuthMethod extends Audit<String>  {

    @Id
    @IdGenerator("AUM")
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(
            name = "id_generator",
            strategy = "org.example.authorize.utils.generator.id.StringIdentifierGenerator"
    )
    @Size(max = 35)
    private String id;

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
