package com.financial.alireza.transactions.controller;

import com.financial.alireza.transactions.dto.CreateTransactionRequest;
import com.financial.alireza.transactions.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final RequestService requestService;

    @PostMapping("transaction/request")
    public Long requestTransaction(@Valid @RequestBody CreateTransactionRequest transactionRequest){
        return requestService.submitTransaction(transactionRequest);
    }
}
