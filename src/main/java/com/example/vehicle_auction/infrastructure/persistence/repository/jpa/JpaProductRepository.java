package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.infrastructure.persistence.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaProductRepository extends JpaRepository<Product, UUID> {
    boolean existsByVinNumber(String vinNumber);

    Optional<Product> findByIdAndDeletedFalse(UUID id);

    Page<Product> findAllByDeletedFalse(Pageable pageable);

    Optional<Product> findByIdAndDeletedTrue(UUID id);

    Page<Product> findAllBySellerIdAndDeletedFalse(UUID sellerId, Pageable pageable);
}