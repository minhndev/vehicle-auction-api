package com.example.vehicle_auction.application.usecase.bid;

import com.example.vehicle_auction.application.dto.bid.BidHistoryItemResponse;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.model.BidModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.domain.repository.BidRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class GetBidHistoryUseCase {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final BidRealtimeStore bidRealtimeStore;

    public List<BidHistoryItemResponse> getTop10Bids(UUID auctionId) {
        Optional<List<BidHistoryItemResponse>> redisTopBids = bidRealtimeStore.getTopBids(auctionId, 10);
        if (redisTopBids.isPresent()) {
            return redisTopBids.get();
        }

        List<BidModel> bids = bidRepository.findTop10ByAuctionId(auctionId);

        List<BidHistoryItemResponse> response = IntStream.range(0, bids.size())
                .mapToObj(index -> toHistoryItem(bids.get(index), index + 1))
                .toList();

        BigDecimal currentPrice = response.isEmpty() ? null : response.getFirst().amount();
        Integer version = auctionRepository.findById(auctionId)
                .map(AuctionModel::getVersion)
                .orElse(null);

        bidRealtimeStore.warmupFromDb(auctionId, response, currentPrice, version);

        if (response.isEmpty()) {
            log.info("Bid history is empty for auctionId={}. Returning []", auctionId);
        }

        return response;
    }

    private BidHistoryItemResponse toHistoryItem(BidModel bid, int rank) {
        return new BidHistoryItemResponse(
                bid.getId(),
                bid.getAuctionId(),
                toBidderMask(bid.getBidderId()),
                bid.getAmount(),
                bid.getStatus().name(),
                rank,
                rank == 1,
                bid.getCreatedAt()
        );
    }

    private String toBidderMask(UUID bidderId) {
        String raw = bidderId.toString().replace("-", "");
        String suffix = raw.substring(Math.max(0, raw.length() - 6)).toUpperCase();
        return "BIDDER-" + suffix;
    }
}
