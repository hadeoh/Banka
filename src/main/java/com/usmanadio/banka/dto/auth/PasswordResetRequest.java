package com.usmanadio.banka.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PasswordResetRequest {

    @NotBlank
    @NotNull
    private String email;
}
