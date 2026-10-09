package com.financial.alireza.transactions.service;

import com.financial.alireza.accounts.repository.AccountRepository;
import com.financial.alireza.transactions.config.TransferFeeProperties;
import com.financial.alireza.transactions.controller.TransactionController;
import com.financial.alireza.transactions.dto.LockedAccounts;
import com.financial.alireza.transactions.exception.TransactionFailedException;
import com.financial.alireza.transactions.model.Status;
import com.financial.alireza.transactions.model.TransactionRequestEntity;
import com.financial.alireza.transactions.repository.TransactionRepository;
import com.financial.alireza.transactions.validation.TransactionExecutionValidator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public abstract  class  TransactionHandlerAbstract {
    protected final TransactionRepository transactionRepository;
    protected final TransactionExecutionValidator executionValidator;
    protected final AccountRepository accountRepository;
    protected final TransferFeeProperties feeProperties;
    protected final TransactionNumberGenerator transactionNumberGenerator;

    protected TransactionHandlerAbstract(TransactionRepository transactionRepository, TransactionExecutionValidator executionValidator, AccountRepository accountRepository, TransferFeeProperties feeProperties, TransactionNumberGenerator transactionNumberGenerator) {
        this.transactionRepository = transactionRepository;
        this.executionValidator = executionValidator;
        this.accountRepository = accountRepository;
        this.feeProperties = feeProperties;
        this.transactionNumberGenerator = transactionNumberGenerator;
    }

    public void lunchTransaction(TransactionRequestEntity transactionRequest){

        LockedAccounts accounts = loadAndLockAccounts(transactionRequest);

        Long fee = calculateFee(transactionRequest.getAmount());
        List<String> errors =
        executionValidator.validateTransaction(transactionRequest, accounts, fee);

        if(!errors.isEmpty()){
            transactionRequest.setFailureReason(String.join("", errors));
            transactionRequest.setStatus(Status.FAILURE);
            transactionRequest.setCompleteAt(Instant.now());
            return;
        }

        execute(transactionRequest, accounts, fee);
    };

    protected abstract LockedAccounts loadAndLockAccounts(TransactionRequestEntity request);
    protected abstract Long calculateFee(Long amount);
    protected abstract void execute (TransactionRequestEntity request, LockedAccounts accounts, Long fee);



}
