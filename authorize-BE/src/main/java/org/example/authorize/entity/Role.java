package org.example.authorize.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
public class Role extends BaseEntity {

    private String name;

    private boolean systemRole;

    private boolean readOnly;

    @ManyToMany(mappedBy = "roles")
    private List<Principal> principals;
}
