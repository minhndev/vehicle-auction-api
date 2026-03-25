package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationModel {
    private String id;
    private String accountId;
    private NotificationType type;
    private String title;
    private String content;
    private String referenceId;
    private String referenceType;
    private boolean isRead;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;

    public NotificationModel() {}

    public NotificationModel(String id,
                             String accountId,
                             NotificationType type,
                             String title,
                             String content,
                             String referenceId,
                             String referenceType,
                             boolean isRead,
                             LocalDateTime readAt,
                             LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.type = type;
        this.title = title;
        this.content = content;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.isRead = isRead;
        this.readAt = readAt;
        this.createdAt = createdAt;
    }

    public void markAsRead() {
        if (!this.isRead) {
            this.isRead = true;
            this.readAt = LocalDateTime.now();
        }
    }
}
