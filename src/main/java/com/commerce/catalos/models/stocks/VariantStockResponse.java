package com.commerce.catalos.models.stocks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VariantStockResponse {

    private String id;

    private String variantId;

    private Map<String, StockInfo> stockInfo;

    private boolean active;
}
