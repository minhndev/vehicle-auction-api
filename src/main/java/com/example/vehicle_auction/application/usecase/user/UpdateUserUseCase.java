package com.example.vehicle_auction.application.usecase.user;

import com.example.vehicle_auction.application.dto.user.UpdateUserRequest;
import com.example.vehicle_auction.application.dto.user.UserManagementResponse;
import com.example.vehicle_auction.application.mapper.UserMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.RoleModel;
import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.RoleRepository;
import com.example.vehicle_auction.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserManagementResponse execute(UUID id, UpdateUserRequest req) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.updateProfile(req.firstName(), req.lastName(), req.phoneNumber(), req.address());

        if (req.avatarURL() != null && !req.avatarURL().isBlank())
            user.setAvatarURL(req.avatarURL());

        if (req.roleNames() != null && !req.roleNames().isEmpty() && user.getAccount() != null) {
            Set<RoleModel> roles = req.roleNames().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND)))
                    .collect(Collectors.toSet());

            user.getAccount().setRoles(roles);
        }

        user.setUpdatedAt(LocalDateTime.now());

        UserModel savedUser = userRepository.save(user);

        return userMapper.toManagementResponse(savedUser);
    }
}
