package com.financial.alireza.accounts.internal.dto;

import com.financial.alireza.accounts.internal.model.AccountStatus;
import com.financial.alireza.accounts.internal.model.CustomerType;
import lombok.Builder;

import java.time.Instant;
import java.time.LocalDate;

@Builder
public record AccountInformationResponse(String name
        , String nationalId
        , LocalDate birth
        , CustomerType customerType
        , String phoneNumber
        , String address
        , String postalCode
        , AccountStatus accountStatus
        , LocalDate lastUpdate) {
}
