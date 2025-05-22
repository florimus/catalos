package com.commerce.catalos.models.productTypes;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class UpdateProductTypeRequest {

    @NotBlank(message = "product-type id is mandatory")
    private String id;

    @NotBlank(message = "name is mandatory")
    private String name;

    private Map<String, AttributeItemProperties> productAttributes;

    private Map<String, AttributeItemProperties> variantAttributes;

}
