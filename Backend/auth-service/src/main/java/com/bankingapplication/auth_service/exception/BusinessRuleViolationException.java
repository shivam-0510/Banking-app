package com.bankingapplication.auth_service.exception;

import org.springframework.http.HttpStatus;

public class BusinessRuleViolationException extends RuntimeException {

    private final String rule;
    private final HttpStatus status;

    public BusinessRuleViolationException(String message, String rule) {
        super(message);
        this.rule = rule;
        this.status = HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public BusinessRuleViolationException(String message, String rule, Throwable cause) {
        super(message, cause);
        this.rule = rule;
        this.status = HttpStatus.UNPROCESSABLE_ENTITY;
    }

    public String getRule() {
        return rule;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
