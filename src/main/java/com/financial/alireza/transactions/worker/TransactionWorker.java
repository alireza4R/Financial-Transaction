package com.financial.alireza.transactions.worker;

import com.financial.alireza.transactions.service.ExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionWorker {

    private final ExecutionService executionService;

    @Scheduled(
            fixedDelayString = "${app.transactions.worker-delay-ms:500}"
    )
    public void poll() {
        boolean processed = executionService.processNextRequest();

        if (processed) {
            log.info("Transaction processing committed");
        }
    }
}
