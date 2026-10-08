package com.financial.alireza.accounts.repository;

import com.financial.alireza.accounts.model.AccountEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    public Optional<AccountEntity> findByNationalId(String nationalId);

    public Optional<AccountEntity> findByAccountNumber(String accountNumber);

    public boolean existByAccountNumber(String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from AccountEntity a where a.nationalId = :nationalId")
    Optional<AccountEntity> findForUpdateByNationalId(
            @Param("nationalId") String nationalId);
}
