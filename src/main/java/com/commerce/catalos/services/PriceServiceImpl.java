package com.commerce.catalos.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.PriceHelper;
import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.prices.SkuPriceResponse;
import com.commerce.catalos.models.prices.UpsertPriceRequest;
import com.commerce.catalos.models.prices.UpsertPriceResponse;
import com.commerce.catalos.persistence.dtos.Price;
import com.commerce.catalos.persistence.repositories.PriceRepository;
import com.commerce.catalos.pricing.PricingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    private final ChannelService channelService;

    private final PricingService pricingService;

    private Price findPriceBySkuId(final String skuId) {
        return priceRepository.findBySkuIdAndEnabled(skuId, true);
    }

    @Override
    public UpsertPriceResponse upsertPrice(UpsertPriceRequest upsertPriceRequest) {
        Price price = this.findPriceBySkuId(upsertPriceRequest.getSkuId());
        if (null == price) {
            price = createNewPrice(upsertPriceRequest);
        } else {
            price = updateExistingPrice(price, upsertPriceRequest);
        }
        return PriceHelper.toUpsertPriceResponseFromPrice(price);
    }

    private Price createNewPrice(final UpsertPriceRequest upsertPriceRequest) {
        Logger.info("cfdfd20b-5371-4c39-a52b-d8663309d7d6", "Price not exits for the variant");
        channelService.verifyChannels(upsertPriceRequest.getPriceInfo().keySet().stream().toList(), true);
        return priceRepository.save(PriceHelper.toPriceFromUpsertPriceRequest(upsertPriceRequest));
    }

    @CacheEvict(value = "skuPriceCache", key = "#upsertPriceRequest.skuId + '-*'", allEntries = true)
    private Price updateExistingPrice(final Price price, final UpsertPriceRequest upsertPriceRequest) {
        Logger.info("846f53f5-ed40-4d95-89c3-0d05fb4b4c73", "Price updating");
        channelService.verifyChannels(upsertPriceRequest.getPriceInfo().keySet().stream().toList(), true);
        price.setPriceInfo(upsertPriceRequest.getPriceInfo());
        return priceRepository.save(price);
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
    @Cacheable(value = "skuPriceCache", key = "#skuId + '-' + #channelId + '-' + #quantity")
    public CalculatedPriceResponse getPriceOfSku(final String skuId, final String channelId, final Integer quantity) {
        return pricingService.getPriceOfSku(skuId, channelId, this.getTablePriceBySku(skuId), quantity, null);
    }

}
