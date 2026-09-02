package com.example.vehicle_auction.infrastructure.messaging.consumer;

import com.example.vehicle_auction.application.port.out.EmailSenderPort;
import com.example.vehicle_auction.infrastructure.configuration.RabbitMQConfig;
import com.example.vehicle_auction.infrastructure.messaging.dto.MailMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailConsumer {

    private final EmailSenderPort emailSenderPort;

    @RabbitListener(queues = {RabbitMQConfig.Q_MAIL_REGISTRATION, RabbitMQConfig.Q_MAIL_DEPOSIT})
    public void consumeMailMessage(MailMessage message) {
        log.info("Consuming mail message for: {}", message.to());
        try {
            emailSenderPort.sendEmail(message.to(), message.subject(), message.body());
            log.info("Successfully sent email to: {}", message.to());
        } catch (Exception e) {
            log.error("Failed to send email to: {}. Error: {}", message.to(), e.getMessage());
            // RabbitMQ will retry based on configuration (default is infinite retry)
            throw e; 
        }
    }
}
