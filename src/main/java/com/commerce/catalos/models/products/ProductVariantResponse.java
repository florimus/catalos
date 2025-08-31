package com.commerce.catalos.models.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantResponse {

    private String productId;

    private String productName;

    private List<Map<String, String>> variants;
}
