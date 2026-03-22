package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.user.UserResponse;
import com.example.vehicle_auction.domain.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(UserModel userModel);
}
