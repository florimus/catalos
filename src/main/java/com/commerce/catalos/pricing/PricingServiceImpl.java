package com.commerce.catalos.pricing;

import java.util.List;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.prices.PriceInfo;
import com.commerce.catalos.models.prices.SkuPriceResponse;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.variants.VariantResponse;
import com.commerce.catalos.persistence.dtos.Discount;
import com.commerce.catalos.persistence.repositories.PromotionRepository;
import com.commerce.catalos.services.ProductService;
import com.commerce.catalos.services.VariantService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PricingServiceImpl implements PricingService {

    private final VariantService variantService;

    private final ProductService productService;

    private final PromotionRepository promotionRepository;

    private final KieContainer kieContainer;

    private CalculatedPriceResponse calculatePrice(final PriceInfo priceInfo, final List<Discount> applicableDiscounts,
            final Integer quantity) {

        CalculatedPriceResponse calculatedPriceResponse = new CalculatedPriceResponse();
        KieSession kieSession = kieContainer.newKieSession();
        kieSession.setGlobal("calculatedPriceResponse", calculatedPriceResponse);

        kieSession.insert(priceInfo);
        kieSession.insert(quantity);
        applicableDiscounts.forEach(kieSession::insert);

        kieSession.fireAllRules();
        kieSession.dispose();

        return calculatedPriceResponse;
    }

    private PriceInfo getTablePriceBySku(final SkuPriceResponse skuPriceResponse, final String channelId) {
        if (skuPriceResponse == null) {
            Logger.error("b8a2df55-0afa-4447-ac65-c5224c8a433c", "Price not found for SKU: {}", channelId);
            throw new NotFoundException("Price not found for SKU: " + channelId);
        }
        PriceInfo priceInfo = skuPriceResponse.getPriceInfo().get(channelId);
        return priceInfo;
    }

    @Override
    public CalculatedPriceResponse getPriceOfSku(final String skuId, final String channelId,
            final SkuPriceResponse skuPriceResponse, final Integer quantity, final String customerGroupId) {

        VariantResponse variant = this.variantService.getVariantBySkuId(skuId);
        if (variant == null) {
            Logger.error("c495cbeb-92a8-424d-b8a2-8e823ce345ef", "Variant not exits for SKU: {}", skuId);
            throw new NotFoundException("Variant not found for SKU: " + skuId);
        }

        ProductResponse product = this.productService.getProductById(variant.getProductId());
        if (product == null) {
            Logger.error("9d2fe949-22b6-4503-978b-410c8263f7fe", "Product not exits for SKU: {}", skuId);
            throw new NotFoundException("Product not found for SKU: " + skuId);

        }

        Logger.info("a5e2582c-7f7d-4dfd-b185-40e486a2eab5", "Fetched variant: {} for SKU: {}", variant.getName(),
                skuId);

        PriceInfo priceInfo = this.getTablePriceBySku(skuPriceResponse, channelId);
        if (priceInfo == null) {
            Logger.info("77ac63b5-0ab2-4da5-becd-6bb1ada26e4b", "Price info not found for SKU: {}: and channel {}",
                    skuId, channelId);
            throw new NotFoundException("Price info not found for SKU: " + skuId + " and channel: " + channelId);
        }
        Logger.info("d1f3c5b2-8c4e-4f0a-9b6d-7f8c1e2b3a4c", "Fetched price info: {} for SKU: {} and channel: {}",
                priceInfo, skuId, channelId);

        Logger.info("b1c2d3e4-5f6a-7b8c-9d0e-f1a2b3c4d5e6",
                "Searching discounts for variantId: {}, product: {}, channel: {}, quantity: {}, customerGroupId: {}",
                variant.getId(), product.getId(), channelId, quantity, customerGroupId);
        List<Discount> allDiscounts = this.promotionRepository.getActiveDiscounts(variant.getId(), product.getId(),
                channelId, quantity, customerGroupId, null, null, null);

        return this.calculatePrice(priceInfo, allDiscounts, quantity);
    }

}
