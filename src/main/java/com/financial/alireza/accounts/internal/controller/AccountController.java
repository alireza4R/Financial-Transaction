package com.financial.alireza.accounts.internal.controller;

import com.financial.alireza.accounts.internal.dto.AccountCreateResponse;
import com.financial.alireza.accounts.internal.dto.CreateAccountRequest;
import com.financial.alireza.accounts.internal.dto.UpdateAccountRequest;
import com.financial.alireza.accounts.internal.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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


}
