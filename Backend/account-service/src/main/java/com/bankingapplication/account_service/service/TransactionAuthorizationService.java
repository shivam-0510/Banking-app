package com.bankingapplication.account_service.service;

import org.springframework.stereotype.Service;

import com.bankingapplication.account_service.entity.Transaction;
import com.bankingapplication.account_service.repository.TransactionRepository;
import com.bankingapplication.account_service.security.UserPrincipal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionAuthorizationService {

    private final TransactionRepository transactionRepository;
    private final AccountAuthorizationService accountAuthorizationService;

    public boolean hasTransactionAccess(UserPrincipal userPrincipal, String transactionId) {
        // Admin and Manager roles have access to all transactions
        if (hasAdminOrManagerRole(userPrincipal)) {
            return true;
        }

        // Check if the transaction belongs to an account owned by the user
        return transactionRepository.findByTransactionId(transactionId)
                .map(transaction -> accountAuthorizationService.hasAccountAccess(
                userPrincipal, transaction.getAccount().getAccountNumber()))
                .orElse(false);
    }

    private boolean hasAdminOrManagerRole(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                || auth.getAuthority().equals("ROLE_MANAGER"));
    }
}
