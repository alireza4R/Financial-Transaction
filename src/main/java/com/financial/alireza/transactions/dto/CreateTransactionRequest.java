package com.financial.alireza.transactions.dto;

import com.financial.alireza.transactions.model.TransactionType;

public record CreateTransactionRequest(
        TransactionType transactionType,
        String source,
        String destination,
        Long amount
) {
}
