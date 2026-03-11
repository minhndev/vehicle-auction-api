package com.example.vehicle_auction.domain.repository;

import com.example.vehicle_auction.domain.model.CategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    boolean existsByName(String name);
    boolean existsBySlug(String slug);

    Page<CategoryModel> findAllByDeletedFalse(Pageable pageable);
    Optional<CategoryModel> findByIdAndDeletedFalse(UUID id);
    Optional<CategoryModel> findByIdAndDeletedTrue(UUID id);

    // Hàm chuẩn để fetch chi tiết và lưu trữ
    Optional<CategoryModel> findById(UUID id);
    CategoryModel save(CategoryModel categoryModel);
}
