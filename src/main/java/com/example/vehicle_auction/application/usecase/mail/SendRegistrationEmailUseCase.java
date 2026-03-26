package com.example.vehicle_auction.application.usecase.mail;

import com.example.vehicle_auction.infrastructure.configuration.RabbitMQConfig;
import com.example.vehicle_auction.infrastructure.messaging.RabbitMQProducer;
import com.example.vehicle_auction.infrastructure.messaging.dto.MailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendRegistrationEmailUseCase {
    private final RabbitMQProducer rabbitMQProducer;

    public void execute(String userEmail,
                        String username,
                        String subject,
                        String body) {
        MailMessage message = new MailMessage(userEmail, subject, body);
        rabbitMQProducer.sendMessage(RabbitMQConfig.RK_MAIL_REGISTRATION, message);
    }
}
