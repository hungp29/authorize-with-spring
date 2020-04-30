package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Role entity.
 */
@Entity
@Data
public class Role extends Audit<String> {

    @Id
    @IdGenerator("ROL")
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(
            name = "id_generator",
            strategy = "org.example.authorize.utils.generator.id.StringIdentifierGenerator"
    )
    @Size(max = 35)
    private String id;

    @Size(max = 255)
    @Column(nullable = false)
    private String name;

    @Column
    private boolean systemRole;

    @Column
    private boolean readOnly;

    @ManyToMany(mappedBy = "roles")
    private List<Principal> principals;

    @ManyToMany(mappedBy = "roles")
    private List<Policy> policies;
}
