package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.ProductImageModel;
import com.example.vehicle_auction.domain.model.ProductModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Product;
import com.example.vehicle_auction.infrastructure.persistence.entity.ProductImage;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductModel toDomain(Product entity);

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductModel model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntityFromModel(ProductModel model, @MappingTarget Product entity);

    ProductImageModel imageToDomain(ProductImage entity);
    ProductImage imageToEntity(ProductImageModel model);

    @AfterMapping
    default void linkImages(@MappingTarget Product productEntity) {
        if (productEntity.getImages() != null) {
            for (ProductImage image : productEntity.getImages()) {
                image.setProduct(productEntity);
            }
        }
    }
}
