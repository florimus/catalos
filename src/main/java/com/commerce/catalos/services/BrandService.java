package com.commerce.catalos.services;

import com.commerce.catalos.models.brands.*;
import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;

import java.util.List;

public interface BrandService {

    CreateBrandResponse createBrand(final CreateBrandRequest createBrandRequest);

    BrandResponse getBrandById(final String id);

    Page<BrandResponse> listBrands(final String query, final Pageable pageable);

    UpdateBrandResponse updateBrand(final String id, final UpdateBrandRequest updateBrandRequest);

    BrandStatusUpdateResponse updateBrandStatus(final String id, final boolean status);

    List<BrandResponse> listBrandsByIds(final BrandListRequest brandListRequest);
}
