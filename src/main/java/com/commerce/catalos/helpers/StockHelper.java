package com.commerce.catalos.helpers;

import com.commerce.catalos.models.stocks.UpsertStockRequest;
import com.commerce.catalos.models.stocks.UpsertStockResponse;
import com.commerce.catalos.models.stocks.VariantStockResponse;
import com.commerce.catalos.persistence.dtos.Stock;
import org.springframework.beans.BeanUtils;

public class StockHelper {

    public static Stock toStockFromUpsertStockRequest(final UpsertStockRequest upsertStockRequest) {
        Stock stock = new Stock();
        BeanUtils.copyProperties(upsertStockRequest, stock);
        return stock;
    }

    public static UpsertStockResponse toUpsertStockResponseFromStock(final Stock stock) {
        UpsertStockResponse response = new UpsertStockResponse();
        BeanUtils.copyProperties(stock, response);
        return response;
    }

    public static VariantStockResponse toVariantStockResponseFromStock(final Stock stock) {
        VariantStockResponse response = new VariantStockResponse();
        BeanUtils.copyProperties(stock, response);
        return response;
    }
}