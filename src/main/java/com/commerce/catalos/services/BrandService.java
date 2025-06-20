package com.commerce.catalos.services;

import com.commerce.catalos.models.brands.CreateBrandRequest;
import com.commerce.catalos.models.brands.CreateBrandResponse;

public interface BrandService {

    CreateBrandResponse createBrand(final CreateBrandRequest createBrandRequest);

}
