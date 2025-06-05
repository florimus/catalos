package com.commerce.catalos.services;

import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.prices.SkuPriceResponse;
import com.commerce.catalos.models.prices.UpsertPriceRequest;
import com.commerce.catalos.models.prices.UpsertPriceResponse;

public interface PriceService {

    UpsertPriceResponse upsertPrice(final UpsertPriceRequest upsertPriceRequest);

    SkuPriceResponse getTablePriceBySku(final String skuId);

    CalculatedPriceResponse getPriceOfSku(final String skuId, final String channelId);

}
