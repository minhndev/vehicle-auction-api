package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.bid.BidResponse;
import com.example.vehicle_auction.application.usecase.bid.GetMyBidsUseCase;
import com.example.vehicle_auction.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bids")
@RequiredArgsConstructor
@Tag(name = "User Bids", description = "Manage user bids across all auctions")
public class UserBidController {

    private final GetMyBidsUseCase getMyBidsUseCase;

    @Operation(summary = "Get My Bids", description = "Get a paginated list of all bids placed by the authenticated user across all auctions.")
    @GetMapping("/my-bids")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<BidResponse>> getMyBids(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Pageable pageable
    ) {
        Page<BidResponse> response = getMyBidsUseCase.execute(userDetails.getAccount().getId(), pageable);
        return ResponseEntity.ok(response);
    }
}
