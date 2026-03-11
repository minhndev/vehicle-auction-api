package com.example.vehicle_auction.infrastructure.persistence.repository;

import com.example.vehicle_auction.domain.model.ProductModel;
import com.example.vehicle_auction.domain.repository.ProductRepository;
import com.example.vehicle_auction.infrastructure.persistence.entity.Product;
import com.example.vehicle_auction.infrastructure.persistence.mapper.ProductEntityMapper;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaCategoryRepository;
import com.example.vehicle_auction.infrastructure.persistence.repository.jpa.JpaProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaRepository;
    private final JpaCategoryRepository jpaCategoryRepository;
    private final ProductEntityMapper mapper;

    @Override
    public boolean existsByVinNumber(String vinNumber) {
        return jpaRepository.existsByVinNumber(vinNumber);
    }

    @Override
    public Optional<ProductModel> findByIdAndDeletedFalse(UUID id) {
        return jpaRepository.findByIdAndDeletedFalse(id).map(mapper::toDomain);
    }

    @Override
    public Optional<ProductModel> findByIdAndDeletedTrue(UUID id) {
        return jpaRepository.findByIdAndDeletedTrue(id).map(mapper::toDomain);
    }

    @Override
    public Page<ProductModel> findAllByDeletedFalse(Pageable pageable) {
        return jpaRepository.findAllByDeletedFalse(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<ProductModel> findAllBySellerIdAndDeletedFalse(UUID sellerId, Pageable pageable) {
        return jpaRepository.findAllBySellerIdAndDeletedFalse(sellerId, pageable).map(mapper::toDomain);
    }

    @Override
    public Optional<ProductModel> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public ProductModel save(ProductModel productModel) {
        Product entity;
        if (productModel.getId() != null) {
            entity = jpaRepository.findById(productModel.getId()).orElseThrow();
            mapper.updateEntityFromModel(productModel, entity);
        } else {
            entity = mapper.toEntity(productModel);

            if (productModel.getCategoryId() != null) {
                entity.setCategory(jpaCategoryRepository.getReferenceById(productModel.getCategoryId()));
            }
        }
        return mapper.toDomain(jpaRepository.save(entity));
    }
}
