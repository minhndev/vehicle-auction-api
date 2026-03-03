package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.NotificationModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {
    NotificationModel save(NotificationModel model);

    Optional<NotificationModel> findById(UUID id);

    List<NotificationModel> findByAccountId(UUID accountId);

    long countUnreadByAccountId(UUID accountId);
}
