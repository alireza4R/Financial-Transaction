package com.financial.alireza.transactions.service;

import com.financial.alireza.accounts.model.AccountEntity;
import com.financial.alireza.accounts.repository.AccountRepository;
import com.financial.alireza.transactions.config.TransferFeeProperties;
import com.financial.alireza.transactions.dto.LockedAccounts;
import com.financial.alireza.transactions.model.Status;
import com.financial.alireza.transactions.model.TransactionRequestEntity;
import com.financial.alireza.transactions.model.TransactionType;
import com.financial.alireza.transactions.repository.TransactionRepository;
import com.financial.alireza.transactions.validation.TransactionExecutionValidator;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class WithdrawHandler extends TransactionHandlerAbstract {


    protected WithdrawHandler(TransactionRepository transactionRepository, TransactionExecutionValidator executionValidator, AccountRepository accountRepository, TransferFeeProperties feeProperties, TransactionNumberGenerator transactionNumberGenerator) {
        super(transactionRepository, executionValidator, accountRepository, feeProperties, transactionNumberGenerator);
    }

    @Override
    protected LockedAccounts loadAndLockAccounts(
            TransactionRequestEntity request) {

            AccountEntity account = accountRepository.findForUpdateByAccountNumber(request.getSource()).orElse(null);


        return new LockedAccounts(
                account,
                null,
                null
        );
    }

    @Override
    protected Long calculateFee(Long amount) {
        return 0L;
    }

    @Override
    protected void execute(TransactionRequestEntity request , LockedAccounts accounts, Long fee) {
        accounts.source().setBalance(
                accounts.source().getBalance() - request.getAmount()
        );

        request.setTransactionNumber(transactionNumberGenerator.generate());
        request.setStatus(Status.SUCCESSFUL);
        request.setCompleteAt(Instant.now());

        accountRepository.save(accounts.source());
        transactionRepository.save(request);

    }
}
