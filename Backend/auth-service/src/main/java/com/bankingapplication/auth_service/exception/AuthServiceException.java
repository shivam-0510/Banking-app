package com.bankingapplication.auth_service.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class AuthServiceException extends RuntimeException {

    private final HttpStatus status;

    public AuthServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
