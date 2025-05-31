package com.commerce.catalos.services;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.variants.CreateVariantRequest;
import com.commerce.catalos.models.variants.CreateVariantResponse;
import com.commerce.catalos.models.variants.UpdateVariantRequest;
import com.commerce.catalos.models.variants.UpdateVariantResponse;
import com.commerce.catalos.models.variants.VariantListResponse;

public interface VariantService {

    public CreateVariantResponse createVariant(final CreateVariantRequest createVariantRequest);

    public UpdateVariantResponse updateVariant(final UpdateVariantRequest updateVariantRequest);

    public UpdateVariantResponse getVariantById(final String id);

    public Page<VariantListResponse> listVariants(String productId, String query, Pageable pageable);

}
