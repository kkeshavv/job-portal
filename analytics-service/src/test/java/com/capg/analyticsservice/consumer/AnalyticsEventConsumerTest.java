package com.capg.analyticsservice.consumer;

import com.capg.analyticsservice.service.AnalyticsService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsEventConsumerTest {

    @Mock private AnalyticsService analyticsService;

    @InjectMocks
    private AnalyticsEventConsumer consumer;

    @Test
    void handleJobCreated_incrementsMetric() {
        consumer.handleJobCreated(Map.of("jobId", 1L));
        verify(analyticsService).incrementMetric("JOB_CREATED");
    }

    @Test
    void handleJobApplied_incrementsAllMetrics() {
        Map<String, Object> message = Map.of("jobId", 1L, "candidateId", "seeker@test.com");
        consumer.handleJobApplied(message);

        verify(analyticsService).incrementMetric("JOB_APPLIED");
        verify(analyticsService).incrementJobApplication(1L);
        verify(analyticsService).incrementUserApplication("seeker@test.com");
    }

    @Test
    void handleResumeUploaded_incrementsMetric() {
        consumer.handleResumeUploaded(Map.of());
        verify(analyticsService).incrementMetric("RESUME_UPLOADED");
    }

    @Test
    void handleJobClosed_incrementsMetric() {
        consumer.handleJobClosed(Map.of());
        verify(analyticsService).incrementMetric("JOB_CLOSED");
    }
}
