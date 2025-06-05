package com.commerce.catalos.models.prices;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpsertPriceRequest {

    @NotBlank(message = "sku Id is mandatory")
    private String skuId;

    private Map<String, PriceInfo> priceInfo;
}
