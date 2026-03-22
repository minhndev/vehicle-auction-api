package com.example.vehicle_auction.application.usecase.user;

import com.example.vehicle_auction.application.dto.user.UpdateUserStatusRequest;
import com.example.vehicle_auction.application.dto.user.UserManagementResponse;
import com.example.vehicle_auction.application.dto.user.UserResponse;
import com.example.vehicle_auction.application.mapper.UserMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserUseCase {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse getUserByAccount(UUID accountId) {
        UserModel user = userRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toResponse(user);
    }

    public Page<UserManagementResponse> getAllUsers(Pageable pageable,
                                                    String keyword,
                                                    Boolean active,
                                                    Boolean verified,
                                                    Boolean deleted) {
        return userRepository.findAll(pageable, keyword, active, verified, deleted)
                .map(userMapper::toManagementResponse);
    }

    public UserManagementResponse getUserById(UUID userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, userId));
        return userMapper.toManagementResponse(user);
    }

    public UserManagementResponse updateStatus(UUID userId, UpdateUserStatusRequest request) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, userId));

        if (user.getAccount() == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, userId);
        }

        user.getAccount().setActive(request.active());
        UserModel saved = userRepository.save(user);
        return userMapper.toManagementResponse(saved);
    }
}
