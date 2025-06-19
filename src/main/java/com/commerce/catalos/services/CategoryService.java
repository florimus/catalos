package com.commerce.catalos.services;

import com.commerce.catalos.models.categories.CreateCategoryRequest;
import com.commerce.catalos.models.categories.CreateCategoryResponse;

public interface CategoryService {

    CreateCategoryResponse createCategory(final CreateCategoryRequest createCategoryRequest);

}
