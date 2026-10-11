package com.financial.alireza.transactions.config;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@ConfigurationProperties(prefix = "app.transfer-fee")
public record TransferFeeProperties(
        @NotNull
        @DecimalMin("0")
        BigDecimal rate,

        @NotNull
        @PositiveOrZero
        Long minimum,

        @NotNull
        @PositiveOrZero
        Long maximum,

        @NotNull
        @Pattern(regexp = "[0-9]{14}")
        String bankAccountNumber
) {
    @AssertTrue(message = "Minimum fee must not exceed maximum fee")
    public boolean isRangeValid() {
        return minimum == null
                || maximum == null
                || minimum <= maximum;
    }
}