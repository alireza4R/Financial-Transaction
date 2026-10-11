package com.financial.alireza.transactions.service;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionNumberGenerator {

    private final JdbcTemplate jdbcTemplate;

    public Long generate() {
        Long number = jdbcTemplate.queryForObject(
                "SELECT transaction_number_seq.NEXTVAL FROM dual",
                Long.class
        );

        if (number == null) {
            throw new IllegalStateException(
                    "Could not generate transaction number"
            );
        }

        return number;
    }
}