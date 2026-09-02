package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.NotificationModel;
import com.example.vehicle_auction.domain.repository.NotificationRepository;
import com.example.vehicle_auction.infrastructure.persistence.document.Notification;
import com.example.vehicle_auction.infrastructure.persistence.mapper.NotificationDocumentMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.mongo.MongoNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final MongoNotificationRepository mongoNotificationRepository;
    private final NotificationDocumentMapper mapper;

    @Override
    public NotificationModel save(NotificationModel model) {
        Notification entity;
        if (model.getId() != null && mongoNotificationRepository.existsById(model.getId())) {
            entity = mongoNotificationRepository.findById(model.getId()).get();
            mapper.updateEntityFromModel(model, entity);
        } else {
            entity = mapper.toDocument(model);
        }
        return mapper.toDomain(mongoNotificationRepository.save(entity));
    }

    @Override
    public Optional<NotificationModel> findById(String id) {
        return mongoNotificationRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<NotificationModel> findByAccountId(String accountId) {
        return mongoNotificationRepository.findByAccountIdOrderByCreatedAtDesc(accountId).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public long countUnreadByAccountId(String accountId) {
        return mongoNotificationRepository.countByAccountIdAndIsReadFalse(accountId);
    }
}