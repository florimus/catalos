package com.commerce.catalos.resolvers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.models.categories.CategoryResponse;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CategoryResolvers {

    private final CategoryService categoryService;

    @SchemaMapping(typeName = "Product", field = "category")
    public CategoryResponse resolveProductCategory(final ProductResponse productResponse) {
        return categoryService.getCategory(productResponse.getCategoryId());
    }

    @SchemaMapping(typeName = "Category", field = "parent")
    public CategoryResponse resolveParentCategory(final CategoryResponse categoryResponse) {
        if (null == categoryResponse.getParentId()){
            Logger.info("", "No Parent Category");
            return null;
        }
        return categoryService.getCategory(categoryResponse.getParentId());
    }
}
