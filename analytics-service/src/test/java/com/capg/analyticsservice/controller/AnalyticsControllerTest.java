package com.capg.analyticsservice.controller;

import com.capg.analyticsservice.entity.JobApplicationMetrics;
import com.capg.analyticsservice.entity.UserApplicationMetrics;
import com.capg.analyticsservice.service.AnalyticsService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsControllerTest {

    @Mock private AnalyticsService analyticsService;

    @InjectMocks
    private AnalyticsController analyticsController;

    @Test
    void getSummary_returnsMap() {
        when(analyticsService.getSummary()).thenReturn(Map.of("JOB_CREATED", 10L, "JOB_APPLIED", 25L));

        Map<String, Long> result = analyticsController.getSummary();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(10L, result.get("JOB_CREATED"));
        verify(analyticsService).getSummary();
    }

    @Test
    void getJobMetrics_returnsPage() {
        JobApplicationMetrics metrics = new JobApplicationMetrics(1L, 5L);
        Page<JobApplicationMetrics> page = new PageImpl<>(List.of(metrics), PageRequest.of(0, 10), 1);
        when(analyticsService.getJobMetrics(0, 10)).thenReturn(page);

        Page<JobApplicationMetrics> result = analyticsController.getJobMetrics(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(analyticsService).getJobMetrics(0, 10);
    }

    @Test
    void getUserMetrics_returnsPage() {
        UserApplicationMetrics metrics = new UserApplicationMetrics("seeker@test.com", 3L);
        Page<UserApplicationMetrics> page = new PageImpl<>(List.of(metrics), PageRequest.of(0, 10), 1);
        when(analyticsService.getUserMetrics(0, 10)).thenReturn(page);

        Page<UserApplicationMetrics> result = analyticsController.getUserMetrics(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(analyticsService).getUserMetrics(0, 10);
    }
}
