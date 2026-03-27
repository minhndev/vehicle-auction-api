package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.watchlist.WatchlistResponse;
import com.example.vehicle_auction.application.usecase.watchlist.WatchlistUseCase;
import com.example.vehicle_auction.domain.model.WatchlistModel;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/watchlist")
@RequiredArgsConstructor
@Tag(name = "Watchlist", description = "API for managing user's product watchlist")
public class WatchlistController {
    private final WatchlistUseCase watchlistUseCase;

    private UUID getCurrentAccountId(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getAccount().getId();
    }

    @Operation(summary = "Add a product to the watchlist")
    @PostMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> addToWatchlist(@PathVariable UUID productId, Authentication authentication) {
        UUID accountId = getCurrentAccountId(authentication);
        watchlistUseCase.addToWatchlist(accountId, productId);
        return ResponseEntity.ok("Product added to watchlist.");
    }

    @Operation(summary = "Remove a product from the watchlist")
    @DeleteMapping("/{productId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> removeFromWatchlist(@PathVariable UUID productId, Authentication authentication) {
        UUID accountId = getCurrentAccountId(authentication);
        watchlistUseCase.removeFromWatchlist(accountId, productId);
        return ResponseEntity.ok("Product removed from watchlist.");
    }

    @Operation(summary = "Get my watchlist", description = "Retrieves all products in the current user's watchlist")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<WatchlistResponse>> getMyWatchlist(Authentication authentication) {
        UUID accountId = getCurrentAccountId(authentication);
        List<WatchlistResponse> watchlist = watchlistUseCase.getUserWatchlist(accountId)
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(watchlist);
    }

    private WatchlistResponse toResponse(WatchlistModel watchlistModel) {
        return new WatchlistResponse(
                watchlistModel.getId(),
                watchlistModel.getAccountId(),
                watchlistModel.getProductId(),
                watchlistModel.getCreatedAt()
        );
    }
}