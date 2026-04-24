package com.capg.applicationservice.exception;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void errorResponse_gettersReturnCorrectValues() {
        LocalDateTime now = LocalDateTime.now();
        ErrorResponse response = new ErrorResponse(now, 409, "Conflict", "Already applied");

        assertEquals(now, response.getTimestamp());
        assertEquals(409, response.getStatus());
        assertEquals("Conflict", response.getError());
        assertEquals("Already applied", response.getMessage());
    }
}
