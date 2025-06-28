package com.commerce.catalos.models.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRequestLineItem {

    private String variantId;

    private Integer quantity;
}
