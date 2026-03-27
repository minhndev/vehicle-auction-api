package com.example.vehicle_auction.application.usecase.watchlist;

import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.WatchlistModel;
import com.example.vehicle_auction.domain.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WatchlistUseCase {

    private final WatchlistRepository watchlistRepository;

    @Transactional
    public void addToWatchlist(UUID accountId, UUID productId) {
        if (watchlistRepository.existsByAccountIdAndProductId(accountId, productId)) {
            throw new AppException(ErrorCode.WATCHLIST_ALREADY_EXISTS);
        }

        WatchlistModel watchlist = new WatchlistModel();
        watchlist.setAccountId(accountId);
        watchlist.setProductId(productId);

        watchlistRepository.save(watchlist);
    }

    @Transactional
    public void removeFromWatchlist(UUID accountId, UUID productId) {
        watchlistRepository.deleteByAccountIdAndProductId(accountId, productId);
    }

    public List<WatchlistModel> getUserWatchlist(UUID accountId) {
        return watchlistRepository.findByAccountId(accountId);
    }
}