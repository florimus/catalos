package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.StockHelper;
import com.commerce.catalos.models.stocks.*;
import com.commerce.catalos.persistence.dtos.Stock;
import com.commerce.catalos.persistence.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    private Stock findStockByVariantId(final String variantId) {
        return stockRepository.findByVariantIdAndEnabled(variantId, true);
    }

    @Override
    public UpsertStockResponse upsertStock(final UpsertStockRequest upsertStockRequest) {
        Stock stock = this.findStockByVariantId(upsertStockRequest.getVariantId());
        if(null == stock) {
            Logger.info("uuid", "Stock not exits for the variant");
        } else {
            Logger.info("uuid", "Stock updating");
            stock.setStockInfo(upsertStockRequest.getStockInfo());
        }
        stock = stockRepository.save(StockHelper.toStockFromUpsertStockRequest(upsertStockRequest));
        return StockHelper.toUpsertStockResponseFromStock(stock);
    }

    @Override
    public VariantStockResponse getStockByVariantId(final String variantId) {
        Stock stock = this.findStockByVariantId(variantId);
        if(null == stock) {
            Logger.error("uuid", "Stock not exits for the variant");
            throw new NotFoundException("Stock not exits for variant" + variantId);
        }
        return StockHelper.toVariantStockResponseFromStock(stock);
    }
}
