package com.financial.alireza.accounts.dto;

import com.financial.alireza.accounts.model.CustomerType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreateAccountRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name is too long")
        String name,

        @NotBlank(message = "National ID is required")
        @Pattern(
                regexp = "[0-9]{10}",
                message = "National ID must be 10 digits"
        )
        String nationalId,

        @NotNull(message = "Birth date is required")
        @PastOrPresent(message = "Birth date cannot be in the future")
        LocalDate birth,

        @NotNull(message = "Customer type is required")
        CustomerType customerType,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "09[0-9]{9}",
                message = "Phone number must be 11 digits starting with 09"
        )
        String phoneNumber,

        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address is too long")
        String address,

        @NotBlank(message = "Postal code is required")
        @Pattern(
                regexp = "[0-9]{10}",
                message = "Postal code must be 10 digits"
        )
        String postalCode
) {
}