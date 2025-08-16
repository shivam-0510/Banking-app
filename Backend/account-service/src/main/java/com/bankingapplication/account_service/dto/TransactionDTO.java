package com.bankingapplication.account_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bankingapplication.account_service.entity.TransactionStatus;
import com.bankingapplication.account_service.entity.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
}
