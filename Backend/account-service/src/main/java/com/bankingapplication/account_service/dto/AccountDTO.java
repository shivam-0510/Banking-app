package com.bankingapplication.account_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bankingapplication.account_service.entity.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;
    private String accountNumber;
    private String userId;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
    private BigDecimal dailyTransactionLimit;
    private BigDecimal dailyWithdrawalLimit;
    private Double interestRate;
    private BigDecimal overdraftLimit;
    private BigDecimal minimumBalance;
}
