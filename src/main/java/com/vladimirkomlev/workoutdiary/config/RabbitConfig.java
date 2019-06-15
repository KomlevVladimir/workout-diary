package com.vladimirkomlev.workoutdiary.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${rmq.email.queue-name}")
    private String emailQueueName;
    @Value("${rmq.email.exchange-name}")
    private String emailExchangeName;

    @Bean
    public Jackson2JsonMessageConverter jackson2MessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public Queue emailsQueue() {
        return QueueBuilder.durable(emailQueueName).build();
    }

    @Bean
    public Exchange emailsExchange() {
        return ExchangeBuilder.topicExchange(emailExchangeName).build();
    }

    @Bean
    public Binding emailBinding(Queue emailsQueue, Exchange emailsExchange) {
        return BindingBuilder.bind(emailsQueue).to(emailsExchange).with(emailQueueName).noargs();
    }
}