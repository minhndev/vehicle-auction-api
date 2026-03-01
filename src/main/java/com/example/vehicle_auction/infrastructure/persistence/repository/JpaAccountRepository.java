package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.infrastructure.persistence.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaAccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);
}
