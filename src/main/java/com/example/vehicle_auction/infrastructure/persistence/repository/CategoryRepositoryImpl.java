package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.CategoryModel;
import com.example.vehicle_auction.domain.repository.CategoryRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Category;
import com.example.vehicle_auction.infrastructure.persistence.mapper.CategoryEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final JpaCategoryRepository jpaRepository;
    private final CategoryEntityMapper mapper;

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsBySlug(String slug) {
        return jpaRepository.existsBySlug(slug);
    }

    @Override
    public Page<CategoryModel> findAllByDeletedFalse(Pageable pageable) {
        return jpaRepository.findAllByDeletedFalse(pageable).map(mapper::toDomain);
    }

    @Override
    public Optional<CategoryModel> findByIdAndDeletedFalse(UUID id) {
        return jpaRepository.findByIdAndDeletedFalse(id).map(mapper::toDomain);
    }

    @Override
    public Optional<CategoryModel> findByIdAndDeletedTrue(UUID id) {
        return jpaRepository.findByIdAndDeletedTrue(id).map(mapper::toDomain);
    }

    @Override
    public Optional<CategoryModel> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public CategoryModel save(CategoryModel categoryModel) {
        Category entity;
        if (categoryModel.getId() != null) {
            entity = jpaRepository.findById(categoryModel.getId()).orElseThrow();
            mapper.updateEntityFromModel(categoryModel, entity);
        } else {
            entity = mapper.toEntity(categoryModel);
        }
        return mapper.toDomain(jpaRepository.save(entity));
    }
}
