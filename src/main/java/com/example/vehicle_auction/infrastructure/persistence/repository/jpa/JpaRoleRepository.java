package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.infrastructure.persistence.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaRoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}
