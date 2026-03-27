package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.infrastructure.persistence.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaWatchlistRepository extends JpaRepository<Watchlist, UUID> {
    void deleteByAccountIdAndProductId(UUID accountId, UUID productId);
    List<Watchlist> findByAccountId(UUID accountId);
    boolean existsByAccountIdAndProductId(UUID accountId, UUID productId);
}
