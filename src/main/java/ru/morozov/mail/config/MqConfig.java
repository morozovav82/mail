package ru.morozov.mail.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    @Value("${mq.OrderReady-topic}")
    private String orderReadyTopic;

    @Value("${mq.OrderCanceled-topic}")
    private String orderCanceledTopic;

    @Bean
    public Queue orderReadyQueue() {
        return new Queue(orderReadyTopic);
    }

    @Bean
    public Queue orderCanceledQueue() {
        return new Queue(orderCanceledTopic);
    }
}
