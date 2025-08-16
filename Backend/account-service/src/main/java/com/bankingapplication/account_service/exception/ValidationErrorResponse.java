package com.bankingapplication.account_service.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse {

    private int status;
    private String message;
    private String path;
    private Map<String, String> errors;
    private LocalDateTime timestamp;
}
