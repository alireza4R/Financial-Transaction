package com.financial.alireza.accounts.internal.repository;

import com.financial.alireza.accounts.internal.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
