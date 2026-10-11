package com.financial.alireza.security;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@ConfigurationProperties(prefix = "app.security")
public record OperatorProperties(
        @NotEmpty List<@Valid Operator> operators
) {

    public record Operator(
            @NotBlank String username,
            @NotBlank String passwordHash
    ) {
    }
}