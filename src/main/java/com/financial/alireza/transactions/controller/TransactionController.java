package com.financial.alireza.transactions.controller;

import com.financial.alireza.transactions.dto.*;
import com.financial.alireza.transactions.service.ReportService;
import com.financial.alireza.transactions.service.RequestService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final RequestService requestService;
    private final ReportService reportService;

    @PostMapping("transaction/request")
    public Long requestTransaction(@Valid @RequestBody CreateTransactionRequest transactionRequest){
        return requestService.submitTransaction(transactionRequest);
    }

    @GetMapping("transaction/{id}")
    public ResponseEntity<TransactionStatusResponse> getTransactionStatus(@PathVariable Long id){

        return ResponseEntity.ok(requestService.getStatus(id));

    }

    @GetMapping("transaction/report")
    public ResponseEntity<ReportResponse> getReport(@Valid @ModelAttribute @ParameterObject ReportRequest reportRequest,
                                                    @RequestParam (defaultValue = "1") @Min(1) int page,
                                                    @RequestParam(defaultValue = "20") @Min(1) @Max(100) int limit){

        Page<TransactionResponse> result = reportService.report(reportRequest, page, limit);

        ReportResponse response = new ReportResponse(
                result.getContent(),
                result.getNumber() + 1,
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );

        return ResponseEntity.ok(response);

    }
}
