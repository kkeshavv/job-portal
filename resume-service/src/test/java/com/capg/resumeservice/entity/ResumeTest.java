package com.capg.resumeservice.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ResumeTest {

    @Test
    void resume_allGettersSettersWork() {
        LocalDateTime now = LocalDateTime.now();
        Resume resume = new Resume();
        resume.setResumeId(1L);
        resume.setUserEmail("seeker@test.com");
        resume.setFileUrl("/uploads/test.pdf");
        resume.setUploadedAt(now);

        assertEquals(1L, resume.getResumeId());
        assertEquals("seeker@test.com", resume.getUserEmail());
        assertEquals("/uploads/test.pdf", resume.getFileUrl());
        assertEquals(now, resume.getUploadedAt());
    }
}
