package com.financial.alireza.transactions.dto;

import com.financial.alireza.transactions.model.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import com.financial.alireza.transactions.validation.ValidTransactionRequest;

@ValidTransactionRequest
public record CreateTransactionRequest(
        @NotNull
        TransactionType transactionType,
        @Pattern(regexp = "[0-9]{14}", message = "account number length must be 14")
        String source,
        @Pattern(regexp = "[0-9]{14}", message = "account number length must be 14")
        String destination,
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be greater than zero")
        Long amount
) {
}
