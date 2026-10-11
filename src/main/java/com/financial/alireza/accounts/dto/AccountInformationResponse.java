package com.financial.alireza.accounts.dto;

import com.financial.alireza.accounts.model.AccountStatus;
import com.financial.alireza.accounts.model.CustomerType;
import lombok.Builder;

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
