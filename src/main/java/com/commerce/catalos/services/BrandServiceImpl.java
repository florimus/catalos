package com.commerce.catalos.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.helpers.BrandHelper;
import com.commerce.catalos.models.brands.BrandResponse;
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

    private Brand findBrandById(final String id) {
        return brandRepository.findBrandByIdAndEnabled(id, true);
    }

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

    @Override
    public BrandResponse getBrandById(final String id) {
        if (id == null || id.isBlank()) {
            Logger.error("̦85d98bac-1140-4530-8aa9-1f92ee497090", "Brand id is mandatory");
            throw new BadRequestException("Invalid brand id");
        }
        Brand brand = this.findBrandById(id);
        if (brand == null || brand.getId().isBlank()) {
            Logger.error("c1a172d4-08cd-43f2-8572-300ccafa519a", "Brand not found with id: {}", id);
            throw new BadRequestException("Brand not found");
        }
        return BrandHelper.toBrandResponseFromBrand(brand);
    }

}
