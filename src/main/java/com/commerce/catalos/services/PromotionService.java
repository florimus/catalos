package com.commerce.catalos.services;

import java.util.List;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.promotions.*;
import com.commerce.catalos.persistence.dtos.Discount;
import org.springframework.data.domain.Pageable;

public interface PromotionService {

    CreatePromotionResponse createPromotion(final CreatePromotionRequest createPromotionRequest);

    List<Discount> getActiveDiscountsForVariant(final String variantId, final String channelId, final Integer quantity,
            final String customerGroupId);

    Page<PromotionResponse> searchPromotions(
            final String query, final String channel, final PromotionFilterInputs promotionFilterInputs, final Pageable pageable);

    PromotionResponse getPromotionById(final String id);

    PromotionResponse updatePromotion(final String id, final UpdatePromotionRequest updatePromotionRequest);
}
