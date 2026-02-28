package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.UserModel;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    UserModel save(UserModel userModel);

    Optional<UserModel> findByAccountId(UUID accountId);
}