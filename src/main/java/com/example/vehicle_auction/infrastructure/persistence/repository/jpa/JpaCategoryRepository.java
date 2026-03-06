package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.infrastructure.persistence.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaCategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);
}