package com.example.vehicle_auction.infrastructure.messaging.dto;

import java.io.Serializable;

public record MailMessage(
        String to,
        String subject,
        String body
) implements Serializable {
}
