package com.example.vehicle_auction.domain.model;

import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.domain.enums.BidStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AuctionModel {

    private UUID id;
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
        // Validate trạng thái và thời gian
        if (status != AuctionStatus.ACTIVE || now.isBefore(startTime) || now.isAfter(endTime)) {
            throw new AppException(ErrorCode.AUCTION_NOT_ACTIVE);
        }

        // Validate bước giá
        BigDecimal minRequiredBid = currentPrice.add(bidIncrement);
        if (amount.compareTo(minRequiredBid) < 0) {
            throw new AppException(ErrorCode.BID_AMOUNT_TOO_LOW, minRequiredBid);
        }

        // Cập nhật giá và người thắng tạm thời
        this.currentPrice = amount;
        this.winnerId = bidderId;

        // 4. Anti-snipping (Gia hạn nếu đặt ở 5 phút cuối)
        LocalDateTime currentEnd = (this.actualEndTime != null) ? this.actualEndTime : this.endTime;
        if (now.plusMinutes(5).isAfter(currentEnd)) {
            this.actualEndTime = currentEnd.plusMinutes(5);
        }

        return BidModel.builder()
                .auctionId(this.id)
                .bidderId(bidderId)
                .amount(amount)
                .status(BidStatus.VALID)
                .createdAt(now)
                .build();
    }
}
