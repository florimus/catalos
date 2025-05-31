package com.commerce.catalos.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.variants.CreateVariantRequest;
import com.commerce.catalos.models.variants.CreateVariantResponse;
import com.commerce.catalos.models.variants.UpdateVariantRequest;
import com.commerce.catalos.models.variants.UpdateVariantResponse;
import com.commerce.catalos.models.variants.VariantDeleteResponse;
import com.commerce.catalos.models.variants.VariantListResponse;
import com.commerce.catalos.models.variants.VariantStatusUpdateResponse;

@Service
public class VariantServiceImpl implements VariantService {

    @Override
    public CreateVariantResponse createVariant(final CreateVariantRequest createVariantRequest) {
        return null;
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
