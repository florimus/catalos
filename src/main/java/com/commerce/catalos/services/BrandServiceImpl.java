package com.commerce.catalos.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.helpers.BrandHelper;
import com.commerce.catalos.models.brands.CreateBrandRequest;
import com.commerce.catalos.models.brands.CreateBrandResponse;
import com.commerce.catalos.persistence.dtos.Brand;
import com.commerce.catalos.persistence.repositories.BrandRepository;
import com.commerce.catalos.security.AuthContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    private final AuthContext authContext;

    @Override
    public CreateBrandResponse createBrand(final CreateBrandRequest createBrandRequest) {
        Logger.info("6781fdf4-873d-4540-a9fa-9e0aba990d2a", "Received request for creating new brand : {}",
                createBrandRequest.getName());

        Brand brand = BrandHelper.toBrandFromCreateBrandRequest(createBrandRequest);
        brand.setCreatedBy(authContext.getCurrentUser().getEmail());
        brand.setCreatedAt(new Date());
        brand = brandRepository.save(brand);
        return BrandHelper.toCreateBrandResponseFromBrand(brand);
    }

}
