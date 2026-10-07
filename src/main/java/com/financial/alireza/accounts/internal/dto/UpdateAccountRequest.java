package com.financial.alireza.accounts.internal.dto;

import com.financial.alireza.accounts.internal.model.AccountStatus;
import com.financial.alireza.accounts.internal.model.CustomerType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.sql.Time;
import java.time.LocalDate;

public record UpdateAccountRequest( String name,
                                    String nationalId,
                                    CustomerType customerType,
                                    String phoneNumber,
                                    String address,
                                    String postalCode,
                                    LocalDate birth,
                                    AccountStatus accountStatus) {
}
