package com.commerce.catalos.pricing;

import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.prices.SkuPriceResponse;

public interface PricingService {

    CalculatedPriceResponse getPriceOfSku(String skuId, String channelId, final SkuPriceResponse skuPriceResponse,
            final Integer quantity, final String customerGroupId);
}
