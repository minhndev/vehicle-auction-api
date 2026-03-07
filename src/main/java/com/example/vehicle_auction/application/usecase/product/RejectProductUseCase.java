package com.example.vehicle_auction.application.usecase.product;

import com.example.vehicle_auction.application.dto.product.ProductResponse;
import com.example.vehicle_auction.application.mapper.ProductMapper;
import com.example.vehicle_auction.domain.enums.ProductStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Product;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RejectProductUseCase {

    private final JpaProductRepository jpaProductRepository;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponse execute(UUID productId) {
        log.info("Starting to approve product with ID: {}", productId);

        // Find the product by ID
        Product product = jpaProductRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Update the product status to APPROVED
        if (product.getStatus() != ProductStatus.PENDING){
            log.warn("Cannot reject product with ID {} because it is not in PENDING status. Current status: {}",
                    productId, product.getStatus());
            throw new AppException(ErrorCode.PRODUCT_NOT_PENDING);
        }
        product.setStatus(ProductStatus.REJECTED);

        // Save the updated product
        Product updatedProduct = jpaProductRepository.save(product);
        log.info("Product with ID: {} has been REJECTED", updatedProduct.getId());

        return productMapper.toResponse(updatedProduct);
    }
}
