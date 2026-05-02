package com.capg.jobservice.service.impl;

import com.capg.jobservice.dto.request.JobRequest;
import com.capg.jobservice.dto.response.JobResponse;
import com.capg.jobservice.dto.JobClosedEvent;
import com.capg.jobservice.dto.JobEvent;
import com.capg.jobservice.entity.Job;
import com.capg.jobservice.exception.JobNotFoundException;
import com.capg.jobservice.exception.UnauthorizedException;
import com.capg.jobservice.repository.JobRepository;
import com.capg.jobservice.service.JobService;
import com.capg.jobservice.mapper.JobMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class JobServiceImpl implements JobService {

    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    private final JobRepository jobRepository;
    private final RabbitTemplate rabbitTemplate;
    private final JobMapper jobMapper;

    private static final String JOB_NOT_FOUND      = "Job not found";
    private static final String EXCHANGE           = "jobportal.exchange";
    private static final String ROUTING_KEY        = "job.created";
    private static final String CLOSED_ROUTING_KEY = "job.closed";

    public JobServiceImpl(JobRepository jobRepository, RabbitTemplate rabbitTemplate, JobMapper jobMapper) {
        this.jobRepository = jobRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.jobMapper = jobMapper;
    }

    // CREATE JOB
    @Override
    @Transactional
    public JobResponse createJob(JobRequest request, String email, String role) {

        if (!"RECRUITER".equals(role)) {
            log.warn("Unauthorized job creation attempt");
            throw new UnauthorizedException("Only recruiters can create jobs");
        }

        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setDescription(request.getDescription());
        job.setJobType(request.getJobType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setCreatedBy(email);
        job.setStatus("OPEN");
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        Job saved = jobRepository.save(job);
        log.info("Job created successfully");

        try {
            JobEvent event = new JobEvent();
            event.setJobId(saved.getJobId());
            event.setTitle(saved.getTitle());
            event.setDescription(saved.getDescription());
            event.setCompany(saved.getCompany());
            event.setLocation(saved.getLocation());
            event.setSalary(saved.getSalary());
            event.setJobType(saved.getJobType());
            event.setExperienceLevel(saved.getExperienceLevel());
            event.setCreatedBy(saved.getCreatedBy());

            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, event);
            log.info("RabbitMQ event published exchange={} routingKey={} jobId={}", EXCHANGE, ROUTING_KEY, saved.getJobId());
        } catch (Exception e) {
            log.error("RabbitMQ publish failed jobId={}", saved.getJobId(), e);
        }

        return jobMapper.toResponse(saved);
    }

    // UPDATE JOB
    @Override
    @Transactional
    public JobResponse updateJob(Long jobId, JobRequest request, String email) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(JOB_NOT_FOUND));
        if (!job.getCreatedBy().equals(email)) {
            throw new UnauthorizedException("You can only edit your own jobs");
        }
        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setDescription(request.getDescription());
        job.setJobType(request.getJobType());
        job.setExperienceLevel(request.getExperienceLevel());
        job.setUpdatedAt(LocalDateTime.now());
        Job saved = jobRepository.save(job);
        log.info("Job updated");
        return jobMapper.toResponse(saved);
    }

    // GET JOB BY ID
    @Override
    @Cacheable(value = "jobs", key = "#jobId")
    public JobResponse getJobById(Long jobId) {
        log.debug("Fetching job from DB");

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> {
                    log.warn("Job not found");
                    return new JobNotFoundException(JOB_NOT_FOUND);
                });

        return jobMapper.toResponse(job);
    }

    // GET ALL JOBS
    @Override
    public Page<JobResponse> getAllJobs(int page, int size) {
        log.debug("Fetching all jobs from DB");
        return jobRepository.findAll(PageRequest.of(page, size))
                .map(jobMapper::toResponse);
    }

    // GET JOBS BY RECRUITER
    @Override
    public Page<JobResponse> getJobsByRecruiter(String email, int page, int size) {
        log.debug("Fetching jobs by recruiter");
        return jobRepository.findByCreatedByOrderByCreatedAtDesc(email, PageRequest.of(page, size))
                .map(jobMapper::toResponse);
    }

    // CLOSE JOB
    @Override
    @Transactional
    public JobResponse closeJob(Long jobId, String role) {

        if (!"RECRUITER".equals(role)) {
            log.warn("Unauthorized job close attempt");
            throw new UnauthorizedException("Only recruiters can close jobs");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> {
                    log.warn("Close failed - job not found");
                    return new JobNotFoundException(JOB_NOT_FOUND);
                });

        job.setStatus("CLOSED");
        job.setUpdatedAt(LocalDateTime.now());

        Job saved = jobRepository.save(job);
        log.info("Job closed successfully");

        try {
            JobClosedEvent event = new JobClosedEvent(saved.getJobId(), saved.getTitle(), "CLOSED", saved.getCreatedBy());
            rabbitTemplate.convertAndSend(EXCHANGE, CLOSED_ROUTING_KEY, event);
            log.info("RabbitMQ event published exchange={} routingKey={} jobId={}", EXCHANGE, CLOSED_ROUTING_KEY, saved.getJobId());
        } catch (Exception e) {
            log.error("RabbitMQ publish failed jobId={}", saved.getJobId(), e);
        }

        return jobMapper.toResponse(saved);
    }
}
