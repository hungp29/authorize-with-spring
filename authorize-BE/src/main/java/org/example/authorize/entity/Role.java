package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Role entity.
 */
@Entity
@Data
public class Role extends Audit<String> {

    @Id
    @Size(max = 32)
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
