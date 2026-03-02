package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.auction.AuctionRequest;
import com.example.vehicle_auction.application.dto.auction.AuctionResponse;
import com.example.vehicle_auction.application.usecase.auction.CreateAuctionUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
@Tag(name = "Auction Management", description = "APIs for managing auctions")
public class AuctionController {

    private final CreateAuctionUseCase createAuctionUseCase;

    @Operation(
            summary = "Create a new auction session",
            description = "Allows Admin to create a new auction for an APPROVED product. " +
                    "The auction will start in UPCOMING status until the start time is reached."
    )
    @ApiResponse(responseCode = "201", description = "Auction session created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid time, overlapping auctions, or invalid product status")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PostMapping
    @PreAuthorize("hasAuthority('AUCTION_CREATE')")
    public ResponseEntity<AuctionResponse> createAuction(@RequestBody @Valid AuctionRequest request) {
        return ResponseEntity.ok(createAuctionUseCase.execute(request));
    }
}
