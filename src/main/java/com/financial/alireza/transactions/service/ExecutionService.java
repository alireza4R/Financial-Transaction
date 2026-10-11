package com.financial.alireza.transactions.service;

import com.financial.alireza.transactions.exception.TransactionFailedException;
import com.financial.alireza.transactions.model.TransactionRequestEntity;
import com.financial.alireza.transactions.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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

        log.info(
                "Transaction handled, awaiting commit: trackingId={}, status={}",
                request.getId(),
                request.getStatus()
        );

        return true;
    }


}
