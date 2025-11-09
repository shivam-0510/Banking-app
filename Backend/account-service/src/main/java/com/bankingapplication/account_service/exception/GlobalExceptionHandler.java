package com.bankingapplication.account_service.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        log.error("Resource not found: {} - Reference: {}", ex.getMessage(), errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInvalidOperationException(
            InvalidOperationException ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        log.error("Invalid operation: {} - Reference: {}", ex.getMessage(), errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(
            InsufficientBalanceException ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        log.error("Insufficient balance: {} - Reference: {}", ex.getMessage(), errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("Validation error: {} - Reference: {}", errors, errorReference);

        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation error",
                path,
                errors,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        log.error("Invalid request format: Reference: {}", errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request format. Please check your request body.",
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        String message = String.format("Invalid parameter '%s': '%s' is not a valid %s",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        log.error("{} - Reference: {}", message, errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMissingRequestParameter(
            MissingServletRequestParameterException ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        log.error("Missing request parameter: {} - Reference: {}", ex.getMessage(), errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(
            NoHandlerFoundException ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        String message = String.format("No endpoint found for %s %s", ex.getHttpMethod(), ex.getRequestURL());

        log.error("{} - Reference: {}", message, errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                message,
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolation(
            BusinessRuleViolationException ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        log.error("Business rule violation ({}): {} - Reference: {}",
                ex.getRule(), ex.getMessage(), errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ex.getMessage(),
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        log.error("Access denied: Reference: {}", errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.FORBIDDEN.value(),
                "Access denied. You don't have permission to access this resource.",
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        log.error("Authentication failed: Reference: {}", errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication failed. Invalid credentials.",
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({DataAccessException.class, SQLException.class, JDBCConnectionException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleDatabaseError(
            Exception ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        log.error("Database error: Reference: {}", errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "A database error occurred. Please try again later.",
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {

        String path = extractPath(request);
        String errorReference = generateErrorReference();

        log.error("Unhandled exception: Reference: {}", errorReference, ex);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again later.",
                path,
                errorReference,
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String extractPath(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
            return httpRequest.getRequestURI();
        }
        return request.getDescription(false);
    }

    private String generateErrorReference() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
