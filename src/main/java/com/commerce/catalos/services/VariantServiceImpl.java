package com.commerce.catalos.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.variants.CreateVariantRequest;
import com.commerce.catalos.models.variants.CreateVariantResponse;
import com.commerce.catalos.models.variants.UpdateVariantRequest;
import com.commerce.catalos.models.variants.UpdateVariantResponse;
import com.commerce.catalos.models.variants.VariantListResponse;

@Service
public class VariantServiceImpl implements VariantService {

    @Override
    public CreateVariantResponse createVariant(CreateVariantRequest createVariantRequest) {
        return null;
    }

    @Override
    public UpdateVariantResponse updateVariant(UpdateVariantRequest updateVariantRequest) {
        return null;
    }

    @Override
    public UpdateVariantResponse getVariantById(String id) {
        return null;
    }

    @Override
    public Page<VariantListResponse> listVariants(String productId, String query, Pageable pageable) {
        return null;
    }

}
