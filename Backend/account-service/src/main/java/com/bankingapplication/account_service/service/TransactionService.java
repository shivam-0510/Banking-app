package com.bankingapplication.account_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankingapplication.account_service.dto.TransactionDTO;
import com.bankingapplication.account_service.dto.request.DepositRequest;
import com.bankingapplication.account_service.dto.request.TransferRequest;
import com.bankingapplication.account_service.dto.request.WithdrawalRequest;
import com.bankingapplication.account_service.entity.Account;
import com.bankingapplication.account_service.entity.Transaction;
import com.bankingapplication.account_service.entity.TransactionStatus;
import com.bankingapplication.account_service.entity.TransactionType;
import com.bankingapplication.account_service.exception.InsufficientBalanceException;
import com.bankingapplication.account_service.exception.InvalidOperationException;
import com.bankingapplication.account_service.exception.ResourceNotFoundException;
import com.bankingapplication.account_service.mapper.EntityMapper;
import com.bankingapplication.account_service.repository.AccountRepository;
import com.bankingapplication.account_service.repository.TransactionRepository;
import com.bankingapplication.account_service.util.AccountNumberGenerator;
import com.bankingapplication.account_service.util.PaginationUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final EntityMapper entityMapper;
    private final AccountService accountService;
    private final AccountNumberGenerator accountNumberGenerator;

    @Transactional
    public TransactionDTO deposit(DepositRequest request) {
        log.info("Processing deposit of {} to account {}", request.getAmount(), request.getAccountNumber());

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "account number", request.getAccountNumber()));

        // Validate if account is active
        if (!account.isActive()) {
            throw new InvalidOperationException("Cannot deposit to an inactive account");
        }

        // Validate transaction limit
        validateDailyTransactionLimit(account, request.getAmount(), TransactionType.DEPOSIT);

        // Update account balance
        BigDecimal newBalance = account.getBalance().add(request.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = Transaction.builder()
                .account(account)
                .transactionId(accountNumberGenerator.generateTransactionId())
                .amount(request.getAmount())
                .transactionType(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .description(request.getDescription() != null ? request.getDescription() : "Deposit")
                .referenceNumber(request.getReferenceNumber())
                .balanceAfterTransaction(newBalance)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Deposit completed successfully, new balance: {}", newBalance);

        return entityMapper.mapToTransactionDTO(savedTransaction);
    }

    @Transactional
    public TransactionDTO withdraw(WithdrawalRequest request) {
        log.info("Processing withdrawal of {} from account {}", request.getAmount(), request.getAccountNumber());

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "account number", request.getAccountNumber()));

        // Validate if account is active
        if (!account.isActive()) {
            throw new InvalidOperationException("Cannot withdraw from an inactive account");
        }

        // Validate daily withdrawal limit
        validateDailyWithdrawalLimit(account, request.getAmount());

        // Validate transaction limit
        validateDailyTransactionLimit(account, request.getAmount(), TransactionType.WITHDRAWAL);

        // Check if the account has sufficient balance
        BigDecimal availableBalance = account.getBalance().add(account.getOverdraftLimit() != null ? account.getOverdraftLimit() : BigDecimal.ZERO);
        if (availableBalance.compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }

        // Check minimum balance requirement if not using overdraft
        if (account.getMinimumBalance() != null
                && account.getBalance().compareTo(request.getAmount().add(account.getMinimumBalance())) < 0) {
            // Using overdraft
            log.info("Withdrawal will use overdraft protection");
        }

        // Update account balance
        BigDecimal newBalance = account.getBalance().subtract(request.getAmount());
        account.setBalance(newBalance);
        accountRepository.save(account);

        // Create transaction record
        Transaction transaction = Transaction.builder()
                .account(account)
                .transactionId(accountNumberGenerator.generateTransactionId())
                .amount(request.getAmount())
                .transactionType(TransactionType.WITHDRAWAL)
                .status(TransactionStatus.COMPLETED)
                .description(request.getDescription() != null ? request.getDescription() : "Withdrawal")
                .referenceNumber(request.getReferenceNumber())
                .balanceAfterTransaction(newBalance)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Withdrawal completed successfully, new balance: {}", newBalance);

        return entityMapper.mapToTransactionDTO(savedTransaction);
    }

    @Transactional
    public TransactionDTO transfer(TransferRequest request) {
        log.info("Processing transfer of {} from account {} to account {}",
                request.getAmount(), request.getSourceAccountNumber(), request.getDestinationAccountNumber());

        // Validate accounts
        Account sourceAccount = accountRepository.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "account number", request.getSourceAccountNumber()));

        Account destinationAccount = accountRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "account number", request.getDestinationAccountNumber()));

        // Validate if accounts are active
        if (!sourceAccount.isActive() || !destinationAccount.isActive()) {
            throw new InvalidOperationException("Cannot transfer between inactive accounts");
        }

        // Validate daily withdrawal limit for source account
        validateDailyWithdrawalLimit(sourceAccount, request.getAmount());

        // Validate transaction limit
        validateDailyTransactionLimit(sourceAccount, request.getAmount(), TransactionType.TRANSFER);

        // Check if the source account has sufficient balance
        BigDecimal availableBalance = sourceAccount.getBalance().add(
                sourceAccount.getOverdraftLimit() != null ? sourceAccount.getOverdraftLimit() : BigDecimal.ZERO);

        if (availableBalance.compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }

        // Update account balances
        BigDecimal newSourceBalance = sourceAccount.getBalance().subtract(request.getAmount());
        BigDecimal newDestinationBalance = destinationAccount.getBalance().add(request.getAmount());

        sourceAccount.setBalance(newSourceBalance);
        destinationAccount.setBalance(newDestinationBalance);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);

        // Generate a common reference for the transfer
        String transferReference = accountNumberGenerator.generateReferenceNumber();

        // Create outgoing transaction for source account
        Transaction sourceTransaction = Transaction.builder()
                .account(sourceAccount)
                .transactionId(accountNumberGenerator.generateTransactionId())
                .amount(request.getAmount())
                .transactionType(TransactionType.TRANSFER)
                .status(TransactionStatus.COMPLETED)
                .description(request.getDescription() != null ? request.getDescription() : "Transfer to " + request.getDestinationAccountNumber())
                .referenceNumber(request.getReferenceNumber() != null ? request.getReferenceNumber() : transferReference)
                .sourceAccountNumber(request.getSourceAccountNumber())
                .destinationAccountNumber(request.getDestinationAccountNumber())
                .balanceAfterTransaction(newSourceBalance)
                .build();

        // Create incoming transaction for destination account
        Transaction destinationTransaction = Transaction.builder()
                .account(destinationAccount)
                .transactionId(accountNumberGenerator.generateTransactionId())
                .amount(request.getAmount())
                .transactionType(TransactionType.DEPOSIT)
                .status(TransactionStatus.COMPLETED)
                .description(request.getDescription() != null ? request.getDescription() : "Transfer from " + request.getSourceAccountNumber())
                .referenceNumber(request.getReferenceNumber() != null ? request.getReferenceNumber() : transferReference)
                .sourceAccountNumber(request.getSourceAccountNumber())
                .destinationAccountNumber(request.getDestinationAccountNumber())
                .balanceAfterTransaction(newDestinationBalance)
                .build();

        transactionRepository.save(destinationTransaction);
        Transaction savedSourceTransaction = transactionRepository.save(sourceTransaction);

        log.info("Transfer completed successfully");

        return entityMapper.mapToTransactionDTO(savedSourceTransaction);
    }

    public TransactionDTO getTransactionById(String transactionId) {
        log.info("Fetching transaction with ID: {}", transactionId);
        Transaction transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction", "id", transactionId));

        return entityMapper.mapToTransactionDTO(transaction);
    }

    public List<TransactionDTO> getTransactionsByAccountNumber(String accountNumber) {
        log.info("Fetching transactions for account: {}", accountNumber);

        // Verify account exists
        if (!accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new ResourceNotFoundException("Account", "account number", accountNumber);
        }

        List<Transaction> transactions = transactionRepository.findByAccountAccountNumber(accountNumber);
        return entityMapper.mapToTransactionDTOList(transactions);
    }

    public Map<String, Object> getTransactionsByAccountNumberPaginated(
            String accountNumber, int page, int size, String sortBy, String direction) {
        log.info("Fetching paginated transactions for account: {}", accountNumber);

        // Verify account exists
        if (!accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new ResourceNotFoundException("Account", "account number", accountNumber);
        }

        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, direction);
        Page<Transaction> transactionPage = transactionRepository.findByAccountAccountNumber(accountNumber, pageable);

        return PaginationUtil.createPageResponse(transactionPage);
    }

    public List<TransactionDTO> getTransactionsByDateRange(String accountNumber, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching transactions for account {} between {} and {}", accountNumber, startDate, endDate);

        // Verify account exists
        if (!accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new ResourceNotFoundException("Account", "account number", accountNumber);
        }

        List<Transaction> transactions = transactionRepository.findTransactionsByAccountNumberAndDateRange(
                accountNumber, startDate, endDate);

        return entityMapper.mapToTransactionDTOList(transactions);
    }

    // Helper methods
    private void validateDailyTransactionLimit(Account account, BigDecimal amount, TransactionType type) {
        BigDecimal dailyLimit = account.getDailyTransactionLimit();
        if (dailyLimit != null) {
            BigDecimal dailyTotal = accountService.getDailyTransactionTotal(account.getId(), type);
            if (dailyTotal.add(amount).compareTo(dailyLimit) > 0) {
                throw new InvalidOperationException("Daily transaction limit exceeded");
            }
        }
    }

    private void validateDailyWithdrawalLimit(Account account, BigDecimal amount) {
        BigDecimal withdrawalLimit = account.getDailyWithdrawalLimit();
        if (withdrawalLimit != null) {
            BigDecimal dailyWithdrawalTotal = accountService.getDailyTransactionTotal(account.getId(), TransactionType.WITHDRAWAL);
            BigDecimal dailyTransferTotal = accountService.getDailyTransactionTotal(account.getId(), TransactionType.TRANSFER);

            BigDecimal totalWithdrawals = dailyWithdrawalTotal.add(dailyTransferTotal).add(amount);
            if (totalWithdrawals.compareTo(withdrawalLimit) > 0) {
                throw new InvalidOperationException("Daily withdrawal limit exceeded");
            }
        }
    }
}
