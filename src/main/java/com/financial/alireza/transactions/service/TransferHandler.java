package com.financial.alireza.transactions.service;

import com.financial.alireza.transactions.repository.TransactionRepository;

public class TransferHandler extends TransactionHandlerAbstract{
    public TransferHandler(TransactionRepository transactionRepository) {
        super(transactionRepository);
    }

    @Override
    public void executeTransaction() {

    }
}
