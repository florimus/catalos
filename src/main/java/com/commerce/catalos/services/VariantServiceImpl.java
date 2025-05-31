package com.commerce.catalos.services;

import java.util.Date;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.VariantHelper;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.variants.CreateVariantRequest;
import com.commerce.catalos.models.variants.CreateVariantResponse;
import com.commerce.catalos.models.variants.UpdateVariantRequest;
import com.commerce.catalos.models.variants.UpdateVariantResponse;
import com.commerce.catalos.models.variants.VariantDeleteResponse;
import com.commerce.catalos.models.variants.VariantListResponse;
import com.commerce.catalos.models.variants.VariantResponse;
import com.commerce.catalos.models.variants.VariantStatusUpdateResponse;
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

    private final AuthContext authContext;

    private Variant findVariantById(final String id) {
        return variantRepository.findVariantByIdAndEnabled(id, true);
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
    public UpdateVariantResponse updateVariant(final UpdateVariantRequest updateVariantRequest) {
        return null;
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
        return null;
    }

    @Override
    public VariantDeleteResponse deleteVariant(final String id) {
        return null;
    }

}
