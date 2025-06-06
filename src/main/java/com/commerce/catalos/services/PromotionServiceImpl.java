package com.commerce.catalos.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.utils.TimeUtils;
import com.commerce.catalos.helpers.PromotionHelper;
import com.commerce.catalos.models.promotions.CreatePromotionRequest;
import com.commerce.catalos.models.promotions.CreatePromotionResponse;
import com.commerce.catalos.persistence.dtos.Discount;
import com.commerce.catalos.persistence.repositories.PromotionRepository;
import com.commerce.catalos.security.AuthContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final ProductService productService;

    private final VariantService variantService;

    private final ChannelService channelService;

    private final PromotionRepository promotionRepository;

    private final AuthContext authContext;

    @Override
    public CreatePromotionResponse createPromotion(final CreatePromotionRequest createPromotionRequest) {
        Discount discount = switch (createPromotionRequest.getDiscountType()) {
            case PercentageOFF -> this.createPercentageOFFDiscount(createPromotionRequest);
            default ->
                throw new IllegalArgumentException("Unexpected value: " + createPromotionRequest.getDiscountType());
        };
        return PromotionHelper.toCreatePromotionResponseFromDiscount(discount);
    }

    private List<String> validateProductIds(final List<String> productIds) {
        return productService.getProductsByIds(productIds).stream().map(product -> product.getId()).toList();
    }

    private List<String> validateVariantIds(final List<String> variantIds) {
        return variantService.getVariantsByIds(variantIds).stream().map(variant -> variant.getId()).toList();
    }

    private void isValidChannel(final String channelId) {
        channelService.verifyChannels(List.of(channelId), true);
    }

    public Discount createPercentageOFFDiscount(final CreatePromotionRequest createPromotionRequest) {
        Logger.info("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Validating Percentage Off Discount {}",
                createPromotionRequest.getName());
        Discount discount = new Discount();
        discount.setName(createPromotionRequest.getName());
        discount.setDiscountMode(createPromotionRequest.getDiscountMode());
        discount.setDiscountType(createPromotionRequest.getDiscountType());

        if (createPromotionRequest.getDiscountValue() == null
                || createPromotionRequest.getDiscountValue() <= 0 || createPromotionRequest.getDiscountValue() > 100) {
            Logger.error("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Invalid discount value: {}",
                    createPromotionRequest.getDiscountValue());
            throw new BadRequestException("Invalid discount value");
        }

        Logger.info("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Setting discount value to {}",
                createPromotionRequest.getDiscountValue());
        discount.setDiscountValue(createPromotionRequest.getDiscountValue());

        if (createPromotionRequest.getMaxDiscountPrice() != null) {
            Logger.info("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Setting max discount price to {}",
                    createPromotionRequest.getMaxDiscountPrice());
            discount.setMaxDiscountPrice(createPromotionRequest.getMaxDiscountPrice());
        }

        if (createPromotionRequest.getDiscountedProducts() != null
                && !createPromotionRequest.getDiscountedProducts().isEmpty()) {
            Logger.info("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Setting discounted products to {}",
                    createPromotionRequest.getDiscountedProducts());
            discount.setDiscountedProducts(this.validateProductIds(createPromotionRequest.getDiscountedProducts()));
        }

        discount.setMinItemQuantity(createPromotionRequest.getMinItemQuantity());
        discount.setForAllProducts(createPromotionRequest.isForAllProducts());

        if (createPromotionRequest.getTargetedProductIds() != null
                && !createPromotionRequest.getTargetedProductIds().isEmpty()) {
            Logger.info("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Setting targeted products to {}",
                    createPromotionRequest.getTargetedProductIds());
            discount.setTargetedProductIds(this.validateProductIds(createPromotionRequest.getTargetedProductIds()));
        }

        if (createPromotionRequest.getTargetedVariantIds() != null
                && !createPromotionRequest.getTargetedVariantIds().isEmpty()) {
            Logger.info("3bc58c3a-5429-41b4-bea7-9bc1266df05c", "Setting targeted variants to {}",
                    createPromotionRequest.getTargetedVariantIds());
            discount.setTargetedVariantIds(this.validateVariantIds(createPromotionRequest.getTargetedVariantIds()));
        }

        // TODO: Implement the logic verify categories
        // TODO: Implement the logic verify collections
        // TODO: Implement the logic verify brands
        // TODO: Implement the logic verify customer groups

        if (TimeUtils.isFutureDate(createPromotionRequest.getStartDate())) {
            Logger.info("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Setting start date to {}",
                    createPromotionRequest.getStartDate());
            discount.setStartDate(createPromotionRequest.getStartDate());
        } else {
            Logger.error("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Start date cannot be in the past");
            throw new BadRequestException("Start date cannot be in the past");
        }

        if (TimeUtils.isFutureDate(createPromotionRequest.getExpireDate()) &&
                createPromotionRequest.getExpireDate().after(createPromotionRequest.getStartDate())) {
            Logger.info("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Setting expire date to {}",
                    createPromotionRequest.getExpireDate());
            discount.setExpireDate(createPromotionRequest.getExpireDate());
        } else {
            Logger.error("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Expire date cannot be in the past");
            throw new BadRequestException("Expire date cannot be in the past");
        }

        this.isValidChannel(createPromotionRequest.getAvailableChannel());
        Logger.info("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Setting available channel to {}",
                createPromotionRequest.getAvailableChannel());
        discount.setAvailableChannel(createPromotionRequest.getAvailableChannel());

        discount.setCreatedBy(authContext.getCurrentUser().getEmail());
        discount.setCreatedAt(new Date());
        discount.setEnabled(true);
        discount.setActive(true);
        Logger.info("91553c66-6d0c-44d6-bd38-5f3efa490d6d", "Creating discount with name: {}",
                createPromotionRequest.getName());
        return promotionRepository.save(discount);
    }

    @Override
    public List<Discount> getActiveDiscountsForVariant(final String variantId, final String channelId,
            final Integer quantity, final String customerGroupId) {
        Logger.info("e7367f24-ed2f-48e0-b8e1-c16b6073127f", "Fetching active discounts for variant: {} and channel: {}",
                variantId, channelId);
        return null;
    }

}
