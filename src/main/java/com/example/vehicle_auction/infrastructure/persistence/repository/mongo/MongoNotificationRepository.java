package com.example.vehicle_auction.infrastructure.persistence.repository.mongo;

import com.example.vehicle_auction.infrastructure.persistence.document.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongoNotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByAccountIdOrderByCreatedAtDesc(String accountId);
    long countByAccountIdAndIsReadFalse(String accountId);
}
