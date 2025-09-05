package com.commerce.catalos.models.categories;

import lombok.Data;

import java.util.List;

@Data
public class CategoryListRequest {

    private List<String> ids;
}
