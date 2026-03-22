package com.example.vehicle_auction.application.usecase.product;

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
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    public void execute(UUID id) {
        ProductModel product = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus() == ProductStatus.IN_AUCTION || product.getStatus() == ProductStatus.SOLD) {
            throw new AppException(ErrorCode.PRODUCT_CANNOT_DELETE);
        }

        product.softDelete();
        productRepository.save(product);
    }
}
