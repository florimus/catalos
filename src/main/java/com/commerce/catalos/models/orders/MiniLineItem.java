package com.commerce.catalos.models.orders;

import com.commerce.catalos.models.variants.MiniVariantResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniLineItem {

    private String id;

    private String productName;

    private MiniVariantResponse variant;

    private Integer quantity;

    private LineItemPrice itemPrice;

    private LineItemError error;

}
