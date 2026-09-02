package com.example.vehicle_auction.application.usecase.auction;

import com.example.vehicle_auction.application.dto.auction.AuctionFilterRequest;
import com.example.vehicle_auction.application.dto.auction.AuctionResponse;
import com.example.vehicle_auction.application.mapper.AuctionMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.ProductModel;
import com.example.vehicle_auction.domain.repository.AuctionRepository;
import com.example.vehicle_auction.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchAuctionsUseCase {

    private final AuctionRepository auctionRepository;
    private final AuctionMapper auctionMapper;
    private final ProductRepository productRepository;

    public Page<AuctionResponse> execute(AuctionFilterRequest request, Pageable pageable){

        return auctionRepository.findAll(request, pageable)
                .map(auction -> {

                    ProductModel product = productRepository.findById(auction.getProductId())
                            .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                    return auctionMapper.toResponse(auction, product.getName());
                });
    }
}
