package com.bankingapplication.account_service.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankingapplication.account_service.dto.AccountDTO;
import com.bankingapplication.account_service.dto.request.AccountCreationRequest;
import com.bankingapplication.account_service.entity.Account;
import com.bankingapplication.account_service.entity.Transaction;
import com.bankingapplication.account_service.entity.TransactionStatus;
import com.bankingapplication.account_service.entity.TransactionType;
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
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final EntityMapper entityMapper;
    private final AccountNumberGenerator accountNumberGenerator;

    @Transactional
    public AccountDTO createAccount(AccountCreationRequest request) {
        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID is required. Use the /my-account endpoint instead if you want to use the authenticated user's ID.");
        }

        log.info("Creating new account for user {}", request.getUserId());
        log.debug("Account creation request details: {}", request);

        try {
            // Generate a unique account number
            String accountNumber = accountNumberGenerator.generateAccountNumber(request.getAccountType());
            log.debug("Generated account number: {}", accountNumber);

            // Create the account entity
            Account account = entityMapper.mapToAccount(request);
            account.setAccountNumber(accountNumber);
            log.debug("Created account entity: {}", account);

            // Save the account
            Account savedAccount = accountRepository.save(account);
            log.info("Account created successfully with account number: {}", accountNumber);

            // Create initial deposit transaction if amount > 0
            if (request.getInitialDeposit() != null && request.getInitialDeposit().compareTo(BigDecimal.ZERO) > 0) {
                try {
                    log.debug("Creating initial deposit transaction of amount: {}", request.getInitialDeposit());
                    createInitialDepositTransaction(savedAccount, request.getInitialDeposit());
                } catch (Exception e) {
                    log.error("Error creating initial deposit transaction: {}", e.getMessage(), e);
                    throw new RuntimeException("Failed to create initial deposit transaction: " + e.getMessage(), e);
                }
            }

            return entityMapper.mapToAccountDTO(savedAccount);
        } catch (Exception e) {
            log.error("Error creating account: {}", e.getMessage(), e);
            throw e;
        }
    }

    public AccountDTO getAccountByNumber(String accountNumber) {
        log.info("Fetching account with account number: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "account number", accountNumber));

        return entityMapper.mapToAccountDTO(account);
    }

    public List<AccountDTO> getAccountsByUserId(String userId) {
        log.info("Fetching accounts for user: {}", userId);
        List<Account> accounts = accountRepository.findByUserId(userId);
        return entityMapper.mapToAccountDTOList(accounts);
    }

    public Map<String, Object> getAccountsByUserIdPaginated(String userId, int page, int size, String sortBy, String direction) {
        log.info("Fetching paginated accounts for user: {}", userId);
        Pageable pageable = PaginationUtil.createPageable(page, size, sortBy, direction);
        Page<Account> accountPage = accountRepository.findByUserId(userId, pageable);

        return PaginationUtil.createPageResponse(accountPage);
    }

    @Transactional
    public AccountDTO updateAccountStatus(String accountNumber, boolean isActive) {
        log.info("Updating account status for account: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "account number", accountNumber));

        account.setActive(isActive);
        account = accountRepository.save(account);

        log.info("Account status updated successfully for account: {}", accountNumber);
        return entityMapper.mapToAccountDTO(account);
    }

    public BigDecimal getTotalBalanceByUserId(String userId) {
        log.info("Calculating total balance for user: {}", userId);
        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long getAccountCountByUserId(String userId) {
        return accountRepository.countAccountsByUserId(userId);
    }

    public long getTotalAccountsCount() {
        return accountRepository.count();
    }

    private void createInitialDepositTransaction(Account account, BigDecimal amount) {
        try {
            log.debug("Building transaction for account: {}, amount: {}", account.getAccountNumber(), amount);

            String transactionId = accountNumberGenerator.generateTransactionId();
            log.debug("Generated transaction ID: {}", transactionId);

            Transaction transaction = Transaction.builder()
                    .account(account)
                    .transactionId(transactionId)
                    .amount(amount)
                    .transactionType(TransactionType.DEPOSIT)
                    .status(TransactionStatus.COMPLETED)
                    .description("Initial deposit")
                    .sourceAccountNumber(null) // No source account for initial deposit
                    .destinationAccountNumber(account.getAccountNumber())
                    .balanceAfterTransaction(amount)
                    .build();

            log.debug("Built transaction: {}", transaction);
            Transaction savedTransaction = transactionRepository.save(transaction);
            log.info("Initial deposit transaction created for account: {}, transaction ID: {}",
                    account.getAccountNumber(), savedTransaction.getTransactionId());
        } catch (Exception e) {
            log.error("Error in createInitialDepositTransaction: {}", e.getMessage(), e);
            throw e;
        }
    }

    public BigDecimal getDailyTransactionTotal(Long accountId, TransactionType type) {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        BigDecimal total = transactionRepository.sumAmountByAccountAndTypeAndDateRange(
                accountId, type, startOfDay, endOfDay);

        return total != null ? total : BigDecimal.ZERO;
    }
}
