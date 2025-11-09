package com.bankingapplication.account_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String userId; // References the user ID from auth-service

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String currency; // Default currency (e.g., "USD")

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "daily_transaction_limit")
    private BigDecimal dailyTransactionLimit;

    @Column(name = "daily_withdrawal_limit")
    private BigDecimal dailyWithdrawalLimit;

    @Column(name = "interest_rate")
    private Double interestRate; // For savings accounts

    @Column(name = "overdraft_limit")
    private BigDecimal overdraftLimit; // For checking accounts

    @Column(name = "minimum_balance")
    private BigDecimal minimumBalance;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = true;

        // Set default values based on account type
        if (this.balance == null) {
            this.balance = BigDecimal.ZERO;
        }

        if (this.currency == null) {
            this.currency = "USD";
        }

        if (this.accountType == AccountType.SAVINGS) {
            if (this.interestRate == null) {
                this.interestRate = 0.01; // 1% interest rate
            }
            if (this.minimumBalance == null) {
                this.minimumBalance = new BigDecimal("100.00");
            }
            if (this.dailyWithdrawalLimit == null) {
                this.dailyWithdrawalLimit = new BigDecimal("1000.00");
            }
        } else if (this.accountType == AccountType.CHECKING) {
            if (this.overdraftLimit == null) {
                this.overdraftLimit = new BigDecimal("500.00");
            }
            if (this.dailyWithdrawalLimit == null) {
                this.dailyWithdrawalLimit = new BigDecimal("2000.00");
            }
        }

        if (this.dailyTransactionLimit == null) {
            this.dailyTransactionLimit = new BigDecimal("5000.00");
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Account() {
        this.transactions = new ArrayList<>();
    }

    public Account(Long id, String accountNumber, String userId, AccountType accountType, BigDecimal balance,
                   String currency, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isActive,
                   BigDecimal dailyTransactionLimit, BigDecimal dailyWithdrawalLimit, Double interestRate,
                   BigDecimal overdraftLimit, BigDecimal minimumBalance, List<Transaction> transactions) {
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
        this.transactions = transactions != null ? transactions : new ArrayList<>();
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

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
