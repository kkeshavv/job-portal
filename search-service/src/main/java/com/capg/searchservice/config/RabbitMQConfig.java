package com.capg.searchservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE   = "jobportal.exchange";
    public static final String DLX        = "jobportal.dlx";
    public static final String JOB_QUEUE  = "job.created.search.queue";

    @Bean public TopicExchange exchange() { return new TopicExchange(EXCHANGE); }
    @Bean public DirectExchange deadLetterExchange() { return new DirectExchange(DLX); }

    // ── Main Queue ───────────────────────────────────────────────────────────

    @Bean public Queue jobQueue() {
        return QueueBuilder.durable(JOB_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", JOB_QUEUE + ".dlq")
                .build();
    }

    // ── DLQ ─────────────────────────────────────────────────────────────────

    @Bean public Queue jobDlq() { return new Queue(JOB_QUEUE + ".dlq"); }

    // ── Main Binding ─────────────────────────────────────────────────────────

    @Bean public Binding jobCreatedBinding() {
        return BindingBuilder.bind(jobQueue()).to(exchange()).with("job.created");
    }

    // ── DLQ Binding ──────────────────────────────────────────────────────────

    @Bean public Binding jobDlqBinding() {
        return BindingBuilder.bind(jobDlq()).to(deadLetterExchange()).with(JOB_QUEUE + ".dlq");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
