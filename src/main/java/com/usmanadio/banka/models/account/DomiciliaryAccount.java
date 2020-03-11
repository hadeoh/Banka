package com.usmanadio.banka.models.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.usmanadio.banka.models.AuditModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "accountNumber", referencedColumnName = "accountNumber", nullable = false)
    @JsonIgnore
    private Account account;
}
