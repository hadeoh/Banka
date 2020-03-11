package com.usmanadio.banka.dto.account;

import com.usmanadio.banka.models.account.AccountType;
import com.usmanadio.banka.models.account.DomiciliaryAccountType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AccountRequest {

    @NotNull
    private AccountType accountType;

    private DomiciliaryAccountType domiciliaryAccountType;
}
