package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

    boolean existsByVinNumber(String vinNumber);

    Optional<ProductModel> findByIdAndDeletedFalse(UUID id);
    Optional<ProductModel> findByIdAndDeletedTrue(UUID id);
    Page<ProductModel> findAllByDeletedFalse(Pageable pageable);
    Page<ProductModel> findAllBySellerIdAndDeletedFalse(UUID sellerId, Pageable pageable);

    Optional<ProductModel> findById(UUID id);
    ProductModel save(ProductModel productModel);
}
