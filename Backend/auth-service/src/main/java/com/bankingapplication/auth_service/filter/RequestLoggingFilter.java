package com.bankingapplication.auth_service.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";
    private static final String START_TIME = "startTime";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Generate or extract request ID
            String requestId = extractOrGenerateRequestId(request);
            MDC.put(REQUEST_ID, requestId);

            // Add request ID to response headers for client tracing
            response.addHeader("X-Request-ID", requestId);

            // Record start time for performance logging
            long startTime = System.currentTimeMillis();
            request.setAttribute(START_TIME, startTime);

            // Log incoming request
            logRequest(request);

            // Continue with the filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Log response
            logResponse(request, response);

            // Clean up MDC
            MDC.remove(REQUEST_ID);
        }
    }

    private String extractOrGenerateRequestId(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-ID");
        if (requestId == null || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        return requestId;
    }

    private void logRequest(HttpServletRequest request) {
        String queryString = request.getQueryString() != null ? "?" + request.getQueryString() : "";
        log.info("Received request: {} {} {}", request.getMethod(), request.getRequestURI(), queryString);
    }

    private void logResponse(HttpServletRequest request, HttpServletResponse response) {
        Long startTime = (Long) request.getAttribute(START_TIME);
        if (startTime != null) {
            long duration = System.currentTimeMillis() - startTime;
            log.info("Completed request: {} {} - Status {} - Duration: {}ms",
                    request.getMethod(), request.getRequestURI(), response.getStatus(), duration);
        }
    }
}
