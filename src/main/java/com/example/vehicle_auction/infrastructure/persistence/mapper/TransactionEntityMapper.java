package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.TransactionModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransactionEntityMapper {
    Transaction toEntity(TransactionModel model);

    TransactionModel toDomain(Transaction entity);

    void updateEntityFromModel(TransactionModel model, @MappingTarget Transaction entity);
}
