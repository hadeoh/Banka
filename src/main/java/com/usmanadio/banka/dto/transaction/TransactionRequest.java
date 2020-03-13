package com.usmanadio.banka.dto.transaction;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TransactionRequest {

    @NotNull
    private Double amount;
}
