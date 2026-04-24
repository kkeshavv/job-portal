package com.capg.applicationservice.client;

import com.capg.applicationservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JobClientFallbackFactoryTest {

    @Test
    void create_returnsClientThatThrowsResourceNotFoundException() {
        JobClientFallbackFactory factory = new JobClientFallbackFactory();
        JobClient fallback = factory.create(new RuntimeException("Service down"));

        assertThrows(ResourceNotFoundException.class, () -> fallback.getJobById(1L));
    }
}
