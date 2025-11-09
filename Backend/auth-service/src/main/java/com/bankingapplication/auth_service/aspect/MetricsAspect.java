package com.bankingapplication.auth_service.aspect;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MetricsAspect {

    private static final Logger log = LoggerFactory.getLogger(MetricsAspect.class);

    private final MeterRegistry meterRegistry;

    public MetricsAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Before("execution(* com.bankingapplication.auth_service.service.AuthService.login(..))")
    public void beforeLogin(JoinPoint joinPoint) {
        log.debug("Login attempt");
        Counter.builder("banking.auth.login.attempts")
                .description("Number of login attempts")
                .register(meterRegistry)
                .increment();
    }

    @AfterReturning("execution(* com.bankingapplication.auth_service.service.AuthService.login(..))")
    public void afterSuccessfulLogin(JoinPoint joinPoint) {
        log.info("Login successful");
        Counter.builder("banking.auth.login.success")
                .description("Number of successful logins")
                .register(meterRegistry)
                .increment();
    }

    @AfterThrowing(
            pointcut = "execution(* com.bankingapplication.auth_service.service.AuthService.login(..))",
            throwing = "ex")
    public void afterFailedLogin(JoinPoint joinPoint, Exception ex) {
        log.error("Login failed: {}", ex.getMessage());
        Counter.builder("banking.auth.login.failures")
                .description("Number of failed logins")
                .tag("exception", ex.getClass().getSimpleName())
                .register(meterRegistry)
                .increment();
    }

    @Before("execution(* com.bankingapplication.auth_service.service.AuthService.register(..))")
    public void beforeRegistration(JoinPoint joinPoint) {
        log.debug("Registration attempt");
        Counter.builder("banking.auth.registration.attempts")
                .description("Number of registration attempts")
                .register(meterRegistry)
                .increment();
    }

    @AfterReturning("execution(* com.bankingapplication.auth_service.service.AuthService.register(..))")
    public void afterSuccessfulRegistration(JoinPoint joinPoint) {
        log.info("Registration successful");
        Counter.builder("banking.auth.registration.success")
                .description("Number of successful registrations")
                .register(meterRegistry)
                .increment();
    }

    @AfterThrowing(
            pointcut = "execution(* com.bankingapplication.auth_service.service.AuthService.register(..))",
            throwing = "ex")
    public void afterFailedRegistration(JoinPoint joinPoint, Exception ex) {
        log.error("Registration failed: {}", ex.getMessage());
        Counter.builder("banking.auth.registration.failures")
                .description("Number of failed registrations")
                .tag("exception", ex.getClass().getSimpleName())
                .register(meterRegistry)
                .increment();
    }

    @Before("execution(* com.bankingapplication.auth_service.service.TokenService.refreshToken(..))")
    public void beforeTokenRefresh(JoinPoint joinPoint) {
        log.debug("Token refresh attempt");
        Counter.builder("banking.auth.token.refresh.attempts")
                .description("Number of token refresh attempts")
                .register(meterRegistry)
                .increment();
    }

    @AfterReturning("execution(* com.bankingapplication.auth_service.service.TokenService.refreshToken(..))")
    public void afterSuccessfulTokenRefresh(JoinPoint joinPoint) {
        log.info("Token refresh successful");
        Counter.builder("banking.auth.token.refresh.success")
                .description("Number of successful token refreshes")
                .register(meterRegistry)
                .increment();
    }

    @AfterThrowing(
            pointcut = "execution(* com.bankingapplication.auth_service.service.TokenService.refreshToken(..))",
            throwing = "ex")
    public void afterFailedTokenRefresh(JoinPoint joinPoint, Exception ex) {
        log.error("Token refresh failed: {}", ex.getMessage());
        Counter.builder("banking.auth.token.refresh.failures")
                .description("Number of failed token refreshes")
                .tag("exception", ex.getClass().getSimpleName())
                .register(meterRegistry)
                .increment();
    }
}
