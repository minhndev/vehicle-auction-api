package com.example.vehicle_auction.application.usecase.category;

import com.example.vehicle_auction.application.dto.category.CategoryResponse;
import com.example.vehicle_auction.application.mapper.CategoryMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetCategoryUseCase {

    private final JpaCategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public Page<CategoryResponse> getAll(Pageable pageable) {
        return categoryRepository.findAllByDeletedFalse(pageable)
                .map(categoryMapper::toResponse);
    }

    public CategoryResponse getById(UUID id) {
        return categoryRepository.findByIdAndDeletedFalse(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }
}
