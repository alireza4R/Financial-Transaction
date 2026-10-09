package com.financial.alireza.transactions.service;

import com.financial.alireza.transactions.repository.TransactionRepository;

public class DepositHandler extends TransactionHandlerAbstract{

    public DepositHandler(TransactionRepository transactionRepository) {
        super(transactionRepository);
    }

    @Override
    public void executeTransaction() {

    }
}
