package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    UserModel save(UserModel userModel);

    Optional<UserModel> findByAccountId(UUID accountId);

    Optional<UserModel> findById(UUID userId);

    Optional<UserModel> findByIdAndDeletedFalse(UUID userId);

    Optional<UserModel> findByIdAndDeletedTrue(UUID userId);

    Page<UserModel> findAll(Pageable pageable, String keyword, Boolean active, Boolean verified, Boolean deleted);
}