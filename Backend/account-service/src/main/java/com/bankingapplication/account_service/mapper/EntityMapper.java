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
        return new AccountDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getUserId(),
                account.getAccountType(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreatedAt(),
                account.getUpdatedAt(),
                account.isActive(),
                account.getDailyTransactionLimit(),
                account.getDailyWithdrawalLimit(),
                account.getInterestRate(),
                account.getOverdraftLimit(),
                account.getMinimumBalance()
        );
    }

    public List<AccountDTO> mapToAccountDTOList(List<Account> accounts) {
        return accounts.stream()
                .map(this::mapToAccountDTO)
                .collect(Collectors.toList());
    }

    public Account mapToAccount(AccountCreationRequest request) {
        Account account = new Account();
        account.setUserId(request.getUserId());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getInitialDeposit());
        account.setCurrency(request.getCurrency());
        account.setDailyTransactionLimit(request.getDailyTransactionLimit());
        account.setDailyWithdrawalLimit(request.getDailyWithdrawalLimit());
        account.setInterestRate(request.getInterestRate());
        account.setOverdraftLimit(request.getOverdraftLimit());
        account.setMinimumBalance(request.getMinimumBalance());
        return account;
    }

    public TransactionDTO mapToTransactionDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getTransactionId(),
                transaction.getAccount() != null ? transaction.getAccount().getId() : null,
                transaction.getAccount() != null ? transaction.getAccount().getAccountNumber() : null,
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getSourceAccountNumber(),
                transaction.getDestinationAccountNumber(),
                transaction.getReferenceNumber(),
                transaction.getDescription(),
                transaction.getTransactionDate(),
                transaction.getBalanceAfterTransaction()
        );
    }

    public List<TransactionDTO> mapToTransactionDTOList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::mapToTransactionDTO)
                .collect(Collectors.toList());
    }
}
