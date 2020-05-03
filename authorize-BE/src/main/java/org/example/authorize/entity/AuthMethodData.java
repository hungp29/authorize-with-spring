package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Auth Method Data.
 */
@Entity
@Data
public class AuthMethodData extends Audit<String> {

    @Id
    @IdGenerator("AMD")
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(
            name = "id_generator",
            strategy = "org.example.authorize.utils.generator.id.StringIdentifierGenerator"
    )
    @Size(max = 35)
    private String id;

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
