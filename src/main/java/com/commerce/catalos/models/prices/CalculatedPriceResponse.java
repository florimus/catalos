package com.commerce.catalos.models.prices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculatedPriceResponse {

    private float salesPrice;

    private String discountName;

    private float discountedPrice;

    private float discountPercentage;

    private float discountFlatPrice;

    private float taxPrice;

    private float taxValue;

    private boolean isFixedTax;

    private float finalPrice;

}
