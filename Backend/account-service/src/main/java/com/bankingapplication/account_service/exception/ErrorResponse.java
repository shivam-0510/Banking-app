package com.bankingapplication.account_service.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private int status;
    private String message;
    private String path;
    private String errorReference;
    private LocalDateTime timestamp;

    // Constructors
    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message, String path, String errorReference, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.errorReference = errorReference;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getErrorReference() {
        return errorReference;
    }

    public void setErrorReference(String errorReference) {
        this.errorReference = errorReference;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
