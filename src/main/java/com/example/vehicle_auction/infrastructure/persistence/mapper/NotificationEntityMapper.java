package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.NotificationModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationEntityMapper {
    NotificationModel toDomain(Notification entity);
    Notification toEntity(NotificationModel domain);
    void updateEntityFromModel(NotificationModel domain, @MappingTarget Notification entity);
}
