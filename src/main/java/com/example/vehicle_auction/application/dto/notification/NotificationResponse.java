package com.example.vehicle_auction.application.dto.notification;

import com.example.vehicle_auction.domain.enums.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(
        String id,
        String accountId,
        NotificationType type,
        String title,
        String content,
        String referenceId,
        String referenceType,
        boolean read,
        LocalDateTime readAt,
        LocalDateTime createdAt
) {
}

