package org.example.authorize.entity;

import lombok.Data;
import org.example.authorize.entity.common.Audit;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
public class Account extends Audit<String> {

    @Id
    @Size(max = 32)
    private String id;

    @OneToOne
    @JoinColumn(name = "principal_id", nullable = false)
    private Principal principal;

    @Size(max = 255)
    @Column(nullable = false)
    private String firstName;

    @Size(max = 255)
    @Column(nullable = false)
    private String lastName;

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