package com.financial.alireza.transactions.service;

import com.financial.alireza.accounts.repository.AccountRepository;
import com.financial.alireza.transactions.dto.CreateTransactionRequest;
import com.financial.alireza.transactions.model.TransactionRequestEntity;
import com.financial.alireza.transactions.repository.TransactionRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final TransactionRequestRepository requestRepository;
    private final AccountRepository accountRepository;

    public Long submitTransaction(CreateTransactionRequest transactionRequest){
        TransactionRequestEntity request =
        TransactionRequestEntity.builder()
                        .source(transactionRequest.source())
                                .destination(transactionRequest.destination())
                                        .amount(transactionRequest.amount())
                                                .transactionType(transactionRequest.transactionType())
                                                        .build();

        TransactionRequestEntity saved = requestRepository.save(request);
        return saved.getId();
    }
}
