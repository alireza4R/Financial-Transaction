package com.financial.alireza.accounts.internal.repository;

import com.financial.alireza.accounts.internal.model.AccountHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountHistoryRepository extends JpaRepository<AccountHistoryEntity,Long> {
}
