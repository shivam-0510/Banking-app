package com.bankingapplication.account_service.dto.request;

import java.math.BigDecimal;

import com.bankingapplication.account_service.entity.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationRequest {

    // Changed from @NotBlank to make it optional for the /my-account endpoint
    private String userId;

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Initial deposit amount is required")
    @Positive(message = "Initial deposit amount must be positive")
    private BigDecimal initialDeposit;

    @NotBlank(message = "Currency is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO currency code (e.g., USD, EUR)")
    private String currency;

    // Optional limits can be overridden
    private BigDecimal dailyTransactionLimit;
    private BigDecimal dailyWithdrawalLimit;
    private Double interestRate;
    private BigDecimal overdraftLimit;
    private BigDecimal minimumBalance;
}
