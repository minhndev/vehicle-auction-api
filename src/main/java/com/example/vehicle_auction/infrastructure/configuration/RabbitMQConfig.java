package com.example.vehicle_auction.infrastructure.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String AUCTION_EXCHANGE = "auction.exchange";
    public static final String DELAYED_EXCHANGE = "auction.delayed.exchange";

    public static final String Q_MAIL_REGISTRATION = "q.mail.registration";
    public static final String Q_MAIL_DEPOSIT = "q.mail.deposit";
    public static final String Q_REFUND_PROCESS = "q.refund.process";
    public static final String Q_AUCTION_LIFECYCLE = "q.auction.lifecycle";

    public static final String RK_MAIL_REGISTRATION = "mail.registration";
    public static final String RK_MAIL_DEPOSIT = "mail.deposit";
    public static final String RK_REFUND_PROCESS = "refund.process";
    public static final String RK_AUCTION_LIFECYCLE = "auction.lifecycle";

    @Bean
    public TopicExchange auctionExchange() {
        return new TopicExchange(AUCTION_EXCHANGE);
    }

    /**
     * Delayed Message Exchange for Auction Lifecycle (requires plugin)
     */
    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE, "x-delayed-message", true, false, args);
    }

    @Bean
    public Queue mailRegistrationQueue() {
        return QueueBuilder.durable(Q_MAIL_REGISTRATION).build();
    }

    @Bean
    public Queue mailDepositQueue() {
        return QueueBuilder.durable(Q_MAIL_DEPOSIT).build();
    }

    @Bean
    public Queue refundProcessQueue() {
        return QueueBuilder.durable(Q_REFUND_PROCESS).build();
    }

    @Bean
    public Queue auctionLifecycleQueue() {
        return QueueBuilder.durable(Q_AUCTION_LIFECYCLE).build();
    }

    @Bean
    public Binding mailRegistrationBinding(Queue mailRegistrationQueue, TopicExchange auctionExchange) {
        return BindingBuilder.bind(mailRegistrationQueue).to(auctionExchange).with(RK_MAIL_REGISTRATION);
    }

    @Bean
    public Binding mailDepositBinding(Queue mailDepositQueue, TopicExchange auctionExchange) {
        return BindingBuilder.bind(mailDepositQueue).to(auctionExchange).with(RK_MAIL_DEPOSIT);
    }

    @Bean
    public Binding refundProcessBinding(Queue refundProcessQueue, TopicExchange auctionExchange) {
        return BindingBuilder.bind(refundProcessQueue).to(auctionExchange).with(RK_REFUND_PROCESS);
    }

    @Bean
    public Binding auctionLifecycleBinding(Queue auctionLifecycleQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(auctionLifecycleQueue).to(delayedExchange).with(RK_AUCTION_LIFECYCLE).noargs();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new org.springframework.amqp.rabbit.core.RabbitAdmin(connectionFactory);
    }
}
