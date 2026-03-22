package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.user.UserManagementResponse;
import com.example.vehicle_auction.application.dto.user.UserResponse;
import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.domain.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(UserModel userModel);

    @Mapping(target = "fullName", expression = "java(userModel.getFullName())")
    @Mapping(target = "email", expression = "java(userModel.getAccount() != null ? userModel.getAccount().getEmail() : null)")
    @Mapping(target = "active", expression = "java(userModel.getAccount() != null && userModel.getAccount().isActive())")
    @Mapping(target = "verified", expression = "java(userModel.getAccount() != null && userModel.getAccount().isVerified())")
    @Mapping(target = "roles", expression = "java(mapRoleNames(userModel))")
    UserManagementResponse toManagementResponse(UserModel userModel);

    default List<String> mapRoleNames(UserModel userModel) {
        if (userModel.getAccount() == null || userModel.getAccount().getRoles() == null) {
            return Collections.emptyList();
        }
        return userModel.getAccount().getRoles().stream()
                .map(RoleModel::getName)
                .filter(name -> name != null && !name.isBlank())
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }
}
