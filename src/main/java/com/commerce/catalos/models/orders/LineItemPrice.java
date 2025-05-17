package com.commerce.catalos.models.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineItemPrice {

    private float salesPrice;

    private String discountName;

    private float discountedPrice;

    private float discountPercentage;

    private float discountFlatPrice;

    private float taxPrice;

    private float finalPrice;
}
