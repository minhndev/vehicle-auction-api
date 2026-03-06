package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.NotificationType;

import java.time.LocalDateTime;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void markAsRead() {
        if (!this.isRead) {
            this.isRead = true;
            this.readAt = LocalDateTime.now();
        }
    }
}
