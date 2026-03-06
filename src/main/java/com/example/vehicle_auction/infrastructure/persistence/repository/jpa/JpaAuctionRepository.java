package com.example.vehicle_auction.infrastructure.persistence.repository.jpa;

import com.example.vehicle_auction.domain.enums.AuctionStatus;
import com.example.vehicle_auction.infrastructure.persistence.entity.Auction;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaAuctionRepository extends JpaRepository<Auction, UUID> {
    boolean existsByProductIdAndStatusIn(UUID productId, List<AuctionStatus> statuses);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Auction a WHERE a.id = :id")
    Optional<Auction> findByIdWithPessimisticLock(@Param("id") UUID id);

    @Query("SELECT a FROM Auction a WHERE a.status = :status AND a.startTime <= :now")
    List<Auction> findAuctionsToOpen(@Param("status") AuctionStatus status, @Param("now") LocalDateTime now);

    @Query("SELECT a FROM Auction a WHERE a.status = :status AND (COALESCE(a.actualEndTime, a.endTime) <= :now)")
    List<Auction> findAuctionsToClose(@Param("status") AuctionStatus status, @Param("now") LocalDateTime now);
}