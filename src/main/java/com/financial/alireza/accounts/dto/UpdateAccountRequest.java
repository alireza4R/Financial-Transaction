package com.financial.alireza.accounts.dto;

import com.financial.alireza.accounts.model.AccountStatus;
import com.financial.alireza.accounts.model.CustomerType;

import java.time.LocalDate;

public record UpdateAccountRequest( String name,
                                    String newNationalId,
                                    CustomerType customerType,
                                    String phoneNumber,
                                    String address,
                                    String postalCode,
                                    LocalDate birth,
                                    AccountStatus accountStatus) {
}
