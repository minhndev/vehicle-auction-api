package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.ProductImage.ProductImageResponse;
import com.example.vehicle_auction.application.dto.product.ProductRequest;
import com.example.vehicle_auction.application.dto.product.ProductResponse;
import com.example.vehicle_auction.domain.model.ProductModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.ProductImage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sellerId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    ProductModel toDomain(ProductRequest request);

    @Mapping(target = "categoryName", ignore = true)
    ProductResponse toResponse(ProductModel productModel);

    ProductImageResponse toImageResponse(ProductImage image);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sellerId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget ProductModel productModel);
}
