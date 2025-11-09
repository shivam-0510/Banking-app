package com.bankingapplication.account_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bankingapplication.account_service.entity.TransactionStatus;
import com.bankingapplication.account_service.entity.TransactionType;

public class TransactionDTO {

    private Long id;
    private String transactionId;
    private Long accountId;
    private String accountNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String referenceNumber;
    private String description;
    private LocalDateTime transactionDate;
    private BigDecimal balanceAfterTransaction;

    // Constructors
    public TransactionDTO() {
    }

    public TransactionDTO(Long id, String transactionId, Long accountId, String accountNumber, BigDecimal amount,
                         TransactionType transactionType, TransactionStatus status, String sourceAccountNumber,
                         String destinationAccountNumber, String referenceNumber, String description,
                         LocalDateTime transactionDate, BigDecimal balanceAfterTransaction) {
        this.id = id;
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.accountNumber = accountNumber;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
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
