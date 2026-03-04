package com.example.vehicle_auction.application.usecase.category;

import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.infrastructure.persistence.entity.Category;
import com.example.vehicle_auction.infrastructure.persistence.repository.JpaCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RestoreCategoryUseCase {
    private final JpaCategoryRepository categoryRepository;

    public void execute(UUID id) {

        Category category = categoryRepository.findByIdAndDeletedTrue(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.restore();
        categoryRepository.save(category);
    }
}
