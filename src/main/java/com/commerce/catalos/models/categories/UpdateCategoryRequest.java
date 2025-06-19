package com.commerce.catalos.models.categories;

import lombok.Data;

@Data
public class UpdateCategoryRequest {

    private String name;

    private String parentId;

    private String seoTitle;

    private String seoDescription;
}
