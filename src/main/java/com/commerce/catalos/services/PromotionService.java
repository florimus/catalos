package com.commerce.catalos.services;

import java.util.List;

import com.commerce.catalos.models.promotions.CreatePromotionRequest;
import com.commerce.catalos.models.promotions.CreatePromotionResponse;
import com.commerce.catalos.persistence.dtos.Discount;

public interface PromotionService {

    CreatePromotionResponse createPromotion(final CreatePromotionRequest createPromotionRequest);

    List<Discount> getActiveDiscountsForVariant(final String variantId, final String channelId, final Integer quantity,
            final String customerGroupId);

}
