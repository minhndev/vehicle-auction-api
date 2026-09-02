package com.example.vehicle_auction.application.dto.auction;

import com.example.vehicle_auction.domain.enums.AuctionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AuctionFilterRequest {
    private String keyword;
    private AuctionStatus status;
    private UUID categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;

}
