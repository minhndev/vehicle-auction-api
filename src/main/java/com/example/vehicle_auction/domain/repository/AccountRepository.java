package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.AccountModel;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    boolean existsByEmail(String email);

    AccountModel save(AccountModel accountModel);

    Optional<AccountModel> findByEmail(String email);

    Optional<AccountModel> findById(UUID id);

    Optional<AccountModel> findByVerificationToken(String token);

    Optional<AccountModel> findByResetPasswordToken(String token);
}
