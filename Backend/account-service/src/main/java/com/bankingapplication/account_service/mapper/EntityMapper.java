package com.bankingapplication.account_service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.bankingapplication.account_service.dto.AccountDTO;
import com.bankingapplication.account_service.dto.TransactionDTO;
import com.bankingapplication.account_service.dto.request.AccountCreationRequest;
import com.bankingapplication.account_service.entity.Account;
import com.bankingapplication.account_service.entity.Transaction;

@Component
public class EntityMapper {

    public AccountDTO mapToAccountDTO(Account account) {
        return AccountDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .userId(account.getUserId())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .isActive(account.isActive())
                .dailyTransactionLimit(account.getDailyTransactionLimit())
                .dailyWithdrawalLimit(account.getDailyWithdrawalLimit())
                .interestRate(account.getInterestRate())
                .overdraftLimit(account.getOverdraftLimit())
                .minimumBalance(account.getMinimumBalance())
                .build();
    }

    public List<AccountDTO> mapToAccountDTOList(List<Account> accounts) {
        return accounts.stream()
                .map(this::mapToAccountDTO)
                .collect(Collectors.toList());
    }

    public Account mapToAccount(AccountCreationRequest request) {
        return Account.builder()
                .userId(request.getUserId())
                .accountType(request.getAccountType())
                .balance(request.getInitialDeposit())
                .currency(request.getCurrency())
                .dailyTransactionLimit(request.getDailyTransactionLimit())
                .dailyWithdrawalLimit(request.getDailyWithdrawalLimit())
                .interestRate(request.getInterestRate())
                .overdraftLimit(request.getOverdraftLimit())
                .minimumBalance(request.getMinimumBalance())
                .build();
    }

    public TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .transactionId(transaction.getTransactionId())
                .accountId(transaction.getAccount().getId())
                .accountNumber(transaction.getAccount().getAccountNumber())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .status(transaction.getStatus())
                .sourceAccountNumber(transaction.getSourceAccountNumber())
                .destinationAccountNumber(transaction.getDestinationAccountNumber())
                .referenceNumber(transaction.getReferenceNumber())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .balanceAfterTransaction(transaction.getBalanceAfterTransaction())
                .build();
    }

    public List<TransactionDTO> mapToTransactionDTOList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::mapToTransactionDTO)
                .collect(Collectors.toList());
    }
}
