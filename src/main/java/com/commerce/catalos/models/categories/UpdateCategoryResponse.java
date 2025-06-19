package com.commerce.catalos.models.categories;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoryResponse {

    private String id;

    private String name;

    private String parentName;

    private String parentId;

    private String seoTitle;

    private String seoDescription;

    private boolean active;
}
