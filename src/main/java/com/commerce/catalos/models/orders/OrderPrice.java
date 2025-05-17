package com.commerce.catalos.models.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPrice {

    private float subtotalPrice;

    private float totalTaxPrice;

    private float shippingPrice;

    private float totalDiscountPrice;

    private float grandTotalPrice;
}
