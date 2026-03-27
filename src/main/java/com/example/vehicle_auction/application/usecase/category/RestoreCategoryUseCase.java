package com.example.vehicle_auction.application.usecase.category;

import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.CategoryModel;
import com.example.vehicle_auction.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RestoreCategoryUseCase {
    private final CategoryRepository categoryRepository;

    public void execute(UUID id) {

        CategoryModel category = categoryRepository.findByIdAndDeletedTrue(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.restore();
        categoryRepository.save(category);
    }
}
