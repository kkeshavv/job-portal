package com.capg.jobservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE        = "jobportal.exchange";
    public static final String DLX             = "jobportal.dlx";
    public static final String JOB_CREATED_KEY = "job.created";
    public static final String JOB_CLOSED_KEY  = "job.closed";

    @Bean public TopicExchange exchange() { return new TopicExchange(EXCHANGE); }
    @Bean public DirectExchange deadLetterExchange() { return new DirectExchange(DLX); }

    // ── Main Queues ──────────────────────────────────────────────────────────

    @Bean public Queue jobCreatedAnalyticsQueue() {
        return QueueBuilder.durable("job.created.analytics.queue")
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", "job.created.analytics.queue.dlq")
                .build();
    }
    @Bean public Queue jobCreatedNotifyQueue() {
        return QueueBuilder.durable("job.created.notify.queue")
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", "job.created.notify.queue.dlq")
                .build();
    }
    @Bean public Queue jobCreatedSearchQueue() {
        return QueueBuilder.durable("job.created.search.queue")
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", "job.created.search.queue.dlq")
                .build();
    }
    @Bean public Queue jobClosedAnalyticsQueue() {
        return QueueBuilder.durable("job.closed.analytics.queue")
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", "job.closed.analytics.queue.dlq")
                .build();
    }
    @Bean public Queue jobClosedNotifyQueue() {
        return QueueBuilder.durable("job.closed.notify.queue")
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", "job.closed.notify.queue.dlq")
                .build();
    }

    // ── DLQs ────────────────────────────────────────────────────────────────

    @Bean public Queue jobCreatedAnalyticsDlq() { return new Queue("job.created.analytics.queue.dlq"); }
    @Bean public Queue jobCreatedNotifyDlq()    { return new Queue("job.created.notify.queue.dlq"); }
    @Bean public Queue jobCreatedSearchDlq()    { return new Queue("job.created.search.queue.dlq"); }
    @Bean public Queue jobClosedAnalyticsDlq()  { return new Queue("job.closed.analytics.queue.dlq"); }
    @Bean public Queue jobClosedNotifyDlq()     { return new Queue("job.closed.notify.queue.dlq"); }

    // ── Main Bindings ────────────────────────────────────────────────────────

    @Bean public Binding jobCreatedAnalyticsBinding() {
        return BindingBuilder.bind(jobCreatedAnalyticsQueue()).to(exchange()).with(JOB_CREATED_KEY);
    }
    @Bean public Binding jobCreatedNotifyBinding() {
        return BindingBuilder.bind(jobCreatedNotifyQueue()).to(exchange()).with(JOB_CREATED_KEY);
    }
    @Bean public Binding jobCreatedSearchBinding() {
        return BindingBuilder.bind(jobCreatedSearchQueue()).to(exchange()).with(JOB_CREATED_KEY);
    }
    @Bean public Binding jobClosedAnalyticsBinding() {
        return BindingBuilder.bind(jobClosedAnalyticsQueue()).to(exchange()).with(JOB_CLOSED_KEY);
    }
    @Bean public Binding jobClosedNotifyBinding() {
        return BindingBuilder.bind(jobClosedNotifyQueue()).to(exchange()).with(JOB_CLOSED_KEY);
    }

    // ── DLQ Bindings ─────────────────────────────────────────────────────────

    @Bean public Binding jobCreatedAnalyticsDlqBinding() {
        return BindingBuilder.bind(jobCreatedAnalyticsDlq()).to(deadLetterExchange()).with("job.created.analytics.queue.dlq");
    }
    @Bean public Binding jobCreatedNotifyDlqBinding() {
        return BindingBuilder.bind(jobCreatedNotifyDlq()).to(deadLetterExchange()).with("job.created.notify.queue.dlq");
    }
    @Bean public Binding jobCreatedSearchDlqBinding() {
        return BindingBuilder.bind(jobCreatedSearchDlq()).to(deadLetterExchange()).with("job.created.search.queue.dlq");
    }
    @Bean public Binding jobClosedAnalyticsDlqBinding() {
        return BindingBuilder.bind(jobClosedAnalyticsDlq()).to(deadLetterExchange()).with("job.closed.analytics.queue.dlq");
    }
    @Bean public Binding jobClosedNotifyDlqBinding() {
        return BindingBuilder.bind(jobClosedNotifyDlq()).to(deadLetterExchange()).with("job.closed.notify.queue.dlq");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
