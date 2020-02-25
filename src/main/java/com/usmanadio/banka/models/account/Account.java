package com.usmanadio.banka.models.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String accountNumber;

    @NotNull
    @NotBlank
    private AccountType accountType;

    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    private Double accountBalance = 0.00;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "accountId")
    private List<DomiciliaryAccount> domiciliaryAccounts;

    public void addDomiciliaryAccounts(DomiciliaryAccount domiciliaryAccount) {
        if (domiciliaryAccounts == null) {
            domiciliaryAccounts = new ArrayList<>();
        }
        domiciliaryAccounts.add(domiciliaryAccount);
    }
}
