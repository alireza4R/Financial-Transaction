package com.financial.alireza.transactions.dto;

import com.financial.alireza.transactions.model.Status;
import lombok.Builder;

import java.time.Instant;

@Builder
public record TransactionStatusResponse(
        Status status,
        Long transactionNumber,
        Instant completeAt,
        String failureReason
) {}