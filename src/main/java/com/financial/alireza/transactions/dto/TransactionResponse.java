package com.financial.alireza.transactions.dto;

import com.financial.alireza.transactions.model.TransactionType;

import java.time.Instant;

public record TransactionResponse(
        TransactionType transactionType,
        String source,
        String destination,
        Long amount,
        Instant completeAt,
        Long trackingNumber
) {
}
