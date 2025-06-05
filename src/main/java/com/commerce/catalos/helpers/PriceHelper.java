package com.commerce.catalos.helpers;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.prices.SkuPriceResponse;
import com.commerce.catalos.models.prices.UpsertPriceRequest;
import com.commerce.catalos.models.prices.UpsertPriceResponse;
import com.commerce.catalos.persistence.dtos.Price;

public class PriceHelper {

    public static Price toPriceFromUpsertPriceRequest(final UpsertPriceRequest upsertPriceRequest) {
        Price price = new Price();
        BeanUtils.copyProperties(upsertPriceRequest, price);
        return price;
    }

    public static UpsertPriceResponse toUpsertPriceResponseFromPrice(final Price price) {
        UpsertPriceResponse response = new UpsertPriceResponse();
        BeanUtils.copyProperties(price, response);
        return response;
    }

    public static SkuPriceResponse toSkuPriceResponseFromPrice(final Price price) {
        SkuPriceResponse response = new SkuPriceResponse();
        BeanUtils.copyProperties(price, response);
        return response;
    }

}
