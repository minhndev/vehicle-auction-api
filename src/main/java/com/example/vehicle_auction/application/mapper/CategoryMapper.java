package com.example.vehicle_auction.application.mapper;

import com.example.vehicle_auction.application.dto.category.CategoryRequest;
import com.example.vehicle_auction.application.dto.category.CategoryResponse;
import com.example.vehicle_auction.infrastructure.persistence.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    @Mapping(target = "slug", ignore = true)
    Category toEntity(CategoryRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateEntityFromDto(CategoryRequest request, @MappingTarget Category category);
}
