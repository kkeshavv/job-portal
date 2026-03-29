package com.capg.applicationservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE        = "jobportal.exchange";
    public static final String DLX             = "jobportal.dlx";
    public static final String JOB_APPLIED_KEY = "job.applied";

    @Bean public TopicExchange exchange() { return new TopicExchange(EXCHANGE); }
    @Bean public DirectExchange deadLetterExchange() { return new DirectExchange(DLX); }

    // ── Main Queues ──────────────────────────────────────────────────────────

    @Bean public Queue jobAppliedAnalyticsQueue() {
        return QueueBuilder.durable("job.applied.analytics.queue")
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", "job.applied.analytics.queue.dlq")
                .build();
    }
    @Bean public Queue jobAppliedNotifyQueue() {
        return QueueBuilder.durable("job.applied.notify.queue")
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", "job.applied.notify.queue.dlq")
                .build();
    }

    // ── DLQs ────────────────────────────────────────────────────────────────

    @Bean public Queue jobAppliedAnalyticsDlq() { return new Queue("job.applied.analytics.queue.dlq"); }
    @Bean public Queue jobAppliedNotifyDlq()    { return new Queue("job.applied.notify.queue.dlq"); }

    // ── Main Bindings ────────────────────────────────────────────────────────

    @Bean public Binding jobAppliedAnalyticsBinding() {
        return BindingBuilder.bind(jobAppliedAnalyticsQueue()).to(exchange()).with(JOB_APPLIED_KEY);
    }
    @Bean public Binding jobAppliedNotifyBinding() {
        return BindingBuilder.bind(jobAppliedNotifyQueue()).to(exchange()).with(JOB_APPLIED_KEY);
    }

    // ── DLQ Bindings ─────────────────────────────────────────────────────────

    @Bean public Binding jobAppliedAnalyticsDlqBinding() {
        return BindingBuilder.bind(jobAppliedAnalyticsDlq()).to(deadLetterExchange()).with("job.applied.analytics.queue.dlq");
    }
    @Bean public Binding jobAppliedNotifyDlqBinding() {
        return BindingBuilder.bind(jobAppliedNotifyDlq()).to(deadLetterExchange()).with("job.applied.notify.queue.dlq");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
