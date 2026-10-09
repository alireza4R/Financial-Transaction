package com.financial.alireza.transactions.repository;

import com.financial.alireza.transactions.model.TransactionRequestEntity;

import java.util.Optional;

public interface PendingTransactionRepository {

    Optional<TransactionRequestEntity> findAndLockNextPending();
}