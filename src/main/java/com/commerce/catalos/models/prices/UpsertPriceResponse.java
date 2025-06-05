package com.commerce.catalos.models.prices;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpsertPriceResponse {

    private String id;

    private String skuId;

    private Map<String, PriceInfo> priceInfo;
}
