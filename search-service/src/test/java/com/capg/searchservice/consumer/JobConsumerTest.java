package com.capg.searchservice.consumer;

import com.capg.searchservice.dto.JobClosedEvent;
import com.capg.searchservice.dto.JobEvent;
import com.capg.searchservice.entity.Job;
import com.capg.searchservice.repository.JobRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobConsumerTest {

    @Mock private JobRepository repository;

    @InjectMocks
    private JobConsumer jobConsumer;

    @Test
    void handleJobCreated_savesJobToIndex() {
        JobEvent event = new JobEvent();
        event.setJobId(1L);
        event.setTitle("Java Developer");
        event.setCompany("TechCorp");
        event.setLocation("Bangalore");
        event.setSalary(1000000.0);
        event.setDescription("Spring Boot developer");
        event.setSkills("Java, Spring");

        jobConsumer.handleJobCreated(event);

        ArgumentCaptor<Job> captor = ArgumentCaptor.forClass(Job.class);
        verify(repository).save(captor.capture());
        assertEquals(1L, captor.getValue().getJobId());
        assertEquals("Java Developer", captor.getValue().getTitle());
        assertEquals("OPEN", captor.getValue().getStatus());
    }

    @Test
    void handleJobClosed_existingJob_updatesStatusToClosed() {
        Job job = new Job();
        job.setJobId(1L);
        job.setStatus("OPEN");

        when(repository.findById(1L)).thenReturn(Optional.of(job));

        JobClosedEvent event = new JobClosedEvent();
        event.setJobId(1L);
        event.setTitle("Java Developer");

        jobConsumer.handleJobClosed(event);

        ArgumentCaptor<Job> captor = ArgumentCaptor.forClass(Job.class);
        verify(repository).save(captor.capture());
        assertEquals("CLOSED", captor.getValue().getStatus());
    }

    @Test
    void handleJobClosed_jobNotFound_doesNotSave() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        JobClosedEvent event = new JobClosedEvent();
        event.setJobId(99L);

        jobConsumer.handleJobClosed(event);

        verify(repository, never()).save(any());
    }
}
