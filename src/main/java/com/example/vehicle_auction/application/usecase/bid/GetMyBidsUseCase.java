package com.example.vehicle_auction.application.usecase.bid;

import com.example.vehicle_auction.application.dto.bid.BidResponse;
import com.example.vehicle_auction.application.mapper.BidMapper;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.domain.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMyBidsUseCase {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final BidMapper bidMapper;

    @Transactional(readOnly = true)
    public Page<BidResponse> execute(UUID bidderId, Pageable pageable) {
        return bidRepository.findByBidderIdOrderByCreatedAtDesc(bidderId, pageable)
                .map(bid -> {
                    AuctionModel auction = auctionRepository.findById(bid.getAuctionId()).orElse(null);
                    boolean isWinning = false;
                    if (auction != null && auction.getWinnerId() != null) {
                        isWinning = auction.getWinnerId().equals(bidderId)
                                && auction.getCurrentPrice().compareTo(bid.getAmount()) == 0;
                    }

                    return bidMapper.toResponseWithWinningStatus(bid, isWinning);
                });
    }
}
