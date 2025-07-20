package com.commerce.catalos.models.orders;

import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.variants.VariantResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineItem {

    private String id;

    private ProductResponse product;

    private VariantResponse variant;

    private List<String> unitIds;

    private List<String> packageIds;

    private Integer quantity;

    private LineItemPrice itemPrice;

    private LineItemError error;

}
