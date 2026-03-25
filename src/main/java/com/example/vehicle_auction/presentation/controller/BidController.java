package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.bid.BidRequest;
import com.example.vehicle_auction.application.dto.bid.BidHistoryItemResponse;
import com.example.vehicle_auction.application.dto.bid.BidResponse;
import com.example.vehicle_auction.application.usecase.bid.GetBidHistoryUseCase;
import com.example.vehicle_auction.application.usecase.bid.PlaceBidUseCase;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auctions/{auctionId}/bids")
@RequiredArgsConstructor
public class BidController {

    private final PlaceBidUseCase placeBidUseCase;
    private final GetBidHistoryUseCase getBidUseCase;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<BidResponse> placeBid(
            @PathVariable UUID auctionId,
            @Valid @RequestBody BidRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        UUID bidderId = userDetails.getAccount().getId();

        BidResponse response = placeBidUseCase.execute(auctionId, request, bidderId);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get Top 10 Bids Timeline (masked bidder identity)")
    @GetMapping
    public ResponseEntity<List<BidHistoryItemResponse>> getAllBids(@PathVariable UUID auctionId) {
        return ResponseEntity.ok(getBidUseCase.getTop10Bids(auctionId));
    }


}
