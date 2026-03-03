package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.NotificationModel;
import com.example.vehicle_auction.domain.repository.NotificationRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Notification;
import com.example.vehicle_auction.infrastructure.persistence.mapper.NotificationEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final JpaNotificationRepository jpaRepository;
    private final NotificationEntityMapper mapper;

    @Override
    public NotificationModel save(NotificationModel model) {
        Notification entity;
        if (model.getId() != null && jpaRepository.existsById(model.getId())) {
            entity = jpaRepository.findById(model.getId()).get();
            mapper.updateEntityFromModel(model, entity);
        } else {
            entity = mapper.toEntity(model);
        }
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<NotificationModel> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<NotificationModel> findByAccountId(UUID accountId) {
        return jpaRepository.findByAccountIdOrderByCreatedAtDesc(accountId).stream()
                .map(mapper::toDomain).toList();
    }

    @Override
    public long countUnreadByAccountId(UUID accountId) {
        return jpaRepository.countByAccountIdAndIsReadFalse(accountId);
    }
}