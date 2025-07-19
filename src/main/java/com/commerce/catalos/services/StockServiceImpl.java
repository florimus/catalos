package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.StockHelper;
import com.commerce.catalos.models.stocks.*;
import com.commerce.catalos.persistence.dtos.Stock;
import com.commerce.catalos.persistence.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        if (null == stock) {
            Logger.info("2c241209-1d78-4048-af5c-1a9a4215f6dc", "Stock not exits for the variant");
            stock = stockRepository.save(StockHelper.toStockFromUpsertStockRequest(upsertStockRequest));
        } else {
            Logger.info("851615a2-b7a5-49f3-8d5a-8dadacfc2167", "Stock updating");
            stock.setStockInfo(upsertStockRequest.getStockInfo());
            stock = stockRepository.save(stock);
        }

        return StockHelper.toUpsertStockResponseFromStock(stock);
    }

    @Override
    public VariantStockResponse getStockByVariantId(final String variantId) {
        Stock stock = this.findStockByVariantId(variantId);
        if (null == stock) {
            Logger.error("375a37ff-ec91-4899-b17d-a447850f6bda", "Stock not exits for the variant");
            throw new NotFoundException("Stock not exits for variant " + variantId);
        }
        return StockHelper.toVariantStockResponseFromStock(stock);
    }

    @Override
    public StockInfo getStockInfoByVariantIdAndChannel(String variantId, String channel) {
        Stock stock = this.findStockByVariantId(variantId);
        if (null == stock || !stock.isActive()) {
            Logger.error("375a37ff-ec91-4899-b17d-a447850f6bda", "Stock not exits for the variant");
            return null;
        }
        return stock.getStockInfo().get(channel);
    }

    @Override
    public void updateVariantStockInChannel(final String variantId, final String channelId, final Integer reservation) {
        Stock stock = this.findStockByVariantId(variantId);
        if (null == stock || !stock.isActive()) {
            Logger.error("", "Stock not exits for the variant");
            throw new ConflictException("Order cannot be placed, since there is not stock");
        }
        Map<String, StockInfo> stockInfos = stock.getStockInfo();
        StockInfo currentStockInfo = stockInfos.get(channelId);
        if (null == currentStockInfo) {
            Logger.error("", "Stock not exits for the variant in this channel");
            throw new ConflictException("Order cannot be placed, since there is not stock in this channel");
        }
        currentStockInfo.setReservedStocks(currentStockInfo.getReservedStocks() + reservation);
        stockInfos.put(channelId, currentStockInfo);
        stock.setStockInfo(stockInfos);
        Logger.info("", "Updating the stock reserved number");
        stockRepository.save(stock);
    }
}
