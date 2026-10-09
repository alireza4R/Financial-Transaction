package com.financial.alireza.transactions.service;

import com.financial.alireza.transactions.exception.TransactionFailedException;
import com.financial.alireza.transactions.model.TransactionRequestEntity;
import com.financial.alireza.transactions.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExecutionService {
    private final TransactionRepository transactionRepository;
    private final DepositHandler depositHandler;
    private final WithdrawHandler withdrawHandler;
    private final TransferHandler transferHandler;

    @Transactional
    public boolean processNextRequest() {

        var result = transactionRepository.findAndLockNextPending();

        if (result.isEmpty()) {
            return false;
        }

        TransactionRequestEntity request = result.get();

        TransactionHandlerAbstract handler =
                switch (request.getTransactionType()){
                    case DEPOSIT -> {
                        yield depositHandler;
                    }
                    case WITHDRAW -> {
                        yield withdrawHandler;
                    }
                    case TRANSFER -> {
                        yield  transferHandler;
                    }
                    case FEE -> throw new TransactionFailedException("Transaction not allowed");
                };

        handler.lunchTransaction(request);

        return true;
    }


}
