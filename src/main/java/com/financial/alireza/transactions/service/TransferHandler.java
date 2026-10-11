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
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class TransferHandler extends TransactionHandlerAbstract{


    protected TransferHandler(TransactionRepository transactionRepository, TransactionExecutionValidator executionValidator, AccountRepository accountRepository, TransferFeeProperties feeProperties, TransactionNumberGenerator transactionNumberGenerator) {
        super(transactionRepository, executionValidator, accountRepository, feeProperties, transactionNumberGenerator);
    }

    @Override
    protected LockedAccounts loadAndLockAccounts(
            TransactionRequestEntity request) {

        String bankNumber = feeProperties.bankAccountNumber();

        List<String> numbers = Stream.of(
                        request.getSource(),
                        request.getDestination(),
                        bankNumber
                )
                .distinct()
                .sorted()
                .toList();

        Map<String, AccountEntity> accounts = new HashMap<>();

        for (String number : numbers) {
            accountRepository.findForUpdateByAccountNumber(number)
                    .ifPresent(account -> accounts.put(number, account));
        }

        return new LockedAccounts(
                accounts.get(request.getSource()),
                accounts.get(request.getDestination()),
                accounts.get(bankNumber)
        );
    }

    @Override
    protected Long calculateFee(Long amount) {
        BigDecimal fee = BigDecimal.valueOf(amount)
                .multiply(feeProperties.rate());

        return fee
                .max(BigDecimal.valueOf(feeProperties.minimum()))
                .min(BigDecimal.valueOf(feeProperties.maximum()))
                .setScale(0, RoundingMode.HALF_UP)
                .longValueExact();
    }

    @Override
    protected void execute(TransactionRequestEntity request , LockedAccounts accounts, Long fee) {
        accounts.source().setBalance(
                accounts.source().getBalance() - request.getAmount() - fee
        );

        accounts.destination().setBalance(
                accounts.destination().getBalance() + request.getAmount()
        );

        accounts.bank().setBalance(
                accounts.bank().getBalance() + fee
        );

        TransactionRequestEntity feeTransaction = TransactionRequestEntity.builder()
                .source(request.getSource())
                .amount(fee)
                .transactionType(TransactionType.FEE)
                .status(Status.SUCCESSFUL)
                .destination(accounts.bank().getAccountNumber())
                .parentTransaction(request)
                .transactionNumber(transactionNumberGenerator.generate())
                .completeAt(Instant.now()).build();

        request.setTransactionNumber(transactionNumberGenerator.generate());
        request.setStatus(Status.SUCCESSFUL);
        request.setCompleteAt(Instant.now());

        accountRepository.saveAll(List.of(
                accounts.source(),
                accounts.destination(),
                accounts.bank()
        ));
        transactionRepository.save(feeTransaction);
        transactionRepository.save(request);

    }
}
