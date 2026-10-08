package com.financial.alireza.transactions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Table(name = "transaction_requests")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)

    private String trackingNumber;
    private Long amount;
    private String source;
    private String destination;

    Instant completeAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private TransactionRequestEntity parentTransaction;


}
