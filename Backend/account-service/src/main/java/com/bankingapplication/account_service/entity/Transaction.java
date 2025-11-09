package com.bankingapplication.account_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String transactionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "source_account_number")
    private String sourceAccountNumber; // For transfers

    @Column(name = "destination_account_number")
    private String destinationAccountNumber; // For transfers

    @Column(name = "reference_number")
    private String referenceNumber;

    @Column
    private String description;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "balance_after_transaction")
    private BigDecimal balanceAfterTransaction;

    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
        if (this.status == null) {
            this.status = TransactionStatus.COMPLETED; // Default status
        }
    }

    // Constructors
    public Transaction() {
    }

    public Transaction(Long id, String transactionId, Account account, BigDecimal amount,
                      TransactionType transactionType, TransactionStatus status, String sourceAccountNumber,
                      String destinationAccountNumber, String referenceNumber, String description,
                      LocalDateTime transactionDate, BigDecimal balanceAfterTransaction) {
        this.id = id;
        this.transactionId = transactionId;
        this.account = account;
        this.amount = amount;
        this.transactionType = transactionType;
        this.status = status;
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.referenceNumber = referenceNumber;
        this.description = description;
        this.transactionDate = transactionDate;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getBalanceAfterTransaction() {
        return balanceAfterTransaction;
    }

    public void setBalanceAfterTransaction(BigDecimal balanceAfterTransaction) {
        this.balanceAfterTransaction = balanceAfterTransaction;
    }
}
