package com.usmanadio.banka.models.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.usmanadio.banka.models.AuditModel;
import com.usmanadio.banka.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String accountNumber;

    @NotNull
    private AccountType accountType;

    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    private Double accountBalance = 0.00;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false)
    @JsonIgnore
    private User user;
}
