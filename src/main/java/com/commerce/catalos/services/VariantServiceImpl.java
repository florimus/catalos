package com.commerce.catalos.services;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.VariantHelper;
import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.productTypes.ProductTypeResponse;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.variants.CreateVariantRequest;
import com.commerce.catalos.models.variants.CreateVariantResponse;
import com.commerce.catalos.models.variants.UpdateVariantRequest;
import com.commerce.catalos.models.variants.UpdateVariantResponse;
import com.commerce.catalos.models.variants.VariantDeleteResponse;
import com.commerce.catalos.models.variants.VariantListResponse;
import com.commerce.catalos.models.variants.VariantResponse;
import com.commerce.catalos.models.variants.VariantStatusUpdateResponse;
import com.commerce.catalos.models.variants.VariantURLResponse;
import com.commerce.catalos.persistence.dtos.Variant;
import com.commerce.catalos.persistence.repositories.VariantRepository;
import com.commerce.catalos.security.AuthContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;

    private final ProductService productService;

    private final ProductTypeService productTypeService;

    @Lazy
    @Autowired
    private PriceService priceService;

    private final AuthContext authContext;

    private Variant findVariantById(final String id) {
        return variantRepository.findVariantByIdAndEnabled(id, true);
    }

    private Variant findVariantBySkuId(final String skuId) {
        return variantRepository.findVariantBySkuIdAndEnabled(skuId, true);
    }

    private Variant findVariantByUrl(final String url) {
        return variantRepository.findVariantByUrlAndEnabled(url, true);
    }

    private boolean isExitsBySkuOrSlug(final String sku, final String slug) {
        return variantRepository.existsBySkuIdOrSlugAndEnabled(sku, slug, true);
    }

    private String createUrl(final String productId, final String slug) {
        return String.format("/products/%s/variants/%s", productId, slug);
    }

    @Override
    public CreateVariantResponse createVariant(final CreateVariantRequest createVariantRequest) {
        ProductResponse product = productService.getProductById(createVariantRequest.getProductId());
        Variant variant = new Variant();
        variant.setName(createVariantRequest.getName());

        if (this.isExitsBySkuOrSlug(createVariantRequest.getSkuId(), createVariantRequest.getSlug())) {
            Logger.info("1d9a56b7-342b-4020-b391-0ef164c0ab9a",
                    "Variant with SKU or slug already exists: SKU={}, Slug={}",
                    createVariantRequest.getSkuId(), createVariantRequest.getSlug());
            throw new ConflictException("Variant with the same SKU or slug already exists.");
        }
        variant.setSkuId(createVariantRequest.getSkuId());
        variant.setSlug(createVariantRequest.getSlug());
        Logger.info("dfb82680-0d50-442f-96f8-169382cc812f",
                "Sku Id and Slug are unique for the variant: SKU={}, Slug={}",
                createVariantRequest.getSkuId(), createVariantRequest.getSlug());

        productTypeService.validateVariantAttributeValues(product.getProductTypeId(),
                createVariantRequest.getAttributes());
        Logger.info("91f63368-f0cd-4df5-9bcd-bc17f43c9502", "Variant attributes validated successfully");
        variant.setProductTypeId(product.getProductTypeId());
        variant.setProductId(product.getId());
        variant.setMedias(createVariantRequest.getMedias());
        variant.setAttributes(createVariantRequest.getAttributes());
        variant.setSeoTitle(createVariantRequest.getSeoTitle());
        variant.setSeoDescription(createVariantRequest.getSeoDescription());
        variant.setUrl(this.createUrl(product.getId(), createVariantRequest.getSlug()));
        variant.setCreatedBy(authContext.getCurrentUser().getEmail());
        variant.setCreatedAt(new Date());

        Logger.info("714c7346-eed5-43f6-8d0c-18d2aebd7dd7",
                "Creating variant with name: {}, SKU: {}, Slug: {}", createVariantRequest.getName(),
                createVariantRequest.getSkuId(), createVariantRequest.getSlug());

        return VariantHelper.toCreateVariantResponseFromVariant(variantRepository.save(variant));
    }

    @Override
    public UpdateVariantResponse updateVariant(final String id, final UpdateVariantRequest updateVariantRequest) {
        if (id.isBlank()) {
            Logger.error("2cea4552-31e4-43f5-9bfa-f16e76003422d", "Variant ID cannot be blank");
            throw new BadRequestException("Variant ID cannot be blank");
        }
        Variant variant = this.findVariantById(id);
        if (variant == null) {
            Logger.error("514f06bd-db7f-4eb5-a739-abec9b78573d", "Variant not found with ID: {}", id);
            throw new NotFoundException("Variant not found");
        }
        productTypeService.validateVariantAttributeValues(variant.getProductTypeId(),
                updateVariantRequest.getAttributes());
        Logger.info("8197b62a-1236-45e3-a448-c70cca7a5210", "Variant attributes validated successfully");
        if (updateVariantRequest.getName() != null && !updateVariantRequest.getName().isBlank()) {
            variant.setName(updateVariantRequest.getName());
        }
        variant.setMedias(updateVariantRequest.getMedias());
        variant.setAttributes(updateVariantRequest.getAttributes());
        variant.setSeoTitle(updateVariantRequest.getSeoTitle());
        variant.setSeoDescription(updateVariantRequest.getSeoDescription());
        Logger.info("76e3ade8-bdfd-4351-a621-b9060e9ab53b",
                "Updating variant with ID: {}, Name: {}, SKU: {}, Slug: {}", id,
                updateVariantRequest.getName(), variant.getSkuId(), variant.getSlug());
        return VariantHelper.toUpdateVariantResponseFromVariant(
                variantRepository.save(variant));
    }

    @Override
    public VariantResponse getVariantById(final String id) {
        if (id.isBlank()) {
            Logger.error("2cea4552-31e4-43f5-9bfa-f16e76003422d", "Variant ID cannot be blank");
            throw new BadRequestException("Variant ID cannot be blank");
        }
        Variant variant = this.findVariantById(id);
        if (variant == null) {
            Logger.error("514f06bd-db7f-4eb5-a739-abec9b78573d", "Variant not found with ID: {}", id);
            throw new NotFoundException("Variant not found");
        }
        Logger.info("118d104a-d5de-47a9-a95c-e8791fb2477a", "Retrieved variant with ID: {}", id);
        return VariantHelper.toVariantResponseFromVariant(variant);
    }

    @Override
    public Page<VariantListResponse> listVariants(final String productId, final String query, final Pageable pageable) {
        Logger.info("8a7eff54-302c-4341-970f-c064a8e69e5f",
                "Finding variants of product {} with query: {} and pageable: {}",
                productId, query, pageable);
        Page<Variant> variantsPage = variantRepository.searchProductVariants(productId, query, pageable);
        return new Page<VariantListResponse>(
                VariantHelper.toProductTypeListResponseFromVariants(variantsPage.getHits()),
                variantsPage.getTotalHitsCount(),
                variantsPage.getCurrentPage(),
                variantsPage.getPageSize());
    }

    @Override
    public VariantStatusUpdateResponse updateVariantStatus(final String id, final boolean status) {
        if (id.isBlank()) {
            Logger.error("6908e929-6848-46fe-8e6f-f111dc044dec", "Variant ID cannot be blank");
            throw new BadRequestException("Variant ID cannot be blank");
        }
        Variant variant = this.findVariantById(id);
        if (variant == null) {
            Logger.error("7b6c9625-03f8-422c-ab0a-a177bcfd47e1", "Variant not found with ID: {}", id);
            throw new NotFoundException("Variant not found");
        }
        Logger.info("b0c8f1d2-3a4e-4c5b-9f6d-7c8e9f0a1b2c",
                "Updating status of variant with ID: {} to {}", id, status);
        variant.setActive(status);
        variant.setUpdatedBy(authContext.getCurrentUser().getEmail());
        variant.setUpdatedAt(new Date());
        variantRepository.save(variant);
        return VariantStatusUpdateResponse.builder().status(status)
                .message(status ? "Variant Activated" : "Variant Deactivated").build();
    }

    @Override
    public VariantDeleteResponse deleteVariant(final String id) {
        if (id.isBlank()) {
            Logger.error("82238df9-c337-4bb3-9412-23ecb16018a9", "Variant ID cannot be blank");
            throw new BadRequestException("Variant ID cannot be blank");
        }
        Variant variant = this.findVariantById(id);
        if (variant == null) {
            Logger.error("77f535ca-ace2-461b-b1e3-3a3cc4d1d76e", "Variant not found with ID: {}", id);
            throw new NotFoundException("Variant not found");
        }
        Logger.info("363b58ed-3bb7-42a3-a85a-368626d0fa4b",
                "Deleting variant with ID: {}", id);
        variant.setEnabled(false);
        variant.setActive(false);
        variant.setUpdatedBy(authContext.getCurrentUser().getEmail());
        variant.setUpdatedAt(new Date());
        variantRepository.save(variant);
        return VariantDeleteResponse.builder()
                .message("Variant deleted successfully")
                .ids(List.of(variant.getId()))
                .build();
    }

    @Override
    public List<VariantListResponse> getVariantsByIds(List<String> variantIds) {
        List<Variant> variants = variantRepository.findAllById(variantIds);
        return VariantHelper.toProductTypeListResponseFromVariants(variants);
    }

    @Override
    public VariantResponse getVariantBySkuId(String skuId) {
        if (skuId.isBlank()) {
            Logger.error("0cc0a4de-b9d2-469f-bc61-e2c2ad5dcf7e", "SkuId cannot be blank");
            throw new BadRequestException("SkuId cannot be blank");
        }
        return VariantHelper.toVariantResponseFromVariant(this.findVariantBySkuId(skuId));
    }

    @Override
    public VariantURLResponse getVariantByURL(final String url, final String channel) {
        Variant variant = this.findVariantByUrl(url);
        if (variant == null) {
            Logger.error("cf4af9ad-6d7e-4be4-b9f7-78cef2a033e0", "Variant not found with URL: {}", url);
            throw new NotFoundException("Variant not found");
        }
        CompletableFuture<ProductResponse> productFuture = CompletableFuture
                .supplyAsync(() -> productService.getProductById(variant.getProductId()));
        CompletableFuture<ProductTypeResponse> productTypeFuture = CompletableFuture
                .supplyAsync(() -> productTypeService.getProductTypeById(variant.getProductTypeId()));

        CompletableFuture<CalculatedPriceResponse> pricesFuture = CompletableFuture
                .supplyAsync(() -> priceService.getPriceOfSku(variant.getSkuId(), channel, 1));

        ProductResponse product = productFuture.join();
        ProductTypeResponse productType = productTypeFuture.join();
        productType.setProductAttributes(null);
        productType.setVariantAttributes(null);
        CalculatedPriceResponse prices = pricesFuture.join();

        return VariantHelper.toVariantURLResponseFromVariant(variant, product, productType, prices);
    }

}
