package com.usmanadio.banka.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewPasswordRequest {

    @NotNull
    @NotBlank
    private String newPassword;
}
