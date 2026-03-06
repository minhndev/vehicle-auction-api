package com.example.vehicle_auction.application.usecase.product;

import com.example.vehicle_auction.application.dto.product.ProductRequest;
import com.example.vehicle_auction.application.dto.product.ProductResponse;
import com.example.vehicle_auction.application.mapper.ProductMapper;
import com.example.vehicle_auction.application.usecase.notification.NotificationUseCase;
import com.example.vehicle_auction.domain.enums.NotificationType;
import com.example.vehicle_auction.domain.enums.ProductStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Account;
import com.example.vehicle_auction.infrastructure.persistence.entity.Category;
import com.example.vehicle_auction.infrastructure.persistence.entity.Product;
import com.example.vehicle_auction.infrastructure.persistence.entity.ProductImage;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaAccountRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaCategoryRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreateProductUseCase {

    private final JpaProductRepository jpaProductRepository;
    private final JpaCategoryRepository jpaCategoryRepository;
    private final JpaAccountRepository jpaAccountRepository;
    private final NotificationUseCase notificationUseCase;
    private final ProductMapper productMapper;

    public ProductResponse execute(ProductRequest request){
        log.info("Starting to create new product with VIN: {}", request.vinNumber());

        // Validate category existence
        Category category = jpaCategoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Validate VIN number uniqueness
        if(jpaProductRepository.existsByVinNumber(request.vinNumber())){
            log.warn("VIN number {} already exists", request.vinNumber());
            throw new AppException(ErrorCode.VIN_NUMBER_EXISTS);
        }

        // Map request to entity
        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        product.setStatus(ProductStatus.PENDING);

        // Handle product images
        List<String> imageUrls = request.imageUrls();
        for(int i = 0; i < imageUrls.size(); i++){
            ProductImage image = new ProductImage();
            image.setUrl(imageUrls.get(i));
            image.setSortOrder(i);
            image.setMain(i == 0); // First image is main

            product.addImage(image);
        }

        // Save product to database
        Product savedProduct = jpaProductRepository.save(product);

        log.info("Successfully created product {} with ID: {}", savedProduct.getVinNumber(), savedProduct.getId());

        notifyAdmins(savedProduct);

        return productMapper.toResponse(savedProduct);
    }

    private void notifyAdmins(Product product) {
        try {
            List<Account> admins = jpaAccountRepository.findBySystemTrue();

            for (Account admin : admins) {
                notificationUseCase.createNotification(
                        admin.getId(),
                        NotificationType.CREATE_PRODUCT,
                        "New product requires approval.",
                        "The vehicle with VIN number: " + product.getVinNumber() + " has just been registered and is awaiting your approval.",
                        product.getId(),
                        "PRODUCT"
                );
            }
            log.info("Product approval notification has been sent to {} admin.", admins.size());
        } catch (Exception e) {
            log.error("Error sending notification to Admin: {}", e.getMessage(), e);
        }
    }
}
