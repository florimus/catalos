package com.commerce.catalos.services;

import com.commerce.catalos.models.categories.*;
import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;

import java.util.List;

public interface CategoryService {

    CreateCategoryResponse createCategory(final CreateCategoryRequest createCategoryRequest);

    UpdateCategoryResponse updateCategory(final String id, final UpdateCategoryRequest updateCategoryRequest);

    CategoryResponse getCategory(final String id);

    Page<CategoryResponse> listCategories(final String query, final String parent, final Pageable pageable);

    CategoryStatusUpdateResponse updateCategoryStatus(final String id, final boolean status);

    List<CategoryResponse> listCategoriesByIds(final CategoryListRequest categoryListRequest);
}
