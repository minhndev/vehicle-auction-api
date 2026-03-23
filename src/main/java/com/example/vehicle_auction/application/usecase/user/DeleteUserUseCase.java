package com.example.vehicle_auction.application.usecase.user;

import com.example.vehicle_auction.application.dto.user.UserManagementResponse;
import com.example.vehicle_auction.application.mapper.UserMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.UserModel;
import com.example.vehicle_auction.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void execute(UUID id) {
        UserModel user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.softDelete();
        userRepository.save(user);
    }
}
