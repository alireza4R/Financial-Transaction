package com.financial.alireza.transactions.service;

import com.financial.alireza.transactions.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class WithdrawHandler extends TransactionHandlerAbstract {
    protected WithdrawHandler(TransactionRepository transactionRepository) {
        super(transactionRepository);
    }

    @Override
    public void executeTransaction() {

    }
}
