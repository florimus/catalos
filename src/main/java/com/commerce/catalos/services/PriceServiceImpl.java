package com.commerce.catalos.services;

import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.PriceHelper;
import com.commerce.catalos.helpers.StockHelper;
import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.prices.SkuPriceResponse;
import com.commerce.catalos.models.prices.UpsertPriceRequest;
import com.commerce.catalos.models.prices.UpsertPriceResponse;
import com.commerce.catalos.persistence.dtos.Price;
import com.commerce.catalos.persistence.dtos.Stock;
import com.commerce.catalos.persistence.repositories.PriceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    private Price findPriceBySkuId(final String skuId) {
        return priceRepository.findBySkuIdAndEnabled(skuId, true);
    }

    @Override
    public UpsertPriceResponse upsertPrice(UpsertPriceRequest upsertPriceRequest) {
        Price price = this.findPriceBySkuId(upsertPriceRequest.getSkuId());
        if (null == price) {
            Logger.info("cfdfd20b-5371-4c39-a52b-d8663309d7d6", "Price not exits for the variant");
            price = priceRepository.save(PriceHelper.toPriceFromUpsertPriceRequest(upsertPriceRequest));
        } else {
            Logger.info("846f53f5-ed40-4d95-89c3-0d05fb4b4c73", "Price updating");
            price.setPriceInfo(upsertPriceRequest.getPriceInfo());
            price = priceRepository.save(price);
        }
        return PriceHelper.toUpsertPriceResponseFromPrice(price);
    }

    @Override
    public SkuPriceResponse getTablePriceBySku(String skuId) {
        Price price = this.findPriceBySkuId(skuId);
        if (null == price) {
            Logger.error("9872db33-78ee-4699-88eb-0d738bd49bbe", "Price not exits for the sku");
            throw new NotFoundException("Price not exits for sku " + skuId);
        }
        return PriceHelper.toSkuPriceResponseFromPrice(price);
    }

    @Override
    public CalculatedPriceResponse getPriceOfSku(String skuId, String channelId) {
        return null;
    }

}
