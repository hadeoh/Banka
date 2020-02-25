package com.usmanadio.banka.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.usmanadio.banka.models.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotBlank
    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @NotNull
    private String fullName;

    @NotBlank
    @NotNull
    @JsonIgnore
    private String password;

    @NotBlank
    @NotNull
    private String phoneNumber;

    private String profileImageUrl;

    @ElementCollection(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<Role> roles;

    @JsonIgnore
    private String emailVerificationToken;

    @JsonIgnore
    private EmailVerificationStatus emailVerificationStatus = EmailVerificationStatus.UNVERIFIED;
}
