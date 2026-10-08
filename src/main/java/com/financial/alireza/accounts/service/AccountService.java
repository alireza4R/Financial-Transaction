package com.financial.alireza.accounts.service;

import com.financial.alireza.accounts.dto.AccountInformationResponse;
import com.financial.alireza.accounts.dto.CreateAccountRequest;
import com.financial.alireza.accounts.dto.UpdateAccountRequest;
import com.financial.alireza.accounts.exception.AccountExistException;
import com.financial.alireza.accounts.exception.AccountNotFoundException;
import com.financial.alireza.accounts.model.AccountEntity;
import com.financial.alireza.accounts.model.AccountHistoryEntity;
import com.financial.alireza.accounts.model.AccountKind;
import com.financial.alireza.accounts.repository.AccountHistoryRepository;
import com.financial.alireza.accounts.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final CardNumberGenerator cardNumberGenerator;
    private final AccountHistoryRepository accountHistoryRepository;

    public String createAccount(CreateAccountRequest createAccountRequest){
        accountRepository.findByNationalId(createAccountRequest.nationalId())
                .ifPresent((account)-> {throw new AccountExistException("An account with this national id already exist");});

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

    @Transactional
    public void changeInformation(UpdateAccountRequest updateAccountRequest, String nationalId, String userName){

        AccountEntity accountEntity = accountRepository.findForUpdateByNationalId(nationalId)
                .orElseThrow(()-> new UsernameNotFoundException("No account found with this National ID"));
        accountRepository.findByNationalId(updateAccountRequest.newNationalId()).ifPresent(accountEntity1 -> {throw new AccountExistException("Your new national id already exist");});


        var account = PropertyAccessorFactory
                .forBeanPropertyAccess(accountEntity);

        try {
            for (var component : UpdateAccountRequest.class.getRecordComponents()) {
                String fieldName = component.getName();

                Object newValue = component.getAccessor()
                        .invoke(updateAccountRequest);

                Object oldValue = account.getPropertyValue(fieldName);

                helper(
                        fieldName,
                        oldValue,
                        newValue,
                        value -> account.setPropertyValue(fieldName, value),
                        accountEntity,
                        userName
                );
            }
            accountEntity.setLastUpdate(LocalDate.now());
            accountRepository.save(accountEntity);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot read update request", e);
        }

    }

    public Long getBalance (String accountNumber){
        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new AccountNotFoundException("no account with this account number exist"));
        return accountEntity.getBalance();
    }

    public String getAccountNumber(String nationalId){
        AccountEntity accountEntity = accountRepository.findByNationalId(nationalId)
                .orElseThrow(()-> new AccountNotFoundException("no account with this national id exist"));
        return accountEntity.getAccountNumber();
    }

    public AccountInformationResponse getAccountInformation(String accountNumber){
        AccountEntity accountEntity = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new AccountNotFoundException("no account with this account number exist"));

        AccountInformationResponse accountInformationResponse = AccountInformationResponse.builder()
                .accountStatus(accountEntity.getAccountStatus())
                .name(accountEntity.getName())
                .lastUpdate(accountEntity.getLastUpdate())
                .birth(accountEntity.getBirth())
                .postalCode(accountEntity.getPostalCode())
                .nationalId(accountEntity.getNationalId())
                .phoneNumber(accountEntity.getPhoneNumber())
                .customerType(accountEntity.getCustomerType())
                .address(accountEntity.getAddress())
                .build();

        return accountInformationResponse;

    }

    private <T> void helper(String fieldName, T oldValue, T newValue, Consumer<T> setter, AccountEntity accountEntity, String userName) {
        if (newValue == null || newValue.toString().isBlank()) {
            return;
        }
        if(!Objects.equals(oldValue, newValue)){
            AccountHistoryEntity history = AccountHistoryEntity.builder()
                    .fieldName(fieldName)
                    .oldValue(oldValue == null ? null : oldValue.toString())
                    .newValue(newValue.toString())
                    .userName(userName)
                    .accountEntity(accountEntity)
                    .time(Instant.now())
                    .build();

            accountHistoryRepository.save(history);
            setter.accept(newValue);
        }
    }

}
