package com.financial.alireza.transactions.repository;

import com.financial.alireza.transactions.model.TransactionRequestEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionRequestEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    select t from TransactionRequestEntity t
    where t.id = :id
    """)
    Optional<TransactionRequestEntity> findForUpdateById(
            @Param("id") Long id
    );

}
