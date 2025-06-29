package com.commerce.catalos.models.variants;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MiniVariantResponse {

    private String id;

    private String name;

    private String slug;

    private String productId;

    private String skuId;

    private String productTypeId;

    private String url;

    private List<ProductMedia> medias;

    private boolean active;
}
