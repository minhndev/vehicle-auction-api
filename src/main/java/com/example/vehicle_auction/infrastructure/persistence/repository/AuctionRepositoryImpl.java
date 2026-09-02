package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.application.dto.auction.AuctionFilterRequest;
import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.domain.model.AuctionModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Auction;
import com.example.vehicle_auction.infrastructure.persistence.mapper.AuctionEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaAuctionRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaProductRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.specification.AuctionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AuctionRepositoryImpl implements AuctionRepository {
    private final JpaAuctionRepository jpaAuctionRepository;
    private final JpaProductRepository jpaProductRepository;
    private final AuctionEntityMapper auctionEntityMapper;


    @Override
    public AuctionModel save(AuctionModel auctionModel) {
        if (auctionModel.getId() != null && jpaAuctionRepository.existsById(auctionModel.getId())) {
            Auction existingEntity = jpaAuctionRepository.findById(auctionModel.getId()).orElseThrow();
            auctionEntityMapper.updateEntityFromModel(auctionModel, existingEntity);
            return auctionEntityMapper.toDomain(jpaAuctionRepository.save(existingEntity));
        } else {
            Auction newEntity = auctionEntityMapper.toEntity(auctionModel);

            if (auctionModel.getProductId() != null) {
                newEntity.setProduct(jpaProductRepository.getReferenceById(auctionModel.getProductId()));
            }

            return auctionEntityMapper.toDomain(jpaAuctionRepository.save(newEntity));
        }
    }

    @Override
    public void saveAll(List<AuctionModel> auctionModels) {
        List<Auction> entities = auctionModels.stream().map(model -> {
            if (model.getId() != null) {
                Auction existing = jpaAuctionRepository.findById(model.getId()).orElseThrow();
                auctionEntityMapper.updateEntityFromModel(model, existing);
                return existing;
            }
            return auctionEntityMapper.toEntity(model);
        }).collect(Collectors.toList());

        jpaAuctionRepository.saveAll(entities);
    }

    @Override
    public boolean existsByProductIdAndStatusIn(UUID productId, List<AuctionStatus> statuses) {
        return jpaAuctionRepository.existsByProductIdAndStatusIn(productId, statuses);
    }

    @Override
    public List<AuctionModel> findAuctionsToOpen(AuctionStatus status, LocalDateTime now) {
        return jpaAuctionRepository.findAuctionsToOpen(status, now)
                .stream()
                .map(auctionEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Page<AuctionModel> findAuctionsToClose(AuctionStatus status, LocalDateTime now, Pageable pageable) {
        return jpaAuctionRepository.findAuctionsToClose(status, now, pageable)
                .map(auctionEntityMapper::toDomain);
    }

    @Override
    public Page<AuctionModel> findAll(AuctionFilterRequest request, Pageable pageable) {
        Specification<Auction> spec = AuctionSpecification.filterBY(request);
        return jpaAuctionRepository.findAll(spec, pageable)
                .map(auctionEntityMapper::toDomain);
    }

    @Override
    public Optional<AuctionModel> findByIdWithLock(UUID id) {
        return jpaAuctionRepository.findByIdWithPessimisticLock(id)
                .map(auctionEntityMapper::toDomain);
    }

    @Override
    public Optional<AuctionModel> findById(UUID id) {
        return jpaAuctionRepository.findById(id)
                .map(auctionEntityMapper::toDomain);
    }
}
