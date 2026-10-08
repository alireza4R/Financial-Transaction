package com.financial.alireza.transactions.controller;

import com.financial.alireza.transactions.dto.CreateTransactionRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @PostMapping("transaction/request")
    public void requestTransaction(@RequestBody CreateTransactionRequest TransactionRequest){

    }
}
