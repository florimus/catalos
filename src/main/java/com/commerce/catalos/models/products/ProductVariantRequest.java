package com.commerce.catalos.models.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantRequest {

    private List<String> productIds;

    private List<String> variantIds;
}
