package com.commerce.catalos.models.stocks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockInfo {

    private Integer totalStocks;

    private Integer reservedStocks;

    private Integer safetyStocks;
}
