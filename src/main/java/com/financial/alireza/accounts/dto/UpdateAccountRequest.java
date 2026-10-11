package com.financial.alireza.accounts.dto;

import com.financial.alireza.accounts.model.AccountStatus;
import com.financial.alireza.accounts.model.CustomerType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record UpdateAccountRequest(
        @Size(max = 255, message = "Name is too long")
        @Pattern(
                regexp = "(?s).*\\S.*",
                message = "Name cannot be blank"
        )
        String name,

        @Pattern(
                regexp = "[0-9]{10}",
                message = "National ID must be 10 digits"
        )
        String nationalId,

        CustomerType customerType,

        @Pattern(
                regexp = "09[0-9]{9}",
                message = "Phone number must be 11 digits starting with 09"
        )
        String phoneNumber,

        @Size(max = 255, message = "Address is too long")
        @Pattern(
                regexp = "(?s).*\\S.*",
                message = "Address cannot be blank"
        )
        String address,

        @Pattern(
                regexp = "[0-9]{10}",
                message = "Postal code must be 10 digits"
        )
        String postalCode,

        @PastOrPresent(message = "Birth date cannot be in the future")
        LocalDate birth,

        AccountStatus accountStatus
) {
}
