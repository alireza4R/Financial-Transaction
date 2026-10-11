package com.financial.alireza.transactions.dto;


import com.financial.alireza.transactions.model.Direction;
import com.financial.alireza.transactions.model.TransactionType;
import jakarta.validation.constraints.*;

import java.time.Instant;

public record ReportRequest(
        @NotBlank
        @Pattern(regexp = "[0-9]{14}", message = "account number length must be 14")
        String accountNumber,
        @Pattern(regexp = "[0-9]{14}", message = "account number length must be 14")
        String counterpartyAccount,
        TransactionType transactionType,
        Direction direction,
        @PositiveOrZero
        Long maxAmount,
        @PositiveOrZero
        Long minAmount,
        Instant from,
        Instant to


) {
    @AssertTrue(message = "Minimum amount must not exceed maximum amount")
    public boolean isAmountRangeValid() {
        return minAmount == null
                || maxAmount == null
                || minAmount <= maxAmount;
    }

    @AssertTrue(message = "Start time must be before end time")
    public boolean isDateRangeValid() {
        return from == null
                || to == null
                || from.isBefore(to);
    }
}
