package com.commerce.catalos.helpers;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.promotions.CreatePromotionResponse;
import com.commerce.catalos.persistence.dtos.Discount;

public class PromotionHelper {

    public static CreatePromotionResponse toCreatePromotionResponseFromDiscount(final Discount discount) {
        CreatePromotionResponse response = new CreatePromotionResponse();
        BeanUtils.copyProperties(discount, response);
        return response;
    }
}
