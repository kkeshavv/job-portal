package com.capg.resumeservice.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResumeEventTest {

    @Test
    void resumeEvent_allGettersSettersWork() {
        ResumeEvent event = new ResumeEvent("resume-123", "seeker@test.com", "/uploads/test.pdf");
        assertEquals("resume-123", event.getResumeId());
        assertEquals("seeker@test.com", event.getUserEmail());
        assertEquals("/uploads/test.pdf", event.getFileUrl());

        event.setResumeId("resume-456");
        event.setUserEmail("other@test.com");
        event.setFileUrl("/uploads/other.pdf");
        assertEquals("resume-456", event.getResumeId());
        assertEquals("other@test.com", event.getUserEmail());
        assertEquals("/uploads/other.pdf", event.getFileUrl());
    }

    @Test
    void resumeEvent_defaultConstructor_works() {
        ResumeEvent event = new ResumeEvent();
        assertNull(event.getResumeId());
        assertNull(event.getUserEmail());
        assertNull(event.getFileUrl());
    }
}
