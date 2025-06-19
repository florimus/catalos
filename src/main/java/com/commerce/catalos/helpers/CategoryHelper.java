package com.commerce.catalos.helpers;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.categories.CreateCategoryRequest;
import com.commerce.catalos.models.categories.CreateCategoryResponse;
import com.commerce.catalos.persistence.dtos.Category;

public class CategoryHelper {

    public static Category toCategoryFromCreateCategoryRequest(final CreateCategoryRequest createCategoryRequest,
            final Category parenCategory) {
        Category category = new Category();
        BeanUtils.copyProperties(createCategoryRequest, category);
        category.setParentName(parenCategory.getName());
        category.setParentId(parenCategory.getId());
        return category;
    }

    public static CreateCategoryResponse toCreateCategoryResponseFromCategory(final Category category) {
        CreateCategoryResponse response = new CreateCategoryResponse();
        BeanUtils.copyProperties(category, response);
        return response;
    }
}
