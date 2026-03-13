package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.domain.enums.BidStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.base.AuditModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class AuctionModel extends AuditModel {

    private UUID productId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime actualEndTime;
    private BigDecimal startPrice;
    private BigDecimal currentPrice;
    private BigDecimal bidIncrement;
    private BigDecimal depositAmount;
    private UUID winnerId;
    private AuctionStatus status;
    private Integer version;

    public BidModel placeBid(BigDecimal amount, UUID bidderId, LocalDateTime now) {
        // Validate status and time
        if (status != AuctionStatus.ACTIVE || now.isBefore(startTime) || now.isAfter(endTime)) {
            throw new AppException(ErrorCode.AUCTION_NOT_ACTIVE);
        }

        // Validate bid increment
        BigDecimal minRequiredBid = currentPrice.add(bidIncrement);
        if (amount.compareTo(minRequiredBid) < 0) {
            throw new AppException(ErrorCode.BID_AMOUNT_TOO_LOW, minRequiredBid);
        }

        // Update price and temporary winner
        this.currentPrice = amount;
        this.winnerId = bidderId;

        // Anti-snipping
        LocalDateTime currentEnd = (this.actualEndTime != null) ? this.actualEndTime : this.endTime;
        if (now.plusMinutes(5).isAfter(currentEnd)) {
            this.actualEndTime = currentEnd.plusMinutes(5);
        }

        BidModel newBid = BidModel.builder()
                .auctionId(this.getId())
                .bidderId(bidderId)
                .amount(amount)
                .status(BidStatus.VALID)
                .build();

        newBid.setCreatedAt(now);

        return newBid;
    }
}
