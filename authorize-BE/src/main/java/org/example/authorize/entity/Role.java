package org.example.authorize.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
public class Role extends BaseEntity {

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
