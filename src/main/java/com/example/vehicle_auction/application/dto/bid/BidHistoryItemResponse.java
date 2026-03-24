package com.example.vehicle_auction.application.dto.bid;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Bid history item for auction timeline (sensitive bidder identity is masked)")
public record BidHistoryItemResponse(
        @Schema(description = "Unique bid record ID")
        UUID bidId,

        @Schema(description = "Auction ID")
        UUID auctionId,

        @Schema(description = "Masked bidder label for timeline display", example = "BIDDER-A1B2C3")
        String bidderMask,

        @Schema(description = "Bid amount", example = "510000000.00")
        BigDecimal amount,

        @Schema(description = "Bid status")
        String bidStatus,

        @Schema(description = "Bid rank by amount in current top list (1 = highest)", example = "1")
        int rank,

        @Schema(description = "True if this item is currently the highest bid")
        boolean winning,

        @Schema(description = "Bid creation time used for timeline ordering")
        LocalDateTime createdAt
) {
}

