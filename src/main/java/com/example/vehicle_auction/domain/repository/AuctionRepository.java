package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.application.dto.auction.AuctionFilterRequest;
import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.domain.model.AuctionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuctionRepository {
    Optional<AuctionModel> findByIdWithLock(UUID id);
    Optional<AuctionModel> findById(UUID id);
    AuctionModel save(AuctionModel auctionModel);

    void saveAll(List<AuctionModel> auctionModels);

    boolean existsByProductIdAndStatusIn(UUID productId, List<AuctionStatus> statuses);
    List<AuctionModel> findAuctionsToOpen(AuctionStatus status, LocalDateTime now);
    Page<AuctionModel> findAuctionsToClose(AuctionStatus status, LocalDateTime now, Pageable pageable);

    // Đẩy việc xử lý Specification (JPA) xuống Adapter, Use Case chỉ truyền DTO
    Page<AuctionModel> findAll(AuctionFilterRequest request, Pageable pageable);
}
