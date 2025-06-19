package com.commerce.catalos.models.products;

import com.commerce.catalos.models.productTypes.AttributeItemProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UpdateProductRequest {

    private String name;

    private String categoryId;

    private String brandId;

    private List<String> publishedChannels;

    private Map<String, AttributeItemProperties> attributes;
}
