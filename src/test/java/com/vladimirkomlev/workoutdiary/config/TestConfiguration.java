package com.vladimirkomlev.workoutdiary.config;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import com.vladimirkomlev.workoutdiary.service.WorkoutServiceIntegrationTest;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({WorkoutServiceIntegrationTest.class})
class TestConfiguration {

    @Bean
    ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(new MockConnectionFactory());
    }
}
