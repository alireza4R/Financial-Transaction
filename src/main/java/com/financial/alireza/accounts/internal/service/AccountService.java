package com.financial.alireza.accounts.internal.service;

import com.financial.alireza.accounts.internal.dto.CreateAccountRequest;
import com.financial.alireza.accounts.internal.exception.AccountExistException;
import com.financial.alireza.accounts.internal.model.AccountEntity;
import com.financial.alireza.accounts.internal.model.AccountKind;
import com.financial.alireza.accounts.internal.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CardNumberGenerator cardNumberGenerator;

    public String createAccount(CreateAccountRequest createAccountRequest){
        accountRepository.findByNationalId(createAccountRequest.nationalId())
                .ifPresent((account)-> {throw new AccountExistException();});

        AccountEntity accountEntity = AccountEntity.builder()
                .accountKind(AccountKind.CUSTOMER)
                .accountNumber(cardNumberGenerator.generate())
                .birth(createAccountRequest.birth())
                .address(createAccountRequest.address())
                .customerType(createAccountRequest.customerType())
                .name(createAccountRequest.name())
                .nationalId(createAccountRequest.nationalId())
                .phoneNumber(createAccountRequest.phoneNumber())
                .postalCode(createAccountRequest.postalCode())
                .createAt(LocalDate.now())
                .lastUpdate(LocalDate.now())
                .build();

        AccountEntity savedAccount = accountRepository.save(accountEntity);
        return savedAccount.getAccountNumber();
    }

}
