package com.capg.notificationservice.consumer;

import com.capg.notificationservice.dto.ApplicationEvent;
import com.capg.notificationservice.dto.JobClosedEvent;
import com.capg.notificationservice.dto.JobEvent;
import com.capg.notificationservice.dto.ResumeEvent;
import com.capg.notificationservice.service.EmailService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationConsumerTest {

    @Mock private EmailService emailService;

    @InjectMocks
    private NotificationConsumer notificationConsumer;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(notificationConsumer, "adminEmail", "admin@test.com");
    }

    @Test
    void handleJobCreated_sendsEmail() {
        JobEvent event = new JobEvent();
        event.setJobId(1L);
        event.setTitle("Java Developer");
        event.setCompany("TechCorp");
        event.setLocation("Bangalore");
        event.setSalary(1000000.0);
        event.setCreatedBy("recruiter@test.com");

        notificationConsumer.handleJobCreated(event);

        verify(emailService).send(eq("recruiter@test.com"), contains("Java Developer"), anyString());
    }

    @Test
    void handleJobApplied_sendsEmail() {
        ApplicationEvent event = new ApplicationEvent();
        event.setApplicationId("app-123");
        event.setJobId(1L);
        event.setUserEmail("seeker@test.com");
        event.setStatus("APPLIED");

        notificationConsumer.handleJobApplied(event);

        verify(emailService).send(eq("seeker@test.com"), eq("Application Received"), anyString());
    }

    @Test
    void handleJobClosed_sendsEmail() {
        JobClosedEvent event = new JobClosedEvent();
        event.setJobId(1L);
        event.setTitle("Java Developer");
        event.setStatus("CLOSED");
        event.setCreatedBy("recruiter@test.com");

        notificationConsumer.handleJobClosed(event);

        verify(emailService).send(eq("recruiter@test.com"), contains("Java Developer"), anyString());
    }

    @Test
    void handleResumeUploaded_sendsEmail() {
        ResumeEvent event = new ResumeEvent();
        event.setResumeId("resume-123");
        event.setUserEmail("seeker@test.com");
        event.setFileUrl("/uploads/resumes/test.pdf");

        notificationConsumer.handleResumeUploaded(event);

        verify(emailService).send(eq("seeker@test.com"), eq("Resume Uploaded Successfully"), anyString());
    }
}
