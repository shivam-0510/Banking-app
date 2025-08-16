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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Builder.Default
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
}
