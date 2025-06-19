package com.commerce.catalos.models.categories;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank(message = "Category name is mandatory")
    private String name;

    private String parentId;

    private String seoTitle;

    private String seoDescription;
}
