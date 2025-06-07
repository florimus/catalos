package com.commerce.catalos.models.prices;

import com.commerce.catalos.persistence.dtos.Discount;
import lombok.Data;

@Data
public class BestDiscountHolder {

    private Discount bestDiscount;

    private float bestDiscountedPrice;
}