package com.example.vehicle_auction.application.usecase.category;

import com.example.vehicle_auction.application.dto.category.CategoryRequest;
import com.example.vehicle_auction.application.dto.category.CategoryResponse;
import com.example.vehicle_auction.application.mapper.CategoryMapper;
import com.example.vehicle_auction.domain.exception.AppException;
import com.example.vehicle_auction.domain.exception.ErrorCode;
import com.example.vehicle_auction.domain.model.CategoryModel;
import com.example.vehicle_auction.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreateCategoryUseCase {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public CategoryResponse execute(CategoryRequest request){
        log.info("Creating new category with name: {}", request.name());

        if  (categoryRepository.existsByName(request.name())){
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        String slug = generateSlug(request.name());

        if  (categoryRepository.existsBySlug(slug)){
            slug = slug + "-" + System.currentTimeMillis();
        }

        CategoryModel category = categoryMapper.toDomain(request);
        category.setSlug(slug);

        CategoryModel savedCategory = categoryRepository.save(category);
        log.info("Category created successfully with slug: {}", savedCategory.getSlug());

        return categoryMapper.toResponse(savedCategory);
    }

    private String generateSlug(String name){
        if (name == null) return "";

        String noWhiteSpace = WHITESPACE.matcher(name).replaceAll("-");
        String normalized = Normalizer.normalize(noWhiteSpace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");

        slug = slug.replaceAll("đ", "d").replaceAll("Đ", "D");

        return slug.toLowerCase(Locale.ENGLISH).replaceAll("-{2,}", "-").replaceAll("^-|-$", "");
    }
}
