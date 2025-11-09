package com.bankingapplication.account_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bankingapplication.account_service.entity.AccountType;

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

    // Constructors
    public AccountDTO() {
    }

    public AccountDTO(Long id, String accountNumber, String userId, AccountType accountType, BigDecimal balance,
                      String currency, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isActive,
                      BigDecimal dailyTransactionLimit, BigDecimal dailyWithdrawalLimit, Double interestRate,
                      BigDecimal overdraftLimit, BigDecimal minimumBalance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.currency = currency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.dailyTransactionLimit = dailyTransactionLimit;
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
        this.interestRate = interestRate;
        this.overdraftLimit = overdraftLimit;
        this.minimumBalance = minimumBalance;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public BigDecimal getDailyTransactionLimit() {
        return dailyTransactionLimit;
    }

    public void setDailyTransactionLimit(BigDecimal dailyTransactionLimit) {
        this.dailyTransactionLimit = dailyTransactionLimit;
    }

    public BigDecimal getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(BigDecimal dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public BigDecimal getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(BigDecimal minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
}
