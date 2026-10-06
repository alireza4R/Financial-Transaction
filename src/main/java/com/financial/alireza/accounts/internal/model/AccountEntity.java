package com.financial.alireza.accounts.internal.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Instant;
import java.time.LocalTime;

@Table(name = "accounts")
@AllArgsConstructor
@Setter
@Getter
@Entity
@NoArgsConstructor
@Builder
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true, length = 14, updatable = false)
    private String accountNumber;
    private String name;

    @Column(unique = true)
    private String nationalId;
    private LocalDate birth;
    private String phoneNumber;
    private String address;
    private String postalCode;
    private LocalDate lastUpdate;

    @Builder.Default
    @Column(nullable = false)
    private Long balance = 0L;

    @Column(nullable = false, updatable = false)
    private LocalDate createAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType customerType;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountKind accountKind;

}
