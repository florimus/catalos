package com.commerce.catalos.models.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestLineItem {

    private String variantId;

    private Integer quantity;
}
