package com.commerce.catalos.services;

import com.commerce.catalos.models.stocks.*;
import org.springframework.stereotype.Service;

@Service
public class StockServiceImpl implements StockService {

    @Override
    public CreateStockResponse createStock(final CreateStockRequest createStockRequest) {
        return null;
    }

    @Override
    public UpdateStockResponse updateStock(final UpdateStockRequest updateStockRequest) {
        return null;
    }

    @Override
    public VariantStockResponse getStockByVariantId(final String variantId) {
        return null;
    }
}
