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
@Transactional
public class ApproveProductUseCase {

    private final JpaProductRepository jpaProductRepository;
    private final ProductMapper productMapper;

    public ProductResponse execute(UUID productId) {
        log.info("Starting to approve product with ID: {}", productId);

        // Find the product by ID
        var product = jpaProductRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        // Update the product status to APPROVED
        if (product.getStatus() != ProductStatus.PENDING){
            log.warn("Cannot approve product with ID {} because it is not in PENDING status", productId);
            throw new AppException(ErrorCode.PRODUCT_NOT_PENDING);
        }

        //Set the product status to APPROVED
        product.setStatus(ProductStatus.APPROVED);

        // Save the updated product
        Product updatedProduct = jpaProductRepository.save(product);
        log.info("Product with ID: {} has been APPROVED", updatedProduct.getId());

        return productMapper.toResponse(updatedProduct);
    }
}
