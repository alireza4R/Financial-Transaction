package com.financial.alireza.accounts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Entity
@Table(name = "account_history")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AccountHistoryEntity {

    @Id
    @SequenceGenerator(
            name = "accountHistoryIdGenerator",
            sequenceName = "account_history_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "accountHistoryIdGenerator"
    )
    private Long id;

    private String fieldName;
    private String oldValue;
    private String newValue;
    private Instant time;
    private String userName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private AccountEntity accountEntity;

}
