package com.commerce.catalos.models.prices;

import java.util.Map;

import lombok.Data;

@Data
public class UpsertPriceRequest {

    private String skuId;

    private Map<String, PriceInfo> priceInfo;
}
