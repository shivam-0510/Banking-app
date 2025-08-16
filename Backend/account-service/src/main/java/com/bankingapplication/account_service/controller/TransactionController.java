package com.bankingapplication.account_service.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bankingapplication.account_service.dto.TransactionDTO;
import com.bankingapplication.account_service.dto.request.DepositRequest;
import com.bankingapplication.account_service.dto.request.TransferRequest;
import com.bankingapplication.account_service.dto.request.WithdrawalRequest;
import com.bankingapplication.account_service.dto.response.ApiResponse;
import com.bankingapplication.account_service.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction API", description = "Endpoints for managing bank transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TELLER') or @accountAuthorizationService.hasAccountAccess(authentication.principal, #request.accountNumber)")
    @Operation(summary = "Deposit funds", description = "Deposit funds into an account")
    public ResponseEntity<ApiResponse<TransactionDTO>> deposit(@Valid @RequestBody DepositRequest request) {
        TransactionDTO transaction = transactionService.deposit(request);
        return new ResponseEntity<>(ApiResponse.success("Deposit completed successfully", transaction), HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TELLER') or @accountAuthorizationService.hasAccountAccess(authentication.principal, #request.accountNumber)")
    @Operation(summary = "Withdraw funds", description = "Withdraw funds from an account")
    public ResponseEntity<ApiResponse<TransactionDTO>> withdraw(@Valid @RequestBody WithdrawalRequest request) {
        TransactionDTO transaction = transactionService.withdraw(request);
        return new ResponseEntity<>(ApiResponse.success("Withdrawal completed successfully", transaction), HttpStatus.CREATED);
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TELLER') or @accountAuthorizationService.hasAccountAccess(authentication.principal, #request.sourceAccountNumber)")
    @Operation(summary = "Transfer funds", description = "Transfer funds between accounts")
    public ResponseEntity<ApiResponse<TransactionDTO>> transfer(@Valid @RequestBody TransferRequest request) {
        TransactionDTO transaction = transactionService.transfer(request);
        return new ResponseEntity<>(ApiResponse.success("Transfer completed successfully", transaction), HttpStatus.CREATED);
    }

    @GetMapping("/{transactionId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or @transactionAuthorizationService.hasTransactionAccess(authentication.principal, #transactionId)")
    @Operation(summary = "Get transaction by ID", description = "Retrieve transaction details by transaction ID")
    public ResponseEntity<ApiResponse<TransactionDTO>> getTransactionById(@PathVariable String transactionId) {
        TransactionDTO transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(ApiResponse.success(transaction));
    }

    @GetMapping("/account/{accountNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or @accountAuthorizationService.hasAccountAccess(authentication.principal, #accountNumber)")
    @Operation(summary = "Get transactions by account", description = "Retrieve all transactions for a specific account")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByAccountNumber(@PathVariable String accountNumber) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByAccountNumber(accountNumber);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/account/{accountNumber}/paginated")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or @accountAuthorizationService.hasAccountAccess(authentication.principal, #accountNumber)")
    @Operation(summary = "Get paginated transactions by account", description = "Retrieve paginated transactions for a specific account")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTransactionsByAccountNumberPaginated(
            @PathVariable String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Map<String, Object> response = transactionService.getTransactionsByAccountNumberPaginated(accountNumber, page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/account/{accountNumber}/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or @accountAuthorizationService.hasAccountAccess(authentication.principal, #accountNumber)")
    @Operation(summary = "Get transactions by date range", description = "Retrieve transactions for a specific account within a date range")
    public ResponseEntity<ApiResponse<List<TransactionDTO>>> getTransactionsByDateRange(
            @PathVariable String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByDateRange(accountNumber, startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }
}
