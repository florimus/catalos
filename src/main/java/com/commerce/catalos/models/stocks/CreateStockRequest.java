package com.commerce.catalos.models.stocks;

import java.util.Map;

import lombok.Data;

@Data
public class CreateStockRequest {

    private String variantId;

    private Map<String, StockInfo> stockInfo;
}
