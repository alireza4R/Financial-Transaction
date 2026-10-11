package com.financial.alireza.transactions.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.Instant;

@Table(name = "transaction_requests")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionRequestEntity {

    @Id
    @SequenceGenerator(
            name = "transactionIdGenerator",
            sequenceName = "transaction_requests_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transactionIdGenerator"
    )
    private Long id;
    @Column(nullable = false)

    private Long amount;
    private String source;
    private String destination;
    private String failureReason;

    @Column(unique = true)
    private Long transactionNumber;

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
