package com.capg.jobservice.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class JobTest {

    @Test
    void job_allGettersSettersWork() {
        LocalDateTime now = LocalDateTime.now();
        Job job = new Job(1L, "Java Developer", "TechCorp", "Bangalore",
                1000000.0, "Spring Boot", "OPEN", "recruiter@test.com", now, now);

        assertEquals(1L, job.getJobId());
        assertEquals("Java Developer", job.getTitle());
        assertEquals("TechCorp", job.getCompany());
        assertEquals("Bangalore", job.getLocation());
        assertEquals(1000000.0, job.getSalary());
        assertEquals("Spring Boot", job.getDescription());
        assertEquals("OPEN", job.getStatus());
        assertEquals("recruiter@test.com", job.getCreatedBy());
        assertEquals(now, job.getCreatedAt());
        assertEquals(now, job.getUpdatedAt());

        job.setJobId(2L);
        job.setTitle("Python Developer");
        job.setCompany("NewCorp");
        job.setLocation("Mumbai");
        job.setSalary(900000.0);
        job.setDescription("Django");
        job.setStatus("CLOSED");
        job.setCreatedBy("other@test.com");
        job.setCreatedAt(now);
        job.setUpdatedAt(now);

        assertEquals(2L, job.getJobId());
        assertEquals("Python Developer", job.getTitle());
        assertEquals("CLOSED", job.getStatus());
    }
}
