package com.financial.alireza.transactions.controller;

import com.financial.alireza.transactions.dto.CreateTransactionRequest;
import com.financial.alireza.transactions.dto.TransactionStatusResponse;
import com.financial.alireza.transactions.model.Status;
import com.financial.alireza.transactions.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final RequestService requestService;

    @PostMapping("transaction/request")
    public Long requestTransaction(@Valid @RequestBody CreateTransactionRequest transactionRequest){
        return requestService.submitTransaction(transactionRequest);
    }

    @GetMapping("transaction/{id}")
    public ResponseEntity<TransactionStatusResponse> getTransactionStatus(@PathVariable Long id){

        return ResponseEntity.ok(requestService.getStatus(id));

    }
}
