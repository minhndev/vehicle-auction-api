package com.example.vehicle_auction.infrastructure.persistence.mapper;

import com.example.vehicle_auction.domain.model.CategoryModel;
import com.example.vehicle_auction.infrastructure.persistence.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapper {

    CategoryModel toDomain(Category entity);

    Category toEntity(CategoryModel model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntityFromModel(CategoryModel model, @MappingTarget Category entity);
}
