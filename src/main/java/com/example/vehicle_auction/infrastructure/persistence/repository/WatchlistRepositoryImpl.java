package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.WatchlistModel;
import com.example.vehicle_auction.domain.repository.WatchlistRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Watchlist;
import com.example.vehicle_auction.infrastructure.persistence.mapper.WatchlistEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaWatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WatchlistRepositoryImpl implements WatchlistRepository {

    private final JpaWatchlistRepository jpaWatchlistRepository;
    private final WatchlistEntityMapper mapper;

    @Override
    public WatchlistModel save(WatchlistModel watchlistModel) {
        Watchlist entity = mapper.toEntity(watchlistModel);
        Watchlist savedEntity = jpaWatchlistRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void deleteByAccountIdAndProductId(UUID accountId, UUID productId) {
        jpaWatchlistRepository.deleteByAccountIdAndProductId(accountId, productId);
    }

    @Override
    public List<WatchlistModel> findByAccountId(UUID accountId) {
        return jpaWatchlistRepository.findByAccountId(accountId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByAccountIdAndProductId(UUID accountId, UUID productId) {
        return jpaWatchlistRepository.existsByAccountIdAndProductId(accountId, productId);
    }
}