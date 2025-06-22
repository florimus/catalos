package com.commerce.catalos.services;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.variants.CreateVariantRequest;
import com.commerce.catalos.models.variants.CreateVariantResponse;
import com.commerce.catalos.models.variants.UpdateVariantRequest;
import com.commerce.catalos.models.variants.UpdateVariantResponse;
import com.commerce.catalos.models.variants.VariantDeleteResponse;
import com.commerce.catalos.models.variants.VariantListResponse;
import com.commerce.catalos.models.variants.VariantResponse;
import com.commerce.catalos.models.variants.VariantStatusUpdateResponse;
import com.commerce.catalos.models.variants.VariantURLResponse;

public interface VariantService {

    public CreateVariantResponse createVariant(final CreateVariantRequest createVariantRequest);

    public UpdateVariantResponse updateVariant(final String id, final UpdateVariantRequest updateVariantRequest);

    public VariantResponse getVariantById(final String id);

    public VariantResponse getVariantBySkuId(final String skuId);

    public Page<VariantListResponse> listVariants(final String productId, final String query, final Pageable pageable);

    public VariantStatusUpdateResponse updateVariantStatus(final String id, final boolean status);

    public VariantDeleteResponse deleteVariant(final String id);

    public List<VariantListResponse> getVariantsByIds(final List<String> variantIds);

    public VariantURLResponse getVariantByURL(final String url);

}
