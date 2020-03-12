package com.usmanadio.banka.dto.account;

import com.usmanadio.banka.models.account.AccountStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AccountStatusRequest {

    @NotNull
    @NotBlank
    private AccountStatus accountStatus;
}
