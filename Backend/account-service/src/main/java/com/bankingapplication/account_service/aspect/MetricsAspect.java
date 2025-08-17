package com.bankingapplication.account_service.aspect;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class MetricsAspect {

    private final MeterRegistry meterRegistry;

    @Before("execution(* com.bankingapplication.account_service.service.AccountService.createAccount(..))")
    public void beforeAccountCreation(JoinPoint joinPoint) {
        log.debug("Account creation attempt");
        Counter.builder("banking.account.creation.attempts")
                .description("Number of account creation attempts")
                .register(meterRegistry)
                .increment();
    }

    @AfterReturning("execution(* com.bankingapplication.account_service.service.AccountService.createAccount(..))")
    public void afterAccountCreation(JoinPoint joinPoint) {
        log.info("Account created successfully");
        Counter.builder("banking.account.creation.success")
                .description("Number of successful account creations")
                .register(meterRegistry)
                .increment();
    }

    @AfterThrowing(
            pointcut = "execution(* com.bankingapplication.account_service.service.AccountService.createAccount(..))",
            throwing = "ex")
    public void afterAccountCreationFailure(JoinPoint joinPoint, Exception ex) {
        log.error("Account creation failed: {}", ex.getMessage());
        Counter.builder("banking.account.creation.failures")
                .description("Number of failed account creations")
                .tag("exception", ex.getClass().getSimpleName())
                .register(meterRegistry)
                .increment();
    }

    @Before("execution(* com.bankingapplication.account_service.service.TransactionService.processTransaction(..))")
    public void beforeTransactionProcessing(JoinPoint joinPoint) {
        log.debug("Transaction processing attempt");
        Counter.builder("banking.transaction.processing.attempts")
                .description("Number of transaction processing attempts")
                .register(meterRegistry)
                .increment();
    }

    @AfterReturning("execution(* com.bankingapplication.account_service.service.TransactionService.processTransaction(..))")
    public void afterTransactionProcessing(JoinPoint joinPoint) {
        log.info("Transaction processed successfully");
        Counter.builder("banking.transaction.processing.success")
                .description("Number of successful transaction processings")
                .register(meterRegistry)
                .increment();
    }

    @AfterThrowing(
            pointcut = "execution(* com.bankingapplication.account_service.service.TransactionService.processTransaction(..))",
            throwing = "ex")
    public void afterTransactionProcessingFailure(JoinPoint joinPoint, Exception ex) {
        log.error("Transaction processing failed: {}", ex.getMessage());
        Counter.builder("banking.transaction.processing.failures")
                .description("Number of failed transaction processings")
                .tag("exception", ex.getClass().getSimpleName())
                .register(meterRegistry)
                .increment();
    }
}
