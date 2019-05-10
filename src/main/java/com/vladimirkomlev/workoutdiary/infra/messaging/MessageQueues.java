package com.vladimirkomlev.workoutdiary.infra.messaging;

import com.vladimirkomlev.workoutdiary.infra.email.EmailMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageQueues {
    private final RabbitTemplate rabbitTemplate;
    @Value("${rmq.email.queue-name}")
    private String emailQueueName;

    @Autowired
    public MessageQueues(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enqueueEmail(EmailMessage message) {
        rabbitTemplate.convertAndSend(emailQueueName, message);
    }
}
