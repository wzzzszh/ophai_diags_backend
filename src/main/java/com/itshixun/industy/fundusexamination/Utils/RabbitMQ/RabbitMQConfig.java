package com.itshixun.industy.fundusexamination.Utils.RabbitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String IMAGE_PROCESS_QUEUE = "image.process.queue";

    @Bean
    public Queue imageProcessQueue() {
        return new Queue(IMAGE_PROCESS_QUEUE, true); // 持久化队列
    }
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

