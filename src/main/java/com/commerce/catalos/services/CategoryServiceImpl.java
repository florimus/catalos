package com.commerce.catalos.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.helpers.CategoryHelper;
import com.commerce.catalos.models.categories.CreateCategoryRequest;
import com.commerce.catalos.models.categories.CreateCategoryResponse;
import com.commerce.catalos.persistence.dtos.Category;
import com.commerce.catalos.persistence.repositories.CategoryRepository;
import com.commerce.catalos.security.AuthContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final AuthContext authContext;

    private Category getCategoryById(final String id) {
        return categoryRepository.findCategoryByIdAndEnabled(id, true);
    }

    @Override
    public CreateCategoryResponse createCategory(final CreateCategoryRequest createCategoryRequest) {
        Category parenCategory = new Category();
        if (createCategoryRequest.getParentId() != null || !createCategoryRequest.getParentId().isBlank()) {
            Logger.info("3eaadb1e-6abc-40cc-8b45-ebcf016609aa", "Finding parent category id: {}",
                    createCategoryRequest.getParentId());
            parenCategory = getCategoryById(createCategoryRequest.getParentId());
        }
        Logger.info("̦607360db-536c-4260-9d59-47d8eb18eec5", "Parent Category is: {}", parenCategory.getName());
        Category category = CategoryHelper.toCategoryFromCreateCategoryRequest(createCategoryRequest, parenCategory);
        category.setActive(true);
        category.setEnabled(true);
        category.setCreatedAt(new Date());
        category.setCreatedBy(authContext.getCurrentUser().getEmail());
        category = categoryRepository.save(category);
        Logger.info("6787a804-5018-4dd7-aa68-9ce0db1d097e", "Category created with id: {}", category.getId());
        return CategoryHelper.toCreateCategoryResponseFromCategory(category);
    }

}
