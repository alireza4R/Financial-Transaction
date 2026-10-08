package com.financial.alireza.transactions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "transaction_history")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionHistoryEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long Id;

    private Instant completeAt;

}
