package com.commerce.catalos.models.products;

import com.commerce.catalos.models.productTypes.AttributeItemProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductResponse {

    private String id;

    private String name;

    private String skuId;

    private String categoryId;

    private String brandId;

    private String productTypeId;

    private List<String> publishedChannels;

    private Map<String, AttributeItemProperties> attributes;

    private boolean active;
}
