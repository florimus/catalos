package com.commerce.catalos.models.prices;

import com.commerce.catalos.persistence.dtos.Discount;
import lombok.Data;

@Data
public class BestDiscountHolder {
    private Discount bestDiscount;
    private float bestPrice = Float.MAX_VALUE;

    public void checkAndUpdateBestDiscount(Discount discount, float candidatePrice) {
        if (discount == null)
            return;
        if (candidatePrice < bestPrice) {
            this.bestDiscount = discount;
            this.bestPrice = candidatePrice;
        }
    }
}
