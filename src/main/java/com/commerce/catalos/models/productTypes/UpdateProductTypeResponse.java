package com.commerce.catalos.models.productTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductTypeResponse {

    private String id;

    private String name;

    private String slug;

    private Map<String, AttributeItemProperties> productAttributes;

    private Map<String, AttributeItemProperties> variantAttributes;

    private boolean active;
}
