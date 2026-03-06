package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Auction;
import com.example.vehicle_auction.infrastructure.persistence.mapper.AuctionEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaAuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AuctionRepositoryImpl implements AuctionRepository {
    private final JpaAuctionRepository jpaAuctionRepository;
    private final AuctionEntityMapper auctionEntityMapper;


    @Override
    public AuctionModel save(AuctionModel auctionModel) {
        if (auctionModel.getId() != null && jpaAuctionRepository.existsById(auctionModel.getId())) {
            Auction existingEntity = jpaAuctionRepository.findById(auctionModel.getId()).orElseThrow();
            auctionEntityMapper.updateEntityFromModel(auctionModel, existingEntity);
            return auctionEntityMapper.toDomain(jpaAuctionRepository.save(existingEntity));
        } else {
            Auction newEntity = auctionEntityMapper.toEntity(auctionModel);
            return auctionEntityMapper.toDomain(jpaAuctionRepository.save(newEntity));
        }
    }

    @Override
    public Optional<AuctionModel> findByIdWithLock(UUID id) {
        return jpaAuctionRepository.findByIdWithPessimisticLock(id)
                .map(auctionEntityMapper::toDomain);
    }
}
