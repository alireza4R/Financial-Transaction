package com.financial.alireza.transactions.service;

import com.financial.alireza.accounts.exception.AccountNotFoundException;
import com.financial.alireza.accounts.repository.AccountRepository;
import com.financial.alireza.transactions.dto.ReportRequest;
import com.financial.alireza.transactions.dto.TransactionResponse;
import com.financial.alireza.transactions.model.TransactionRequestEntity;
import com.financial.alireza.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.financial.alireza.transactions.model.Direction;
import com.financial.alireza.transactions.model.Status;
import jakarta.persistence.criteria.Predicate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Page<TransactionResponse> report(ReportRequest filter, int page, int limit) {

        if (!accountRepository.existsByAccountNumber(filter.accountNumber())) {
            throw new AccountNotFoundException("Account not found");
        }

        Specification<TransactionRequestEntity> specification =
                buildSpecification(filter);

        Pageable effectivePageable = PageRequest.of(
                page -1,
                Math.min(limit, 100),
                Sort.by(
                        Sort.Order.desc("completeAt"),
                        Sort.Order.desc("id")
                )
        );


        return transactionRepository
                .findAll(specification, effectivePageable)
                .map(transaction -> new TransactionResponse(
                        transaction.getTransactionType(),
                        transaction.getSource(),
                        transaction.getDestination(),
                        transaction.getAmount(),
                        transaction.getCompleteAt(),
                        transaction.getId()
                ));
    }

    private Specification<TransactionRequestEntity> buildSpecification(ReportRequest filter) {

        return (root, query, cb) -> {

            List<Predicate> conditions = new ArrayList<>();


            conditions.add(
                    cb.equal(root.get("status"), Status.SUCCESSFUL)
            );

            String accountNumber = filter.accountNumber();
            String counterparty = filter.counterpartyAccount();

            Direction direction = filter.direction() == null ? Direction.ALL : filter.direction();


            Predicate incoming = cb.equal(root.get("destination"), accountNumber);


            Predicate outgoing = cb.equal(root.get("source"), accountNumber);


            if (counterparty != null) {
                incoming = cb.and(
                        incoming,
                        cb.equal(root.get("source"), counterparty)
                );

                outgoing = cb.and(
                        outgoing,
                        cb.equal(root.get("destination"), counterparty)
                );
            }

            Predicate accountCondition = switch (direction) {
                case IN -> incoming;
                case OUT -> outgoing;
                case ALL -> cb.or(incoming, outgoing);
            };

            conditions.add(accountCondition);

            if (filter.transactionType() != null) {
                conditions.add(
                        cb.equal(
                                root.get("transactionType"),
                                filter.transactionType()
                        )
                );
            }

            if (filter.minAmount() != null) {
                conditions.add(
                        cb.greaterThanOrEqualTo(
                                root.<Long>get("amount"),
                                filter.minAmount()
                        )
                );
            }

            if (filter.maxAmount() != null) {
                conditions.add(
                        cb.lessThanOrEqualTo(
                                root.<Long>get("amount"),
                                filter.maxAmount()
                        )
                );
            }

            if (filter.from() != null) {
                conditions.add(
                        cb.greaterThanOrEqualTo(
                                root.<Instant>get("completeAt"),
                                filter.from()
                        )
                );
            }

            if (filter.to() != null) {
                conditions.add(
                        cb.lessThan(
                                root.<Instant>get("completeAt"),
                                filter.to()
                        )
                );
            }

            return cb.and(conditions.toArray(new Predicate[0]));
        };
    }
}
