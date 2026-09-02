package com.example.vehicle_auction.infrastructure.messaging;

import com.example.vehicle_auction.infrastructure.configuration.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String routingKey, Object message) {
        log.info("Sending message to RabbitMQ with routing key [{}]: {}", routingKey, message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.AUCTION_EXCHANGE, routingKey, message);
    }

    /**
     * Sends a message with a delay (requires x-delayed-message exchange)
     * @param delayMs Delay in milliseconds
     */
    public void sendDelayedMessage(String routingKey, Object message, long delayMs) {
        log.info("Sending delayed message ({}ms) to RabbitMQ with routing key [{}]: {}", delayMs, routingKey, message);
        rabbitTemplate.convertAndSend(RabbitMQConfig.DELAYED_EXCHANGE, routingKey, message, m -> {
            m.getMessageProperties().setHeader("x-delay", (int) delayMs);
            return m;
        });
    }
}
