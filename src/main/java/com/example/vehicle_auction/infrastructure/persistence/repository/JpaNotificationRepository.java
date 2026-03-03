package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.infrastructure.persistence.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaNotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByAccountIdOrderByCreatedAtDesc(UUID accountId);
    long countByAccountIdAndIsReadFalse(UUID accountId);
}
