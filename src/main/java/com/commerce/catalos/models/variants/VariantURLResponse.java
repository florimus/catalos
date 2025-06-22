package com.commerce.catalos.models.variants;

import java.util.List;
import java.util.Map;

import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.productTypes.AttributeItemProperties;
import com.commerce.catalos.models.productTypes.ProductTypeResponse;
import com.commerce.catalos.models.products.ProductResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantURLResponse {

    private String id;

    private String name;

    private String slug;

    private String productId;

    private String skuId;

    private String productTypeId;

    private String url;

    private String seoTitle;

    private String seoDescription;

    private List<ProductMedia> medias;

    private Map<String, AttributeItemProperties> attributes;

    private boolean active;

    private ProductResponse productInfo;

    private ProductTypeResponse ProductType;

    private CalculatedPriceResponse prices;
}
