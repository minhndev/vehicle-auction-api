package com.example.vehicle_auction.infrastructure.persistence.entity;

import com.example.vehicle_auction.domain.enums.NotificationType;
import com.example.vehicle_auction.infrastructure.persistence.entity.base.AuditEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification extends AuditEntity {
    @Column(name = "type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;
}
