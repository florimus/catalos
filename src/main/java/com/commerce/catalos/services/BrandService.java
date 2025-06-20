package com.commerce.catalos.services;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.brands.BrandResponse;
import com.commerce.catalos.models.brands.CreateBrandRequest;
import com.commerce.catalos.models.brands.CreateBrandResponse;
import com.commerce.catalos.models.brands.UpdateBrandRequest;
import com.commerce.catalos.models.brands.UpdateBrandResponse;

public interface BrandService {

    CreateBrandResponse createBrand(final CreateBrandRequest createBrandRequest);

    BrandResponse getBrandById(final String id);

    Page<BrandResponse> listBrands(final String query, final Pageable pageable);

    UpdateBrandResponse updateBrand(final String id, final UpdateBrandRequest updateBrandRequest);

}
