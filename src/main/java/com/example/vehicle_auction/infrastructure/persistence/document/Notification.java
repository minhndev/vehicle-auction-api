package com.example.vehicle_auction.infrastructure.persistence.document;

import com.example.vehicle_auction.domain.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "notifications")
@Getter
@Setter
public class Notification {
    @Id
    private String id;

    private NotificationType type;

    private String title;

    private String content;

    @Field(name = "reference_id")
    private UUID referenceId;

    @Field(name = "reference_type")
    private String referenceType;

    @Field(name = "is_read")
    private boolean isRead = false;

    @Field(name = "read_at")
    private LocalDateTime readAt;

    @Indexed
    @Field(name = "account_id")
    private UUID accountId;

    @CreatedDate
    @Field(name = "created_at")
    private LocalDateTime createdAt;
}
