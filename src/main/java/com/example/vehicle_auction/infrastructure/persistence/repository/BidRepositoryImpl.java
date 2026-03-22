package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.BidModel;
import com.example.vehicle_auction.domain.repository.BidRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Bid;
import com.example.vehicle_auction.infrastructure.persistence.mapper.BidEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaBidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BidRepositoryImpl implements BidRepository {

    private final JpaBidRepository jpaBidRepository;
    private final BidEntityMapper mapper;

    @Override
    public BidModel save(BidModel bidModel) {
        Bid entity = mapper.toEntity(bidModel);
        Bid savedEntity = jpaBidRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<BidModel> findTop10ByAuctionId(UUID auctionId) {
        return jpaBidRepository.findTop10ByAuctionIdOrderByAmountDesc(auctionId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BidModel> findByBidderIdOrderByCreatedAtDesc(UUID bidderId, Pageable pageable) {
        return jpaBidRepository.findByBidderIdOrderByCreatedAtDesc(bidderId, pageable)
                .map(mapper::toDomain);
    }
}
