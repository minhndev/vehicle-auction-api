package com.example.vehicle_auction.application.usecase.auction;

import com.example.vehicle_auction.application.dto.auction.AuctionFilterRequest;
import com.example.vehicle_auction.application.dto.auction.AuctionResponse;
import com.example.vehicle_auction.application.mapper.AuctionMapper;
import com.example.vehicle_auction.infrastructure.persistence.entity.Auction;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaAuctionRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.specification.AuctionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchAuctionsUseCase {

    private final JpaAuctionRepository  auctionRepository;
    private final AuctionMapper auctionMapper;

    public Page<AuctionResponse> execute(AuctionFilterRequest request, Pageable pageable){

        Specification<Auction> spec = AuctionSpecification.filterBY(request);

        return auctionRepository.findAll(spec, pageable)
                .map(auctionMapper::toResponse);
    }
}
