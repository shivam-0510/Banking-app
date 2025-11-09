package com.bankingapplication.account_service.dto.request;

import java.math.BigDecimal;

import com.bankingapplication.account_service.entity.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
public class AccountCreationRequest {

    // Changed from @NotBlank to make it optional for the /my-account endpoint
    private String userId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Initial deposit amount is required")
    @Positive(message = "Initial deposit amount must be positive")
    private BigDecimal initialDeposit;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO currency code (e.g., USD, EUR)")
    private String currency;

    // Optional limits can be overridden
    private BigDecimal dailyTransactionLimit;
    private BigDecimal dailyWithdrawalLimit;
    private Double interestRate;
    private BigDecimal overdraftLimit;
    private BigDecimal minimumBalance;

    // Constructors
    public AccountCreationRequest() {
    }

    public AccountCreationRequest(String userId, AccountType accountType, BigDecimal initialDeposit,
                                  String currency, BigDecimal dailyTransactionLimit,
                                  BigDecimal dailyWithdrawalLimit, Double interestRate,
                                  BigDecimal overdraftLimit, BigDecimal minimumBalance) {
        this.userId = userId;
        this.accountType = accountType;
        this.initialDeposit = initialDeposit;
        this.currency = currency;
        this.dailyTransactionLimit = dailyTransactionLimit;
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
        this.interestRate = interestRate;
        this.overdraftLimit = overdraftLimit;
        this.minimumBalance = minimumBalance;
    }

    // Getters and Setters
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

    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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
