package com.example.vehicle_auction.application.usecase.bid;

import com.example.vehicle_auction.application.dto.bid.BidRequest;
import com.example.vehicle_auction.application.dto.bid.BidResponse;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.model.BidModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.domain.repository.BidRepository;

import com.example.vehicle_auction.domain.repository.DepositRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public BidResponse execute(UUID auctionId, BidRequest request, UUID bidderId) {

        LocalDateTime now = LocalDateTime.now();

        // 1. Lấy AuctionModel lên và khóa row lại (Pessimistic Lock)
        AuctionModel auctionModel = auctionRepository.findByIdWithLock(auctionId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_NOT_FOUND));

        // 2. Kiểm tra đã nộp cọc chưa
        boolean hasDeposited = depositRepository.hasPaidDeposit(auctionId, bidderId);
        if (!hasDeposited) {
            throw new AppException(ErrorCode.DEPOSIT_REQUIRED);
        }

        // 3. Đặt bid
        BidModel newBidModel = auctionModel.placeBid(request.amount(), bidderId, now);

        // 4. Lưu dữ liệu
        auctionRepository.save(auctionModel);
        BidModel savedBidModel = bidRepository.save(newBidModel);

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
