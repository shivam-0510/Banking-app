package com.bankingapplication.account_service.util;

import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.bankingapplication.account_service.entity.AccountType;

@Component
public class AccountNumberGenerator {

    private static final Random RANDOM = new Random();

    public String generateAccountNumber(AccountType accountType) {
        // Format: 2-digit account type code + 10-digit random number
        String typeCode = getTypeCode(accountType);
        String randomPart = String.format("%010d", Math.abs(UUID.randomUUID().getLeastSignificantBits() % 10000000000L));
        return typeCode + randomPart;
    }

    public String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }

    public String generateReferenceNumber() {
        return "REF" + System.currentTimeMillis() + RANDOM.nextInt(1000);
    }

    private String getTypeCode(AccountType accountType) {
        switch (accountType) {
            case SAVINGS:
                return "SV";
            case CHECKING:
                return "CK";
            case CREDIT:
                return "CR";
            case LOAN:
                return "LN";
            case INVESTMENT:
                return "IN";
            default:
                return "AC";
        }
    }

    // Luhn algorithm for account number validation
    public boolean validateAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() < 12) {
            return false;
        }

        // Skip the first two characters (account type code)
        String numericPart = accountNumber.substring(2);

        // Check if numeric part contains only digits
        if (!numericPart.matches("\\d+")) {
            return false;
        }

        return true;
    }
}
