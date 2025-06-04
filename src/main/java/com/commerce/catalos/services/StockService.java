package com.commerce.catalos.services;

import com.commerce.catalos.models.stocks.*;

public interface StockService {

    UpsertStockResponse upsertStock(final UpsertStockRequest upsertStockRequest);

    VariantStockResponse getStockByVariantId(final String variantId);
}
