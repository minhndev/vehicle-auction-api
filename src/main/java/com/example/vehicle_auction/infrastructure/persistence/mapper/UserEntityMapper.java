package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        uses = {AccountEntityMapper.class}, // Rất quan trọng: Gọi AccountEntityMapper để xử lý biến 'account'
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserEntityMapper {
    UserModel toDomain(User entity);
    User toEntity(UserModel domain);
}
