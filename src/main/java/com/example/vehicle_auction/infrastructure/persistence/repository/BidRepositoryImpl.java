package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.BidModel;
import com.example.vehicle_auction.domain.repository.BidRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Bid;
import com.example.vehicle_auction.infrastructure.persistence.mapper.BidEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
