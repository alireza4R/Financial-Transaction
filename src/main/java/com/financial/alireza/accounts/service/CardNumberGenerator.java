package com.financial.alireza.accounts.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class CardNumberGenerator {

    private final JdbcTemplate jdbcTemplate;

    public CardNumberGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String generate() {
        Long value = jdbcTemplate.queryForObject(
                "SELECT card_number_seq.NEXTVAL FROM dual",
                Long.class
        );

        if (value == null) {
            throw new IllegalStateException("Could not generate card number");
        }

        log.info("Card Number {} has been generated",
                String.format(Locale.ROOT, "%014d", value));

        return String.format(Locale.ROOT, "%014d", value);
    }
}