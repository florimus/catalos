package com.commerce.catalos.models.productTypes;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProductTypeRequest {

    @NotBlank(message = "name is mandatory")
    private String name;

    @NotBlank(message = "slug is mandatory")
    private String slug;

    private Map<String, AttributeItemProperties> productAttributes;

    private Map<String, AttributeItemProperties> variantAttributes;

}
