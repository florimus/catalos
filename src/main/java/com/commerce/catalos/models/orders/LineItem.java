package com.commerce.catalos.models.orders;

import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.variants.VariantResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineItem {

    private String id;

    private ProductResponse product;

    private VariantResponse variant;

    private Integer quantity;

    private LineItemPrice itemPrice;

}
