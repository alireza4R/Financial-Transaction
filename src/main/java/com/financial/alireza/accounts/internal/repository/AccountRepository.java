package com.financial.alireza.accounts.internal.repository;

import com.financial.alireza.accounts.internal.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    public Optional<AccountEntity> findByNationalId(String nationalId);
}
