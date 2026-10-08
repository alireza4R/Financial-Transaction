package com.financial.alireza.accounts.repository;

import com.financial.alireza.accounts.model.AccountHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHistoryRepository extends JpaRepository<AccountHistoryEntity,Long> {
}
