package org.example.authorize.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
public class Account extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "principal_id", nullable = false)
    private Principal principal;

    @Size(max = 255)
    @NotNull
    @Column(nullable = false)
    private String firstName;

    @Size(max = 255)
    @NotNull
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
