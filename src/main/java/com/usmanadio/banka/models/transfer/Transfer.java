package com.usmanadio.banka.models.transfer;

import com.usmanadio.banka.models.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "transfers")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transfer {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @NotNull
    @NotBlank
    private String fromAccountNumber;

    @NotNull
    @NotBlank
    private String toAccountNumber;

    @NotNull
    @NotBlank
    private Double amount;

    @CreationTimestamp
    private Timestamp createdAt;



    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "transactionId")
    private Transaction transaction;
}
