package com.usmanadio.banka.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.usmanadio.banka.models.account.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

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

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "userId")
    private Set<Account> accounts;

    public void addAccount(Account account) {
        if (accounts == null) {
            accounts = new HashSet<>();
        }
        accounts.add(account);
    }
}
