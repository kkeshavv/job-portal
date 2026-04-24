package com.capg.resumeservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_returns404() {
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(new ResumeNotFoundException("Not found"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody().getMessage());
    }

    @Test
    void handleUnauthorized_returns403() {
        ResponseEntity<ErrorResponse> response = handler.handleUnauthorized(new UnauthorizedException("Forbidden"));
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Forbidden", response.getBody().getMessage());
    }

    @Test
    void handleGeneric_returns500() {
        ResponseEntity<ErrorResponse> response = handler.handleGeneric(new Exception("Error"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error", response.getBody().getMessage());
    }
}
