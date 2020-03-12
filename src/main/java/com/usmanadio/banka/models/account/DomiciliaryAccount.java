package com.usmanadio.banka.models.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.usmanadio.banka.models.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "domiciliaryAccounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DomiciliaryAccount extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    private DomiciliaryAccountType domiciliaryAccountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountNumber", referencedColumnName = "accountNumber", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Account account;
}
