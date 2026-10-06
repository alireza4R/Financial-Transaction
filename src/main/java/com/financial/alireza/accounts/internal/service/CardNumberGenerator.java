package com.financial.alireza.accounts.internal.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Locale;

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
        return String.format(Locale.ROOT, "%014d", value);
    }
}