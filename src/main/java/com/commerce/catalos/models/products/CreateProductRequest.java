package com.commerce.catalos.models.products;

import com.commerce.catalos.models.productTypes.AttributeItemProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreateProductRequest {

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotBlank(message = "Sku Id is mandatory")
    private String skuId;

    private String categoryId;

    private String brandId;

    @NotBlank(message = "Product-type Id is mandatory")
    private String productTypeId;

    private List<String> publishedChannels;

    private Map<String, AttributeItemProperties> attributes;
}
