package com.capg.analyticsservice.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    @Test
    void jobApplicationMetrics_gettersSettersWork() {
        JobApplicationMetrics metrics = new JobApplicationMetrics(1L, 5L);
        assertEquals(1L, metrics.getJobId());
        assertEquals(5L, metrics.getApplicationCount());

        metrics.setJobId(2L);
        metrics.setApplicationCount(10L);
        assertEquals(2L, metrics.getJobId());
        assertEquals(10L, metrics.getApplicationCount());
    }

    @Test
    void userApplicationMetrics_gettersSettersWork() {
        UserApplicationMetrics metrics = new UserApplicationMetrics("user@test.com", 3L);
        assertEquals("user@test.com", metrics.getUserEmail());
        assertEquals(3L, metrics.getApplicationCount());

        metrics.setUserEmail("other@test.com");
        metrics.setApplicationCount(6L);
        assertEquals("other@test.com", metrics.getUserEmail());
        assertEquals(6L, metrics.getApplicationCount());
    }

    @Test
    void metricsSummary_gettersSettersWork() {
        MetricsSummary summary = new MetricsSummary(1L, "JOB_CREATED", 10L);
        assertEquals(1L, summary.getId());
        assertEquals("JOB_CREATED", summary.getMetricName());
        assertEquals(10L, summary.getCount());

        summary.setMetricName("JOB_APPLIED");
        summary.setCount(20L);
        assertEquals("JOB_APPLIED", summary.getMetricName());
        assertEquals(20L, summary.getCount());
    }
}
