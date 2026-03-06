package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.NotificationModel;
import com.example.vehicle_auction.infrastructure.persistence.document.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationDocumentMapper {
    NotificationModel toDomain(Notification entity);
    Notification toDocument(NotificationModel domain);
    void updateEntityFromModel(NotificationModel domain, @MappingTarget Notification entity);
}
