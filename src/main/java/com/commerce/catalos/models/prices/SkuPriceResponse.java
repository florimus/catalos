package com.commerce.catalos.models.prices;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuPriceResponse {

    private String id;

    private String skuId;

    private Map<String, PriceInfo> priceInfo;
}
