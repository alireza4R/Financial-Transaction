package com.financial.alireza.transactions.repository;

import com.financial.alireza.transactions.model.TransactionRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionRequestEntity, Long> {


}
