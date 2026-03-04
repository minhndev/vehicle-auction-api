package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.ProductImage.ProductImageResponse;
import com.example.vehicle_auction.application.dto.product.ProductRequest;
import com.example.vehicle_auction.application.dto.product.ProductResponse;
import com.example.vehicle_auction.infrastructure.persistence.entity.Product;
import com.example.vehicle_auction.infrastructure.persistence.entity.ProductImage;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "images", ignore = true)
    Product toEntity(ProductRequest request);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponse toResponse(Product product);

    ProductImageResponse toImageResponse(ProductImage image);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sellerId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntityFromRequest(ProductRequest request, @MappingTarget Product product);
}
