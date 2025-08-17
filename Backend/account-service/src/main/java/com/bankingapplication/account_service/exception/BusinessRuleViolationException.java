package com.bankingapplication.account_service.exception;

public class BusinessRuleViolationException extends RuntimeException {

    private final String rule;

    public BusinessRuleViolationException(String message, String rule) {
        super(message);
        this.rule = rule;
    }

    public BusinessRuleViolationException(String message, String rule, Throwable cause) {
        super(message, cause);
        this.rule = rule;
    }

    public String getRule() {
        return rule;
    }
}
