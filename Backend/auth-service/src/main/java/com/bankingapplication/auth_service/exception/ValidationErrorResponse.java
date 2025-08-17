package com.bankingapplication.auth_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorResponse {

    private int status;
    private String message;
    private String path;
    private Map<String, String> errors;
    private String errorReference;
    private LocalDateTime timestamp;
}
