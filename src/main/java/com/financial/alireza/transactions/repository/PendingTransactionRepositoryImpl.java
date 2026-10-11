package com.financial.alireza.transactions.repository;

import com.financial.alireza.transactions.model.TransactionRequestEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Optional;

public class PendingTransactionRepositoryImpl
        implements PendingTransactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String LOCK_NEXT_PENDING = """
            DECLARE
                CURSOR pending_cursor IS
                    SELECT id
                    FROM transaction_requests
                    WHERE status = 'PENDING'
                    ORDER BY id
                    FOR UPDATE SKIP LOCKED;

                selected_id transaction_requests.id%TYPE;
            BEGIN
                OPEN pending_cursor;

                FETCH pending_cursor INTO selected_id;

                IF pending_cursor%NOTFOUND THEN
                    selected_id := NULL;
                END IF;

                CLOSE pending_cursor;

                ? := selected_id;
            END;
            """;

    @Override
    public Optional<TransactionRequestEntity> findAndLockNextPending() {

        if (!entityManager.isJoinedToTransaction()) {
            throw new IllegalStateException(
                    "An active database transaction is required"
            );
        }

        Session session = entityManager.unwrap(Session.class);

        Long id = session.doReturningWork(connection -> {

            try (CallableStatement statement =
                         connection.prepareCall(LOCK_NEXT_PENDING)) {

                statement.registerOutParameter(1, Types.NUMERIC);
                statement.execute();

                BigDecimal value = statement.getBigDecimal(1);

                return value == null ? null : value.longValueExact();
            }
        });

        if (id == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(
                entityManager.find(TransactionRequestEntity.class, id)
        );
    }
}