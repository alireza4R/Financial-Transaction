package com.financial.alireza.transactions.service;

import com.financial.alireza.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract  class  TransactionHandlerAbstract {
    private final TransactionRepository transactionRepository;
    public abstract void executeTransaction();
}
