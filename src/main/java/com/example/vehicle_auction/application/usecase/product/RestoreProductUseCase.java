package com.example.vehicle_auction.application.usecase.product;

import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Product;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RestoreProductUseCase {
    private final JpaProductRepository productRepository;

    public void execute(UUID id) {
        Product product = productRepository.findByIdAndDeletedTrue(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        product.restore();
        productRepository.save(product);
    }
}
