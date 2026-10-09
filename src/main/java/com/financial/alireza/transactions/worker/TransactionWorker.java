package com.financial.alireza.transactions.worker;

import com.financial.alireza.transactions.service.ExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionWorker {

    private final ExecutionService executionService;

    @Scheduled(
            fixedDelayString = "${app.transactions.worker-delay-ms:500}"
    )
    public void poll() {
        executionService.processNextRequest();
    }
}
