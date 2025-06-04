package com.commerce.catalos.models.stocks;

import lombok.Data;

import java.util.Map;

@Data
public class UpdateStockResponse {

    private String id;

    private String variantId;

    private Map<String, StockInfo> stockInfo;

    private boolean active;
}
