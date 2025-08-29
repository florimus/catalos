package com.commerce.catalos.helpers;

import com.commerce.catalos.models.promotions.PromotionResponse;
import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.promotions.CreatePromotionResponse;
import com.commerce.catalos.persistence.dtos.Discount;

import java.util.List;

public class PromotionHelper {

    public static CreatePromotionResponse toCreatePromotionResponseFromDiscount(final Discount discount) {
        CreatePromotionResponse response = new CreatePromotionResponse();
        BeanUtils.copyProperties(discount, response);
        return response;
    }

    public static PromotionResponse toPromotionResponseFromDiscount(final Discount discount){
        PromotionResponse response = new PromotionResponse();
        BeanUtils.copyProperties(discount, response);
        return response;
    }

    public static List<PromotionResponse> toPromotionResponsesFromDiscounts(final List<Discount> discounts) {
        return discounts.stream()
                .map(PromotionHelper::toPromotionResponseFromDiscount)
                .toList();
    }
}
