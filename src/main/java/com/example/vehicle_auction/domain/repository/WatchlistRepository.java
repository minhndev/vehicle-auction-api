package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.WatchlistModel;

import java.util.List;
import java.util.UUID;

public interface WatchlistRepository {
    WatchlistModel save(WatchlistModel watchlistModel);
    void deleteByAccountIdAndProductId(UUID accountId, UUID productId);
    List<WatchlistModel> findByAccountId(UUID accountId);
    boolean existsByAccountIdAndProductId(UUID accountId, UUID productId);
}
