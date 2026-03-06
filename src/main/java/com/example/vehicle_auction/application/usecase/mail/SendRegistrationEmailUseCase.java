package com.example.vehicle_auction.application.usecase.mail;

import com.example.vehicle_auction.application.port.out.EmailSenderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendRegistrationEmailUseCase {
    private final EmailSenderPort emailSenderPort;

    public void execute(String userEmail,
                        String username,
                        String subject,
                        String body) {
        emailSenderPort.sendEmail(userEmail, subject, body);
    }
}
