package com.commerce.catalos.services;

import com.commerce.catalos.models.stocks.*;

public interface StockService {

    CreateStockResponse createStock(final CreateStockRequest createStockRequest);

    UpdateStockResponse updateStock(final UpdateStockRequest updateStockRequest);

    VariantStockResponse getStockByVariantId(final String variantId);
}
