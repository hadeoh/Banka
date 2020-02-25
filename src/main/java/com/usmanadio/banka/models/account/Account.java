package com.usmanadio.banka.models.account;

import com.usmanadio.banka.models.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.*;

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
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "accountId")
    private Set<DomiciliaryAccount> domiciliaryAccounts;

    public void addDomiciliaryAccount(DomiciliaryAccount domiciliaryAccount) {
        if (domiciliaryAccounts == null) {
            domiciliaryAccounts = new HashSet<>();
        }
        domiciliaryAccounts.add(domiciliaryAccount);
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "accountNumber")
    private Set<Transaction> transactions;

    public void addTransaction(Transaction transaction) {
        if (transactions == null) {
            transactions = new HashSet<>();
        }
        transactions.add(transaction);
    }
}
