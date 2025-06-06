package com.commerce.catalos.persistence.repositories.custom;

import java.util.List;

import com.commerce.catalos.persistence.dtos.Discount;

public interface PromotionCustomRepository {

    List<Discount> getActiveDiscounts(final String variantId, final String productId, final String channelId,
            final Integer quantity, final String customerGroupId, final String categoryId, final String brandId,
            final String collectionId);
}
