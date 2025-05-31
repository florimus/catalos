package com.commerce.catalos.services;

import java.util.Date;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.helpers.VariantHelper;
import com.commerce.catalos.models.products.ProductResponse;
import com.commerce.catalos.models.variants.CreateVariantRequest;
import com.commerce.catalos.models.variants.CreateVariantResponse;
import com.commerce.catalos.models.variants.UpdateVariantRequest;
import com.commerce.catalos.models.variants.UpdateVariantResponse;
import com.commerce.catalos.models.variants.VariantDeleteResponse;
import com.commerce.catalos.models.variants.VariantListResponse;
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
    public UpdateVariantResponse getVariantById(final String id) {
        return null;
    }

    @Override
    public Page<VariantListResponse> listVariants(final String productId, final String query, final Pageable pageable) {
        return null;
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
