package com.example.vehicle_auction.application.usecase.bid;

import com.example.vehicle_auction.application.dto.bid.BidRequest;
import com.example.vehicle_auction.application.dto.bid.BidResponse;
import com.example.vehicle_auction.domain.event.OutbidEvent;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.model.BidModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.domain.repository.BidRepository;

import com.example.vehicle_auction.domain.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceBidUseCase {
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final DepositRepository depositRepository;
    // Sử dụng ApplicationEventPublisher để phát sự kiện khi có bid mới
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public BidResponse execute(UUID auctionId, BidRequest request, UUID bidderId) {

        LocalDateTime now = LocalDateTime.now();

        // Kiểm tra đã nộp cọc chưa
        boolean hasDeposited = depositRepository.hasPaidDeposit(auctionId, bidderId);
        if (!hasDeposited) {
            throw new AppException(ErrorCode.DEPOSIT_REQUIRED);
        }

        // Lấy AuctionModel lên và khóa row lại (Pessimistic Lock)
        AuctionModel auctionModel = auctionRepository.findByIdWithLock(auctionId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));

        // Lấy previous winnerId trước khi đặt bid mới
        UUID previousWinnerId = auctionModel.getWinnerId();

        // Đặt bid
        BidModel newBidModel = auctionModel.placeBid(request.amount(), bidderId, now);

        auctionRepository.save(auctionModel);
        BidModel savedBidModel = bidRepository.save(newBidModel);

        // Nếu có previous winner và không phải người đặt bid mới thì phát sự kiện Outbid
        if (previousWinnerId != null && !previousWinnerId.equals(bidderId)){
            eventPublisher.publishEvent(new OutbidEvent(previousWinnerId, auctionId, request.amount()));
        }

        boolean isWinning = savedBidModel.getBidderId().equals(auctionModel.getWinnerId());

        return new BidResponse(
                savedBidModel.getId(),
                savedBidModel.getAuctionId(),
                savedBidModel.getBidderId(),
                savedBidModel.getAmount(),
                savedBidModel.getCreatedAt(),
                isWinning
        );
    }
}
