package com.financial.alireza.transactions.dto;

import com.financial.alireza.accounts.model.AccountEntity;

public record LockedAccounts(
        AccountEntity source,
        AccountEntity destination,
        AccountEntity bank
) {}