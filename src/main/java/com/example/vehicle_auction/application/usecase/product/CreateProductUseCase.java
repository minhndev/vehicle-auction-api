package com.example.vehicle_auction.application.usecase.product;

import com.example.vehicle_auction.application.dto.product.ProductRequest;
import com.example.vehicle_auction.application.dto.product.ProductResponse;
import com.example.vehicle_auction.application.mapper.ProductMapper;
import com.example.vehicle_auction.application.usecase.notification.NotificationUseCase;
import com.example.vehicle_auction.domain.enums.ProductStatus;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.CategoryModel;
import com.example.vehicle_auction.domain.model.ProductImageModel;
import com.example.vehicle_auction.domain.model.ProductModel;
import com.example.vehicle_auction.domain.repository.AccountRepository;
import com.example.vehicle_auction.domain.repository.CategoryRepository;
import com.example.vehicle_auction.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final NotificationUseCase notificationUseCase;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponse execute(ProductRequest request, UUID sellerId){
        log.info("Starting to create new product with VIN: {}", request.vinNumber());

        // Validate category existence
        CategoryModel category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        // Validate VIN number uniqueness
        if(productRepository.existsByVinNumber(request.vinNumber())){
            log.warn("VIN number {} already exists", request.vinNumber());
            throw new AppException(ErrorCode.VIN_NUMBER_EXISTS);
        }

        // Map request to entity
        ProductModel product = productMapper.toDomain(request);
        product.setSellerId(sellerId);
        product.setCategoryId(category.getId());
        product.setStatus(ProductStatus.PENDING);

        // Handle product images
        List<String> imageUrls = request.imageUrls();
        if (imageUrls != null) {
            for(int i = 0; i < imageUrls.size(); i++){
                ProductImageModel image = ProductImageModel.builder()
                        .url(imageUrls.get(i))
                        .sortOrder(i)
                        .isMain(i == 0)
                        .build();
                product.addImage(image);
            }
        }

        // Save product to database
        ProductModel savedProduct = productRepository.save(product);

        log.info("Successfully created product {} with ID: {}", savedProduct.getVinNumber(), savedProduct.getId());

//        notifyAdmins(savedProduct);

        return productMapper.toResponse(savedProduct);
    }

//    private void notifyAdmins(Product product) {
//        try {
//            List<Account> admins = accountRepository.findBySystemTrue();
//
//            for (Account admin : admins) {
//                notificationUseCase.createNotification(
//                        admin.getId(),
//                        NotificationType.CREATE_PRODUCT,
//                        "New product requires approval.",
//                        "The vehicle with VIN number: " + product.getVinNumber() + " has just been registered and is awaiting your approval.",
//                        product.getId(),
//                        "PRODUCT"
//                );
//            }
//            log.info("Product approval notification has been sent to {} admin.", admins.size());
//        } catch (Exception e) {
//            log.error("Error sending notification to Admin: {}", e.getMessage(), e);
//        }
//    }
}
