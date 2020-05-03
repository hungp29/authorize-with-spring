package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;
import org.example.authorize.entity.common.IdGenerator;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Account entity.
 */
@Entity
@Data
public class Account extends Audit<String> {

    @Id
    @IdGenerator("ACC")
    @GeneratedValue(generator = "id_generator")
    @GenericGenerator(
            name = "id_generator",
            strategy = "org.example.authorize.utils.generator.id.StringIdentifierGenerator"
    )
    @Size(max = 35)
    private String id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "principal_id", nullable = false)
    private Principal principal;

    @Size(max = 255)
    @Column
    private String username;

    @Size(max = 255)
    @Column
    private String firstName;

    @Size(max = 255)
    @Column
    private String lastName;

    @Size(max = 255)
    @Column
    private String email;

    @Size(max = 255)
    @Column
    private String title;

    @Column
    private LocalDate dateOfBirth;

    @Column(columnDefinition = "text")
    private String avatarUrl;

    @Column(columnDefinition = "text")
    private String houseAddress;

    @Column(columnDefinition = "text")
    private String workAddress;

    @Size(max = 20)
    @Column
    private String phoneNumber;

    @Column(columnDefinition = "text")
    private String notificationToken;

    @Size(max = 2)
    @Column
    private String language;
}
