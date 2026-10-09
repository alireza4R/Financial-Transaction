package com.financial.alireza.transactions.validation;

import com.financial.alireza.accounts.model.AccountEntity;
import com.financial.alireza.accounts.model.AccountStatus;
import com.financial.alireza.accounts.repository.AccountRepository;
import com.financial.alireza.transactions.model.Status;
import com.financial.alireza.transactions.model.TransactionRequestEntity;
import com.financial.alireza.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class TransactionExecutionValidator {
    private final AccountRepository accountRepository;

    public List<String> validateTransaction (TransactionRequestEntity request,
                                             long fee){

        Optional<AccountEntity> source = accountRepository.findByAccountNumber(request.getSource());
        Optional<AccountEntity> destination = accountRepository.findByAccountNumber(request.getDestination());
        Optional<AccountEntity> bank = accountRepository.getBankAccount();

        List<String> errors = new ArrayList<>();

        switch (request.getTransactionType()){

            case DEPOSIT -> {
                checkAccount(destination, "Destination", errors);
            }

            case WITHDRAW -> {
                Optional<AccountEntity> account = accountRepository.findByAccountNumber(request.getSource());

                checkAccount(source, "Source", errors);

                source.ifPresent(accountEntity -> checkBalance(accountEntity,request.getAmount()  , 0L, errors));
            }

            case TRANSFER -> {

                checkAccount(source, "Source", errors);
                checkAccount(destination, "Destination", errors);
                checkAccount(bank, "Bank", errors);

                source.ifPresent(accountEntity -> checkBalance(accountEntity,request.getAmount()  , fee, errors));
            }
        }

        return errors;

    }

    private void checkAccount(
            Optional<AccountEntity> account,
            String role,
            List<String> errors) {

        if (account.isEmpty()) {
            errors.add(role + " account doesn't exist");
            return;
        }

        if (account.get().getAccountStatus() != AccountStatus.ACTIVE) {
            errors.add(role + " account is not active");
        }
    }


    private void checkBalance(
            AccountEntity source,
            long amount,
            long fee,
            List<String> errors) {

        Long balance = source.getBalance();

        if (balance == null || balance < 0) {
            throw new IllegalStateException(
                    "Source account has an invalid stored balance"
            );
        }

        if (balance < amount || balance - amount < fee) {
            errors.add(
                    "Source account has insufficient balance "
                            + "for the amount and applicable fee"
            );
        }
    }
}
