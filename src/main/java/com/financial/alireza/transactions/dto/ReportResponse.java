package com.financial.alireza.transactions.dto;

import com.financial.alireza.transactions.model.TransactionRequestEntity;

import java.util.List;

public record ReportResponse(List<TransactionResponse> requestEntities,
                             int page,
                             int limit,
                             long total,
                             int totalPages) {
}
