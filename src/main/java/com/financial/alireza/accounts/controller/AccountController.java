package com.financial.alireza.accounts.controller;

import com.financial.alireza.accounts.dto.AccountCreateResponse;
import com.financial.alireza.accounts.dto.AccountInformationResponse;
import com.financial.alireza.accounts.dto.CreateAccountRequest;
import com.financial.alireza.accounts.dto.UpdateAccountRequest;
import com.financial.alireza.accounts.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountCreateResponse> createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest){
        String accountNumber = accountService.createAccount(createAccountRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AccountCreateResponse(accountNumber));
    }

    @PatchMapping("/update/{nationalId}")
    public void updateAccount(@RequestBody UpdateAccountRequest updateAccountRequest,
                              @NotBlank @PathVariable String nationalId,
                              Principal principal){
        accountService.changeInformation(updateAccountRequest, nationalId, principal.getName());
    }

    @GetMapping("/get/account_number/{nationalId}")
    public String getAccountNumber(@PathVariable String nationalId){
        return accountService.getAccountNumber(nationalId);

    }

    @GetMapping("/get/balance/{accountNumber}")
    public Long getBalance(@PathVariable String accountNumber){
        return accountService.getBalance(accountNumber);

    }


    @GetMapping("/get/information/{accountNumber}")
    public AccountInformationResponse getAccountInformation(@PathVariable String accountNumber){
        return accountService.getAccountInformation(accountNumber);
    }

}
