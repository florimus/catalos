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
            case FlatOFF -> this.createFlatOFFDiscount(createPromotionRequest);
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

    public Discount createFlatOFFDiscount(final CreatePromotionRequest createPromotionRequest) {
        Logger.info("8013b8ff-a153-423c-ac1b-f647cc7fe609", "Validating Flat Off Discount {}",
                createPromotionRequest.getName());
        Discount discount = new Discount();
        discount.setName(createPromotionRequest.getName());
        discount.setDiscountMode(createPromotionRequest.getDiscountMode());
        discount.setDiscountType(createPromotionRequest.getDiscountType());
        discount.setDiscountValue(createPromotionRequest.getDiscountValue());
        discount.setMaxDiscountPrice(createPromotionRequest.getMaxDiscountPrice());

        if (createPromotionRequest.getDiscountedProducts() != null
                && !createPromotionRequest.getDiscountedProducts().isEmpty()) {
            Logger.info("574a0e91-f38e-4bfc-b5a9-98be719ef2f2", "Setting discounted products to {}",
                    createPromotionRequest.getDiscountedProducts());
            discount.setDiscountedProducts(this.validateProductIds(createPromotionRequest.getDiscountedProducts()));
        }

        discount.setMinItemQuantity(createPromotionRequest.getMinItemQuantity());
        discount.setForAllProducts(createPromotionRequest.isForAllProducts());

        if (createPromotionRequest.getTargetedProductIds() != null
                && !createPromotionRequest.getTargetedProductIds().isEmpty()) {
            Logger.info("be380ae6-f592-4e5e-b58c-1a6f879d5d51", "Setting targeted products to {}",
                    createPromotionRequest.getTargetedProductIds());
            discount.setTargetedProductIds(this.validateProductIds(createPromotionRequest.getTargetedProductIds()));
        }

        if (createPromotionRequest.getTargetedVariantIds() != null
                && !createPromotionRequest.getTargetedVariantIds().isEmpty()) {
            Logger.info("dee18d65-1a1a-415c-8bab-059bb48aefe0", "Setting targeted variants to {}",
                    createPromotionRequest.getTargetedVariantIds());
            discount.setTargetedVariantIds(this.validateVariantIds(createPromotionRequest.getTargetedVariantIds()));
        }

        // TODO: Implement the logic verify categories
        // TODO: Implement the logic verify collections
        // TODO: Implement the logic verify brands
        // TODO: Implement the logic verify customer groups

        if (TimeUtils.isFutureDate(createPromotionRequest.getStartDate())) {
            Logger.info("4b8689d3-3bd1-47ba-933d-695a6391449f", "Setting start date to {}",
                    createPromotionRequest.getStartDate());
            discount.setStartDate(createPromotionRequest.getStartDate());
        } else {
            Logger.error("04a8bdb4-7ed1-42bb-968d-f5f661234c4f", "Start date cannot be in the past");
            throw new BadRequestException("Start date cannot be in the past");
        }

        if (TimeUtils.isFutureDate(createPromotionRequest.getExpireDate()) &&
                createPromotionRequest.getExpireDate().after(createPromotionRequest.getStartDate())) {
            Logger.info("e857d8d4-9df6-48d4-a4a0-18cd5dcca1f8", "Setting expire date to {}",
                    createPromotionRequest.getExpireDate());
            discount.setExpireDate(createPromotionRequest.getExpireDate());
        } else {
            Logger.error("6b37abea-6689-4e16-9018-c52592af90b6", "Expire date cannot be in the past");
            throw new BadRequestException("Expire date cannot be in the past");
        }

        this.isValidChannel(createPromotionRequest.getAvailableChannel());
        Logger.info("af6f051c-b0ad-4157-ba22-a8f679ccc5ec", "Setting available channel to {}",
                createPromotionRequest.getAvailableChannel());
        discount.setAvailableChannel(createPromotionRequest.getAvailableChannel());

        discount.setCreatedBy(authContext.getCurrentUser().getEmail());
        discount.setCreatedAt(new Date());
        discount.setEnabled(true);
        discount.setActive(true);
        Logger.info("fd0ac589-0603-4fa3-9ddf-45428ffeb38ba", "Creating discount with name: {}",
                createPromotionRequest.getName());
        return promotionRepository.save(discount);
    }

    public Discount createPercentageOFFDiscount(final CreatePromotionRequest createPromotionRequest) {
        Logger.info("089a0eaf-0436-48b6-ae44-e3415b313793", "Validating Percentage Off Discount {}",
                createPromotionRequest.getName());
        Discount discount = new Discount();
        discount.setName(createPromotionRequest.getName());
        discount.setDiscountMode(createPromotionRequest.getDiscountMode());
        discount.setDiscountType(createPromotionRequest.getDiscountType());

        if (createPromotionRequest.getDiscountValue() == null
                || createPromotionRequest.getDiscountValue() <= 0 || createPromotionRequest.getDiscountValue() > 100) {
            Logger.error("2bb37130-487c-4c20-abc6-88e5e3a15d98", "Invalid discount value: {}",
                    createPromotionRequest.getDiscountValue());
            throw new BadRequestException("Invalid discount value");
        }

        Logger.info("950e2ee3-490d-455f-a659-63a1a592444a", "Setting discount value to {}",
                createPromotionRequest.getDiscountValue());
        discount.setDiscountValue(createPromotionRequest.getDiscountValue());

        if (createPromotionRequest.getMaxDiscountPrice() != null) {
            Logger.info("018cf16e-08c2-47c6-b5ef-e147ed5ed9ed", "Setting max discount price to {}",
                    createPromotionRequest.getMaxDiscountPrice());
            discount.setMaxDiscountPrice(createPromotionRequest.getMaxDiscountPrice());
        }

        if (createPromotionRequest.getDiscountedProducts() != null
                && !createPromotionRequest.getDiscountedProducts().isEmpty()) {
            Logger.info("7012dbea-aa0f-4d1b-b8b6-1a08d08ab3f3", "Setting discounted products to {}",
                    createPromotionRequest.getDiscountedProducts());
            discount.setDiscountedProducts(this.validateProductIds(createPromotionRequest.getDiscountedProducts()));
        }

        discount.setMinItemQuantity(createPromotionRequest.getMinItemQuantity());
        discount.setForAllProducts(createPromotionRequest.isForAllProducts());

        if (createPromotionRequest.getTargetedProductIds() != null
                && !createPromotionRequest.getTargetedProductIds().isEmpty()) {
            Logger.info("6350320a-065c-4e81-b815-e4df12b5f912", "Setting targeted products to {}",
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
            Logger.info("00cc6b3a-9f22-4377-9b43-6f649c73ff3b", "Setting start date to {}",
                    createPromotionRequest.getStartDate());
            discount.setStartDate(createPromotionRequest.getStartDate());
        } else {
            Logger.error("fdf88672-c34e-4151-bb3f-0ec86ce34f4c", "Start date cannot be in the past");
            throw new BadRequestException("Start date cannot be in the past");
        }

        if (TimeUtils.isFutureDate(createPromotionRequest.getExpireDate()) &&
                createPromotionRequest.getExpireDate().after(createPromotionRequest.getStartDate())) {
            Logger.info("40dcf8a4-e9b1-4287-b518-a2541237b9bf", "Setting expire date to {}",
                    createPromotionRequest.getExpireDate());
            discount.setExpireDate(createPromotionRequest.getExpireDate());
        } else {
            Logger.error("997e3c61-63ee-4e1b-8d56-60a5f3d4f9cd", "Expire date cannot be in the past");
            throw new BadRequestException("Expire date cannot be in the past");
        }

        this.isValidChannel(createPromotionRequest.getAvailableChannel());
        Logger.info("74219d66-6f01-44a6-8979-17993acb9812", "Setting available channel to {}",
                createPromotionRequest.getAvailableChannel());
        discount.setAvailableChannel(createPromotionRequest.getAvailableChannel());

        discount.setCreatedBy(authContext.getCurrentUser().getEmail());
        discount.setCreatedAt(new Date());
        discount.setEnabled(true);
        discount.setActive(true);
        Logger.info("8aa9fad3-9a96-411e-a599-03c89e3e5bca", "Creating discount with name: {}",
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
