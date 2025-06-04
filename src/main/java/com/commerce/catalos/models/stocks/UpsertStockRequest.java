package com.commerce.catalos.models.stocks;

import lombok.Data;

import java.util.Map;

@Data
public class UpsertStockRequest {

    private String variantId;

    private Map<String, StockInfo> stockInfo;
}
