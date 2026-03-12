package com.example.vehicle_auction.presentation.controller;

import com.example.vehicle_auction.application.dto.auction.AuctionFilterRequest;
import com.example.vehicle_auction.application.dto.auction.AuctionRequest;
import com.example.vehicle_auction.application.dto.auction.AuctionResponse;
import com.example.vehicle_auction.application.dto.auction.CancelAuctionRequest;
import com.example.vehicle_auction.application.usecase.auction.CancelAuctionUseCase;
import com.example.vehicle_auction.application.usecase.auction.CreateAuctionUseCase;
import com.example.vehicle_auction.application.usecase.auction.SearchAuctionsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auctions")
@RequiredArgsConstructor
@Tag(name = "Auction Management", description = "APIs for managing auctions")
public class AuctionController {

    private final CreateAuctionUseCase createAuctionUseCase;
    private final SearchAuctionsUseCase searchAuctionsUseCase;
    private final CancelAuctionUseCase cancelAuctionUseCase;

    @Operation(
            summary = "Create a new auction session",
            description = "Allows Admin to create a new auction for an APPROVED product. " +
                    "The auction will start in UPCOMING status until the start time is reached."
    )
    @ApiResponse(responseCode = "201", description = "Auction session created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid time, overlapping auctions, or invalid product status")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PostMapping
//    @PreAuthorize("hasAuthority('AUCTION_CREATE')")
    public ResponseEntity<AuctionResponse> createAuction(@RequestBody @Valid AuctionRequest request) {
        return ResponseEntity.ok(createAuctionUseCase.execute(request));
    }

    @Operation(
            summary = "Search and filter auction sessions",
            description = "Allows users to search for auction sessions based on various criteria such as status, price range, keyword, and category. " +
                    "Supports pagination for efficient data retrieval."
    )
    @ApiResponse(responseCode = "200", description = "Auctions retrieved successfully")
    @GetMapping
    public ResponseEntity<Page<AuctionResponse>> searchAuctions(
            @ParameterObject @ModelAttribute @Valid AuctionFilterRequest request,
            @ParameterObject Pageable pageable) {

        Page<AuctionResponse> pageResult =  searchAuctionsUseCase.execute(request, pageable);

        return ResponseEntity.ok(pageResult);
    }

    @Operation(
            summary = "Cancel an auction session",
            description = "Allows Admin to cancel an UPCOMING or ACTIVE auction. This action will automatically trigger deposit refunds for all participating users."
    )
    @ApiResponse(responseCode = "200", description = "Auction cancelled successfully")
    @ApiResponse(responseCode = "400", description = "Auction cannot be cancelled in its current status")
    @ApiResponse(responseCode = "404", description = "Auction not found")
    @PostMapping("/{id}/cancel")
    // @PreAuthorize("hasAuthority('AUCTION_CANCEL')") // Mở ra khi bạn làm phân quyền
    public ResponseEntity<Void> cancelAuction(
            @PathVariable("id") java.util.UUID id,
            @RequestBody @Valid CancelAuctionRequest request) {

        cancelAuctionUseCase.execute(id, request.reason());

        return ResponseEntity.ok().build();
    }

}
