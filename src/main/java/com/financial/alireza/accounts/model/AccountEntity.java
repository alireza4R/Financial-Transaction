package com.financial.alireza.accounts.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "accounts")
@AllArgsConstructor
@Setter
@Getter
@Entity
@NoArgsConstructor
@Builder
public class AccountEntity {
    @Id
    @SequenceGenerator(
            name = "accountIdGenerator",
            sequenceName = "accounts_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "accountIdGenerator"
    )
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
