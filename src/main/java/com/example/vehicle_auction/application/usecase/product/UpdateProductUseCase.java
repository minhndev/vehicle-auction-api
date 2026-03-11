package com.example.vehicle_auction.application.usecase.product;

import com.example.vehicle_auction.application.dto.product.ProductRequest;
import com.example.vehicle_auction.application.dto.product.ProductResponse;
import com.example.vehicle_auction.application.mapper.ProductMapper;
import com.example.vehicle_auction.domain.enums.ProductStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.ProductModel;
import com.example.vehicle_auction.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateProductUseCase {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponse execute(UUID id, ProductRequest request) {
        ProductModel product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus() == ProductStatus.IN_AUCTION || product.getStatus() == ProductStatus.SOLD) {
            throw new AppException(ErrorCode.PRODUCT_CANNOT_UPDATE);
        }

        if (!product.getVinNumber().equals(request.vinNumber()) &&
                productRepository.existsByVinNumber(request.vinNumber())) {
            throw new AppException(ErrorCode.VIN_NUMBER_EXISTS);
        }

        productMapper.updateEntityFromRequest(request, product);

        product.setStatus(ProductStatus.PENDING);

        return productMapper.toResponse(productRepository.save(product));
    }
}
