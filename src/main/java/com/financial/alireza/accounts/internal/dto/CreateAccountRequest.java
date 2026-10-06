package com.financial.alireza.accounts.internal.dto;

import com.financial.alireza.accounts.internal.model.CustomerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.sql.Time;
import java.time.LocalDate;

public record CreateAccountRequest(@NotBlank String name,
                                   @NotBlank String nationalId,
                                   @NotNull LocalDate birth,
                                   @NotNull CustomerType customerType,
                                   @NotBlank String phoneNumber,
                                   @NotBlank String address,
                                   @NotEmpty String postalCode) {
}

