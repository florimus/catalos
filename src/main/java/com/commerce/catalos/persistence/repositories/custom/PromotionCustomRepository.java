package com.commerce.catalos.persistence.repositories.custom;

import java.util.List;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.promotions.PromotionFilterInputs;
import com.commerce.catalos.persistence.dtos.Discount;
import org.springframework.data.domain.Pageable;

public interface PromotionCustomRepository {

    List<Discount> getActiveDiscounts(final String variantId, final String productId, final String channelId,
            final Integer quantity, final String customerGroupId, final String categoryId, final String brandId,
            final String collectionId);

    Page<Discount> getPromotions(final String search, final String channel,
                                 final PromotionFilterInputs promotionFilterInputs, final Pageable pageable);
}
