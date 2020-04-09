package org.example.authorize.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class Principal extends BaseEntity {

    @NotNull
    @Column(nullable = false)
    private boolean disabled;

    @NotNull
    @Column(nullable = false)
    private boolean deleted;

    @OneToOne(mappedBy = "principal")
    private Account account;

    @ManyToMany
    @JoinTable(
            name = "principal_role",
            joinColumns = @JoinColumn(name = "principal_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
