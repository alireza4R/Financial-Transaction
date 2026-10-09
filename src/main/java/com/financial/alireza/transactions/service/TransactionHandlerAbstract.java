package com.financial.alireza.transactions.service;

import com.financial.alireza.transactions.model.TransactionRequestEntity;
import com.financial.alireza.transactions.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public abstract  class  TransactionHandlerAbstract {
    protected final TransactionRepository transactionRepository;

    protected TransactionHandlerAbstract(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void executeTransaction(TransactionRequestEntity transactionRequest){

    };


}
