package org.example.authorize.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdPrefixValue;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Role entity.
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@IdPrefixValue("ROL")
public class Role extends Audit<String> {

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
