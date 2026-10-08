package com.financial.alireza.accounts.dto;

import com.financial.alireza.accounts.model.CustomerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateAccountRequest(@NotBlank String name,
                                   @NotBlank String nationalId,
                                   @NotNull LocalDate birth,
                                   @NotNull CustomerType customerType,
                                   @NotBlank String phoneNumber,
                                   @NotBlank String address,
                                   @NotBlank String postalCode) {
}

