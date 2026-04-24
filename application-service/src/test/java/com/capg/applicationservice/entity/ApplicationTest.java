package com.capg.applicationservice.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void application_allGettersSettersWork() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Application app = new Application(id, 1L, "seeker@test.com", ApplicationStatus.APPLIED, now);

        assertEquals(id, app.getApplicationId());
        assertEquals(1L, app.getJobId());
        assertEquals("seeker@test.com", app.getUserEmail());
        assertEquals(ApplicationStatus.APPLIED, app.getStatus());
        assertEquals(now, app.getAppliedAt());

        UUID newId = UUID.randomUUID();
        app.setApplicationId(newId);
        app.setJobId(2L);
        app.setUserEmail("other@test.com");
        app.setStatus(ApplicationStatus.SHORTLISTED);
        app.setAppliedAt(now);

        assertEquals(newId, app.getApplicationId());
        assertEquals(2L, app.getJobId());
        assertEquals("other@test.com", app.getUserEmail());
        assertEquals(ApplicationStatus.SHORTLISTED, app.getStatus());
    }
}
