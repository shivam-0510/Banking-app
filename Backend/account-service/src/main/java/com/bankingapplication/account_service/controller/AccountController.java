package com.bankingapplication.account_service.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bankingapplication.account_service.dto.AccountDTO;
import com.bankingapplication.account_service.dto.request.AccountCreationRequest;
import com.bankingapplication.account_service.dto.response.ApiResponse;
import com.bankingapplication.account_service.entity.AccountType;
import com.bankingapplication.account_service.security.CurrentUser;
import com.bankingapplication.account_service.security.UserPrincipal;
import com.bankingapplication.account_service.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account API", description = "Endpoints for managing bank accounts")
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Create a new account", description = "Create a new bank account with the specified details (Admin/Manager only)")
    public ResponseEntity<ApiResponse<AccountDTO>> createAccount(@Valid @RequestBody AccountCreationRequest request) {
        try {
            log.info("Received request to create account: {}", request);

            // Validate account type
            try {
                AccountType.valueOf(request.getAccountType().toString());
            } catch (IllegalArgumentException e) {
                log.error("Invalid account type: {}", request.getAccountType());
                return new ResponseEntity<>(
                        ApiResponse.error("Invalid account type: " + request.getAccountType()),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Validate currency
            if (request.getCurrency() == null || !request.getCurrency().matches("^[A-Z]{3}$")) {
                log.error("Invalid currency: {}", request.getCurrency());
                return new ResponseEntity<>(
                        ApiResponse.error("Invalid currency. Must be a 3-letter ISO currency code."),
                        HttpStatus.BAD_REQUEST
                );
            }

            AccountDTO account = accountService.createAccount(request);
            log.info("Account created successfully with ID: {}", account.getId());
            return new ResponseEntity<>(ApiResponse.success("Account created successfully", account), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create account: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    ApiResponse.error("Failed to create account: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/my-account")
    @Operation(summary = "Create a new account for authenticated user", description = "Create a new bank account for the current authenticated user")
    public ResponseEntity<ApiResponse<AccountDTO>> createMyAccount(@Valid @RequestBody AccountCreationRequest request,
            @CurrentUser UserPrincipal currentUser) {
        try {
            log.info("Received request to create account for authenticated user: {}", currentUser.getUsername());

            // Override userId with the authenticated user's username since userId is not available in token
            // In this banking system, we use username as user identifier
            request.setUserId(currentUser.getUsername());
            log.debug("Set userId to {} from authenticated user", request.getUserId());

            // Validate account type
            try {
                AccountType.valueOf(request.getAccountType().toString());
            } catch (IllegalArgumentException e) {
                log.error("Invalid account type: {}", request.getAccountType());
                return new ResponseEntity<>(
                        ApiResponse.error("Invalid account type: " + request.getAccountType()),
                        HttpStatus.BAD_REQUEST
                );
            }

            // Validate currency
            if (request.getCurrency() == null || !request.getCurrency().matches("^[A-Z]{3}$")) {
                log.error("Invalid currency: {}", request.getCurrency());
                return new ResponseEntity<>(
                        ApiResponse.error("Invalid currency. Must be a 3-letter ISO currency code."),
                        HttpStatus.BAD_REQUEST
                );
            }

            AccountDTO account = accountService.createAccount(request);
            log.info("Account created successfully with ID: {}", account.getId());
            return new ResponseEntity<>(ApiResponse.success("Account created successfully", account), HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Failed to create account: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    ApiResponse.error("Failed to create account: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or @accountAuthorizationService.hasAccountAccess(authentication.principal, #accountNumber)")
    @Operation(summary = "Get account by number", description = "Retrieve account details by account number")
    public ResponseEntity<ApiResponse<AccountDTO>> getAccountByNumber(@PathVariable String accountNumber) {
        AccountDTO account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(ApiResponse.success(account));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or #userId == authentication.principal.username")
    @Operation(summary = "Get accounts by user ID", description = "Retrieve all accounts owned by a specific user")
    public ResponseEntity<ApiResponse<List<AccountDTO>>> getAccountsByUserId(@PathVariable String userId) {
        List<AccountDTO> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @GetMapping("/user/{userId}/paginated")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or #userId == authentication.principal.username")
    @Operation(summary = "Get paginated accounts by user ID", description = "Retrieve paginated accounts owned by a specific user")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAccountsByUserIdPaginated(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Map<String, Object> response = accountService.getAccountsByUserIdPaginated(userId, page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{accountNumber}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @Operation(summary = "Update account status", description = "Activate or deactivate an account")
    public ResponseEntity<ApiResponse<AccountDTO>> updateAccountStatus(
            @PathVariable String accountNumber,
            @RequestParam boolean isActive) {
        AccountDTO account = accountService.updateAccountStatus(accountNumber, isActive);
        String message = isActive ? "Account activated successfully" : "Account deactivated successfully";
        return ResponseEntity.ok(ApiResponse.success(message, account));
    }

    @GetMapping("/my-accounts")
    @Operation(summary = "Get current user's accounts", description = "Retrieve all accounts owned by the current authenticated user")
    public ResponseEntity<ApiResponse<List<AccountDTO>>> getMyAccounts(@CurrentUser UserPrincipal currentUser) {
        List<AccountDTO> accounts = accountService.getAccountsByUserId(currentUser.getUsername());
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @GetMapping("/public/health")
    @Operation(summary = "Public health check endpoint", description = "Check if the account service is up and running")
    public ResponseEntity<ApiResponse<String>> healthCheck() {
        log.info("Health check endpoint called");
        return ResponseEntity.ok(ApiResponse.success("Account service is up and running"));
    }

    @GetMapping("/public/database")
    @Operation(summary = "Public database check endpoint", description = "Check if the database connection is working")
    public ResponseEntity<ApiResponse<String>> databaseCheck() {
        try {
            log.info("Database check endpoint called");
            long count = accountService.getTotalAccountsCount();
            return ResponseEntity.ok(ApiResponse.success("Database connection successful. Total accounts: " + count));
        } catch (Exception e) {
            log.error("Database check failed: {}", e.getMessage(), e);
            return new ResponseEntity<>(
                    ApiResponse.error("Database connection failed: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/public/account-types")
    @Operation(summary = "Get available account types", description = "Retrieve a list of all available account types")
    public ResponseEntity<ApiResponse<List<String>>> getAccountTypes() {
        log.info("Account types endpoint called");
        List<String> accountTypes = List.of(
                AccountType.SAVINGS.name(),
                AccountType.CHECKING.name(),
                AccountType.CREDIT.name(),
                AccountType.LOAN.name(),
                AccountType.INVESTMENT.name()
        );
        return ResponseEntity.ok(ApiResponse.success("Available account types", accountTypes));
    }

    @GetMapping("/public/requirements")
    @Operation(summary = "Get account creation requirements", description = "Retrieve the requirements for creating a new account")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAccountRequirements() {
        log.info("Account requirements endpoint called");

        Map<String, Object> legacyRequest = Map.of(
                "userId", "user123",
                "accountType", "SAVINGS",
                "initialDeposit", 1000.00,
                "currency", "USD",
                "dailyTransactionLimit", 5000.00,
                "dailyWithdrawalLimit", 2000.00,
                "interestRate", 0.015,
                "minimumBalance", 100.00
        );

        Map<String, Object> newRequest = Map.of(
                "accountType", "SAVINGS",
                "initialDeposit", 1000.00,
                "currency", "USD",
                "dailyTransactionLimit", 5000.00,
                "dailyWithdrawalLimit", 2000.00,
                "interestRate", 0.015,
                "minimumBalance", 100.00
        );

        Map<String, Object> requirements = Map.of(
                "note", "We now recommend using /api/accounts/my-account endpoint which automatically gets userId from the authenticated user",
                "requiredFields", List.of("accountType", "initialDeposit", "currency"),
                "optionalFields", List.of("dailyTransactionLimit", "dailyWithdrawalLimit", "interestRate", "overdraftLimit", "minimumBalance"),
                "accountTypes", List.of("SAVINGS", "CHECKING", "CREDIT", "LOAN", "INVESTMENT"),
                "exampleLegacyRequest", legacyRequest,
                "exampleNewRequest", newRequest,
                "endpoints", Map.of(
                        "/api/accounts", "Legacy endpoint - requires userId in request body",
                        "/api/accounts/my-account", "New endpoint - automatically uses authenticated user's ID"
                )
        );

        return ResponseEntity.ok(ApiResponse.success("Account creation requirements", requirements));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        log.error("Validation error: {}", errors);
        return new ResponseEntity<>(ApiResponse.error("Validation error", errors), HttpStatus.BAD_REQUEST);
    }
}
