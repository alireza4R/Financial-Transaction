package com.financial.alireza.transactions.service;

import com.financial.alireza.accounts.repository.AccountRepository;
import com.financial.alireza.transactions.dto.CreateTransactionRequest;
import com.financial.alireza.transactions.dto.TransactionStatusResponse;
import com.financial.alireza.transactions.exception.TransactionNotFoundException;
import com.financial.alireza.transactions.model.TransactionRequestEntity;
import com.financial.alireza.transactions.model.TransactionType;
import com.financial.alireza.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final TransactionRepository requestRepository;
    private final AccountRepository accountRepository;

    public Long submitTransaction(CreateTransactionRequest transactionRequest){
        TransactionRequestEntity request =
        TransactionRequestEntity.builder()
                .source(
                        transactionRequest.transactionType() == TransactionType.DEPOSIT
                                ? null
                                : transactionRequest.source()
                )
                .destination(
                        transactionRequest.transactionType() == TransactionType.WITHDRAW
                                ? null
                                : transactionRequest.destination()
                )
                                        .amount(transactionRequest.amount())
                                                .transactionType(transactionRequest.transactionType())
                                                        .build();

        TransactionRequestEntity saved = requestRepository.save(request);
        return saved.getId();
    }

    public TransactionStatusResponse getStatus(Long id){
        TransactionRequestEntity entity =
                requestRepository.findById(id).orElseThrow(()-> new TransactionNotFoundException("Transaction not found"));

        return TransactionStatusResponse
                .builder()
                .status(entity.getStatus())
                .transactionNumber(entity.getTransactionNumber())
                .failureReason(entity.getFailureReason())
                .completeAt(entity.getCompleteAt())
                .build();
    }
}
