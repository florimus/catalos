package com.commerce.catalos.services;

import com.commerce.catalos.models.categories.CategoryResponse;
import com.commerce.catalos.models.categories.CreateCategoryRequest;
import com.commerce.catalos.models.categories.CreateCategoryResponse;
import com.commerce.catalos.models.categories.UpdateCategoryRequest;
import com.commerce.catalos.models.categories.UpdateCategoryResponse;

public interface CategoryService {

    CreateCategoryResponse createCategory(final CreateCategoryRequest createCategoryRequest);

    UpdateCategoryResponse updateCategory(final String id, final UpdateCategoryRequest updateCategoryRequest);

    CategoryResponse getCategory(final String id);

}
