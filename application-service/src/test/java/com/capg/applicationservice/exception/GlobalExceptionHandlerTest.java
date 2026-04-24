package com.capg.applicationservice.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_returns404() {
        ResponseEntity<ErrorResponse> response = handler.handleNotFound(new ResourceNotFoundException("Not found"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody().getMessage());
    }

    @Test
    void handleConflict_returns409() {
        ResponseEntity<ErrorResponse> response = handler.handleConflict(new AlreadyAppliedException("Already applied"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Already applied", response.getBody().getMessage());
    }

    @Test
    void handleUnauthorized_returns403() {
        ResponseEntity<ErrorResponse> response = handler.handleUnauthorized(new UnauthorizedException("Forbidden"));
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Forbidden", response.getBody().getMessage());
    }

    @Test
    void handleInvalidStatus_returns400() {
        ResponseEntity<ErrorResponse> response = handler.handleInvalidStatus(new InvalidStatusException("Invalid status"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid status", response.getBody().getMessage());
    }

    @Test
    void handleAlreadyRejected_returns400() {
        ResponseEntity<ErrorResponse> response = handler.handleAlreadyRejected(new AlreadyRejectedException("Already rejected"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Already rejected", response.getBody().getMessage());
    }

    @Test
    void handleGeneric_returns500() {
        ResponseEntity<ErrorResponse> response = handler.handleGeneric(new Exception("Error"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error", response.getBody().getMessage());
    }
}
