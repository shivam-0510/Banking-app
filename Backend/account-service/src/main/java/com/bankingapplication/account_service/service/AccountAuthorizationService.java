package com.bankingapplication.account_service.service;

import org.springframework.stereotype.Service;

import com.bankingapplication.account_service.entity.Account;
import com.bankingapplication.account_service.repository.AccountRepository;
import com.bankingapplication.account_service.security.UserPrincipal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AccountAuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(AccountAuthorizationService.class);

    private final AccountRepository accountRepository;

    public AccountAuthorizationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean hasAccountAccess(UserPrincipal userPrincipal, String accountNumber) {
        // Admin and Manager roles have access to all accounts
        if (hasAdminOrManagerRole(userPrincipal)) {
            return true;
        }

        // Check if the account belongs to the user
        return accountRepository.findByAccountNumber(accountNumber)
                .map(account -> account.getUserId().equals(userPrincipal.getUsername()))
                .orElse(false);
    }

    private boolean hasAdminOrManagerRole(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")
                || auth.getAuthority().equals("ROLE_MANAGER"));
    }
}
