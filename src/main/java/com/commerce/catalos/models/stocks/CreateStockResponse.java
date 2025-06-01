package com.commerce.catalos.models.stocks;

import java.util.Map;

import lombok.Data;

@Data
public class CreateStockResponse {

    private String id;

    private String variantId;

    private Map<String, StockInfo> stockInfo;

    private boolean active;
}
