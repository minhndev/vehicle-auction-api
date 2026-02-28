package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        uses = {AccountMapper.class}, // Rất quan trọng: Gọi AccountMapper để xử lý biến 'account'
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
    UserModel toDomain(User entity);
    User toEntity(UserModel domain);
}
