package com.example.vehicle_auction.application.usecase.bid;

import com.example.vehicle_auction.application.dto.bid.BidResponse;
import com.example.vehicle_auction.application.mapper.BidMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaBidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetBidHistoryUseCase {

    private final JpaBidRepository bidRepository;
    private final BidMapper bidMapper;

    @Cacheable(value = "top_bids", key = "#auctionId")
    public List<BidResponse> getTop5Bids(UUID auctionId) {

        return bidRepository.findTop10ByAuctionIdOrderByAmountDesc(auctionId)
                .stream()
                .map(bidMapper::toResponse)
                .toList();
    }
}
