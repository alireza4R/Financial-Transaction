package com.financial.alireza.accounts.internal.dto;

import com.financial.alireza.accounts.internal.model.AccountStatus;
import com.financial.alireza.accounts.internal.model.CustomerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.sql.Time;
import java.time.LocalDate;

public record UpdateAccountRequest(@NotBlank String name,
                                   @NotBlank String nationalId,
                                   @NotNull CustomerType customerType,
                                   @NotBlank String phoneNumber,
                                   @NotBlank String address,
                                   @NotBlank String postalCode,
                                   @NotNull LocalDate birth,
                                   @NotNull AccountStatus accountStatus) {
}
