package com.commerce.catalos.models.variants;

import java.util.List;
import java.util.Map;

import com.commerce.catalos.models.productTypes.AttributeItemProperties;

import lombok.Data;

@Data
public class UpdateVariantRequest {

    private String name;

    private String seoTitle;

    private String seoDescription;

    private List<ProductMedia> medias;

    private Map<String, AttributeItemProperties> attributes;
}
