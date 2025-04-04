package com.itshixun.industy.fundusexamination.Utils.RabbitMQ;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// ... 原有import保持不变 ...
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;

@Configuration
public class RabbitMQConfig {

    // 新增交换机和路由键常量
    public static final String IMAGE_PROCESS_EXCHANGE = "image.process.exchange";
    public static final String IMAGE_PROCESS_QUEUE = "image.process.queue";
    public static final String ROUTING_KEY = "image.process.routingkey";

    // 声明直连交换机
    @Bean
    public DirectExchange imageProcessExchange() {
        return new DirectExchange(IMAGE_PROCESS_EXCHANGE);
    }

    // 新增队列与交换机的绑定
    @Bean
    public Binding bindingImageProcess(Queue imageProcessQueue, DirectExchange imageProcessExchange) {
        return BindingBuilder.bind(imageProcessQueue)
                .to(imageProcessExchange)
                .with(ROUTING_KEY);
    }
    // 原有队列声明保持不变
    @Bean
    public Queue imageProcessQueue() {
        return new Queue(IMAGE_PROCESS_QUEUE, true); // 持久化队列
    }
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter()); // 使用JSON转换器
        factory.setDefaultRequeueRejected(false); // 拒绝消息时不重新入队
        return factory;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

