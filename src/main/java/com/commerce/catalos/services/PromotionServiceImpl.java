package com.commerce.catalos.services;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.models.brands.BrandListRequest;
import com.commerce.catalos.models.brands.BrandResponse;
import com.commerce.catalos.models.categories.CategoryListRequest;
import com.commerce.catalos.models.categories.CategoryResponse;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.promotions.PromotionFilterInputs;
import com.commerce.catalos.models.promotions.PromotionResponse;
import com.commerce.catalos.models.variants.VariantListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
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

    private final CategoryService categoryService;

    private final BrandService brandService;

    private final ChannelService channelService;

    private final PromotionRepository promotionRepository;

    private final AuthContext authContext;

    private Discount findPromotionById(final String id) {
        return this.promotionRepository.findByIdAndEnabled(id, true);
    }

    private List<String> validateProductIds(final List<String> productIds) {
        return productService.getProductsByIds(productIds).stream().map(ProductResponse::getId).toList();
    }

    private List<String> validateVariantIds(final List<String> variantIds) {
        return variantService.getVariantsByIds(variantIds).stream().map(VariantListResponse::getId).toList();
    }

    private List<String> validateCategoriesIds(final List<String> categoriesIds) {
        CategoryListRequest request = new CategoryListRequest();
        request.setIds(categoriesIds);
        return categoryService.listCategoriesByIds(request).stream().map(CategoryResponse::getId).toList();
    }

    private List<String> validateBrandIds(final List<String> brandIds) {
        BrandListRequest request = new BrandListRequest();
        request.setIds(brandIds);
        return brandService.listBrandsByIds(request).stream().map(BrandResponse::getId).toList();
    }

    private void isValidChannel(final String channelId) {
        channelService.verifyChannels(List.of(channelId), true);
    }

    @Async
    private void validateAndUpdateVariants(final Discount discount, final List<String> variantIds) {
        if (variantIds != null && !variantIds.isEmpty()) {
            Logger.info("dee18d65-1a1a-415c-8bab-059bb48aefe0", "Setting targeted variants from {}", variantIds);
            discount.setTargetedVariantIds(this.validateVariantIds(variantIds));
        }
    }

    @Async
    private void validateAndUpdateProducts(final Discount discount, final List<String> productsIds) {
        if (productsIds != null && !productsIds.isEmpty()) {
            Logger.info("", "Setting targeted products from {}", productsIds);
            discount.setTargetedProductIds(this.validateProductIds(productsIds));
        }
    }

    @Async
    private void validateAndUpdateCategories(final Discount discount, final List<String> categoryIds) {
        if (categoryIds != null && !categoryIds.isEmpty()) {
            Logger.info("", "Setting targeted categories from {}", categoryIds);
            discount.setTargetedCategories(this.validateCategoriesIds(categoryIds));
        }
    }

    @Async
    private void validateAndUpdateBrands(final Discount discount, final List<String> brandIds) {
        if (brandIds != null && !brandIds.isEmpty()) {
            Logger.info("", "Setting targeted brands from {}", brandIds);
            discount.setTargetedCategories(this.validateBrandIds(brandIds));
        }
    }

    private void validateAndUpdateStartDate(final Discount discount, final Date startDate) {
        if (TimeUtils.isFutureDate(startDate)) {
            Logger.info("4b8689d3-3bd1-47ba-933d-695a6391449f", "Setting start date to {}",
                    startDate);
            discount.setStartDate(startDate);
        } else {
            Logger.error("04a8bdb4-7ed1-42bb-968d-f5f661234c4f", "Start date cannot be in the past");
            throw new BadRequestException("Start date cannot be in the past");
        }
    }

    private void validateAndUpdateExpireDate(final Discount discount, final Date expireDate) {
        if (TimeUtils.isFutureDate(expireDate) && expireDate.after(discount.getStartDate())) {
            Logger.info("e857d8d4-9df6-48d4-a4a0-18cd5dcca1f8", "Setting expire date to {}", expireDate);
            discount.setExpireDate(expireDate);
        } else {
            Logger.error("6b37abea-6689-4e16-9018-c52592af90b6", "Expire date cannot be in the past");
            throw new BadRequestException("Expire date cannot be in the past");
        }
    }

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

    public Discount createFlatOFFDiscount(final CreatePromotionRequest createPromotionRequest) {
        Logger.info("8013b8ff-a153-423c-ac1b-f647cc7fe609", "Validating Flat Off Discount {}",
                createPromotionRequest.getName());
        Discount discount = new Discount();
        discount.setName(createPromotionRequest.getName());
        discount.setDiscountMode(createPromotionRequest.getDiscountMode());
        discount.setDiscountType(createPromotionRequest.getDiscountType());
        discount.setDiscountValue(createPromotionRequest.getDiscountValue());
        discount.setMaxDiscountPrice(createPromotionRequest.getMaxDiscountPrice());
        discount.setForAllProducts(createPromotionRequest.isForAllProducts());
        discount.setMinItemQuantity(createPromotionRequest.getMinItemQuantity());

        discount.setAvailableChannel(createPromotionRequest.getAvailableChannel());

        if(createPromotionRequest.isForAllProducts()){
            discount.setTargetedProductIds(List.of());
            discount.setTargetedVariantIds(List.of());
            discount.setTargetedCategories(List.of());
            discount.setTargetedBrands(List.of());
        } else {
            ExecutorService executor = Executors.newFixedThreadPool(4);

            CompletableFuture<Void> variantsFuture = CompletableFuture.runAsync(
                    () -> validateAndUpdateVariants(discount, createPromotionRequest.getTargetedVariantIds()), executor);
            CompletableFuture<Void> productsFuture = CompletableFuture.runAsync(
                    () -> validateAndUpdateProducts(discount, createPromotionRequest.getDiscountedProducts()), executor);
            CompletableFuture<Void> categoriesFuture = CompletableFuture.runAsync(
                    () -> validateAndUpdateCategories(discount, createPromotionRequest.getTargetedCategories()), executor);
            CompletableFuture<Void> brandsFuture = CompletableFuture.runAsync(
                    () -> validateAndUpdateBrands(discount, createPromotionRequest.getTargetedBrands()), executor);
            CompletableFuture.allOf(variantsFuture, productsFuture, categoriesFuture, brandsFuture).join();

            executor.shutdown();
        }

        // TODO: Implement the logic verify collections
        // TODO: Implement the logic verify customer groups

        this.validateAndUpdateStartDate(discount, createPromotionRequest.getStartDate());
        this.validateAndUpdateExpireDate(discount, createPromotionRequest.getExpireDate());

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

        discount.setAvailableChannel(createPromotionRequest.getAvailableChannel());

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

        discount.setMinItemQuantity(createPromotionRequest.getMinItemQuantity());
        discount.setForAllProducts(createPromotionRequest.isForAllProducts());

        if(createPromotionRequest.isForAllProducts()){
            discount.setTargetedProductIds(List.of());
            discount.setTargetedVariantIds(List.of());
            discount.setTargetedCategories(List.of());
            discount.setTargetedBrands(List.of());
        } else {
            ExecutorService executor = Executors.newFixedThreadPool(4);

            CompletableFuture<Void> variantsFuture = CompletableFuture.runAsync(
                    () -> validateAndUpdateVariants(discount, createPromotionRequest.getTargetedVariantIds()), executor);
            CompletableFuture<Void> productsFuture = CompletableFuture.runAsync(
                    () -> validateAndUpdateProducts(discount, createPromotionRequest.getDiscountedProducts()), executor);
            CompletableFuture<Void> categoriesFuture = CompletableFuture.runAsync(
                    () -> validateAndUpdateCategories(discount, createPromotionRequest.getTargetedCategories()), executor);
            CompletableFuture<Void> brandsFuture = CompletableFuture.runAsync(
                    () -> validateAndUpdateBrands(discount, createPromotionRequest.getTargetedBrands()), executor);
            CompletableFuture.allOf(variantsFuture, productsFuture, categoriesFuture, brandsFuture).join();

            executor.shutdown();
        }

        // TODO: Implement the logic verify collections
        // TODO: Implement the logic verify customer groups

        this.validateAndUpdateStartDate(discount, createPromotionRequest.getStartDate());
        this.validateAndUpdateExpireDate(discount, createPromotionRequest.getExpireDate());

        this.isValidChannel(createPromotionRequest.getAvailableChannel());
        Logger.info("74219d66-6f01-44a6-8979-17993acb9812", "Setting available channel to {}",
                createPromotionRequest.getAvailableChannel());

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

    @Override
    public Page<PromotionResponse> searchPromotions(
            final String query, final String channel, final PromotionFilterInputs promotionFilterInputs, final Pageable pageable) {
        Logger.info("", "Finding promotion with query: {}, filter: {} and pageable: {}",
                query, promotionFilterInputs, pageable);
        Page<Discount> promotions = promotionRepository.getPromotions(query, channel, promotionFilterInputs, pageable);
        return new Page<PromotionResponse>(
                PromotionHelper.toPromotionResponsesFromDiscounts(promotions.getHits()),
                promotions.getTotalHitsCount(),
                promotions.getCurrentPage(),
                promotions.getPageSize());
    }

    @Override
    public PromotionResponse getPromotionById(final String id) {
        if (null == id || id.isBlank()){
            Logger.error("", "Id is mandatory");
            throw new BadRequestException("Invalid promotion id");
        }
        Discount promotion = this.findPromotionById(id);
        if (null != promotion){
            return PromotionHelper.toPromotionResponseFromDiscount(promotion);
        }
        Logger.error("", "Promotion not found");
        throw new NotFoundException("Promotion not found");
    }

}
