package com.financial.alireza.transactions.service;

import com.financial.alireza.accounts.model.AccountEntity;
import com.financial.alireza.accounts.repository.AccountRepository;
import com.financial.alireza.transactions.config.TransferFeeProperties;
import com.financial.alireza.transactions.dto.LockedAccounts;
import com.financial.alireza.transactions.model.Status;
import com.financial.alireza.transactions.model.TransactionRequestEntity;
import com.financial.alireza.transactions.repository.TransactionRepository;
import com.financial.alireza.transactions.validation.TransactionExecutionValidator;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DepositHandler extends TransactionHandlerAbstract{


    protected DepositHandler(TransactionRepository transactionRepository, TransactionExecutionValidator executionValidator, AccountRepository accountRepository, TransferFeeProperties feeProperties, TransactionNumberGenerator transactionNumberGenerator) {
        super(transactionRepository, executionValidator, accountRepository, feeProperties, transactionNumberGenerator);
    }

    @Override
    protected LockedAccounts loadAndLockAccounts(
            TransactionRequestEntity request) {

        AccountEntity account = accountRepository.findForUpdateByAccountNumber(request.getDestination()).orElse(null);


        return new LockedAccounts(
                null,
                account,
                null
        );
    }

    @Override
    protected Long calculateFee(Long amount) {
        return 0L;
    }

    @Override
    protected void execute(TransactionRequestEntity request , LockedAccounts accounts, Long fee) {
        accounts.destination().setBalance(
                Math.addExact(accounts.destination().getBalance() , request.getAmount())
        );

        request.setTransactionNumber(transactionNumberGenerator.generate());
        request.setStatus(Status.SUCCESSFUL);
        request.setCompleteAt(Instant.now());

        accountRepository.save(accounts.destination());
        transactionRepository.save(request);

    }
}
