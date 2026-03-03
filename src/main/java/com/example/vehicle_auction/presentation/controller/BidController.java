package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.bid.BidRequest;
import com.example.vehicle_auction.application.dto.bid.BidResponse;
import com.example.vehicle_auction.application.usecase.bid.PlaceBidUseCase;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auctions/{auctionId}/bids")
@RequiredArgsConstructor
public class BidController {

    private final PlaceBidUseCase placeBidUseCase;

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
}
