package com.bankingapplication.account_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bankingapplication.account_service.entity.Account;
import com.bankingapplication.account_service.entity.AccountType;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByUserId(String userId);

    Page<Account> findByUserId(String userId, Pageable pageable);

    List<Account> findByUserIdAndAccountType(String userId, AccountType accountType);

    @Query("SELECT a FROM Account a WHERE a.userId = :userId AND a.isActive = true")
    List<Account> findActiveAccountsByUserId(@Param("userId") String userId);

    @Query("SELECT COUNT(a) FROM Account a WHERE a.userId = :userId")
    long countAccountsByUserId(@Param("userId") String userId);

    @Query("SELECT a FROM Account a WHERE a.createdAt >= :startDate AND a.createdAt <= :endDate")
    List<Account> findAccountsCreatedBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}
