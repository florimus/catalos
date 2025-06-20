package com.commerce.catalos.services;

import java.util.Date;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.helpers.BrandHelper;
import com.commerce.catalos.models.brands.BrandResponse;
import com.commerce.catalos.models.brands.BrandStatusUpdateResponse;
import com.commerce.catalos.models.brands.CreateBrandRequest;
import com.commerce.catalos.models.brands.CreateBrandResponse;
import com.commerce.catalos.models.brands.UpdateBrandRequest;
import com.commerce.catalos.models.brands.UpdateBrandResponse;
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
            Logger.error("85d98bac-1140-4530-8aa9-1f92ee497090", "Brand id is mandatory");
            throw new BadRequestException("Invalid brand id");
        }
        Brand brand = this.findBrandById(id);
        if (brand == null || brand.getId().isBlank()) {
            Logger.error("c1a172d4-08cd-43f2-8572-300ccafa519a", "Brand not found with id: {}", id);
            throw new BadRequestException("Brand not found");
        }
        return BrandHelper.toBrandResponseFromBrand(brand);
    }

    @Override
    public Page<BrandResponse> listBrands(String query, Pageable pageable) {
        Logger.info("fea267e4-8a32-4101-9cdb-2936b1539da4", "Finding brands with query: {} and pageable: {}",
                query, pageable);
        Page<Brand> brands = brandRepository.searchBrands(query, pageable);
        return new Page<BrandResponse>(
                BrandHelper.toBrandListResponseFromBrands(brands.getHits()),
                brands.getTotalHitsCount(),
                brands.getCurrentPage(),
                brands.getPageSize());
    }

    @Override
    public UpdateBrandResponse updateBrand(String id, UpdateBrandRequest updateBrandRequest) {
        if (id == null || id.isBlank()) {
            Logger.error("f12c49fa-afe6-45a7-be87-556bbc93e3a7", "Brand id is mandatory");
            throw new BadRequestException("Invalid brand id");
        }
        Brand brand = this.findBrandById(id);
        if (brand == null || brand.getId().isBlank()) {
            Logger.error("f4a8cccf-bc05-42ac-8fa0-f2a7c44d9df4", "Brand not found with id: {}", id);
            throw new BadRequestException("Brand not found");
        }
        Logger.info("47dba53e-f44c-4bca-a7c4-0f7f2d92052e4", "Finding brand with id: {}", id);
        if (updateBrandRequest.getName() != null) {
            brand.setName(updateBrandRequest.getName());
        }
        if (updateBrandRequest.getAvatar() != null) {
            brand.setAvatar(updateBrandRequest.getAvatar());
        }
        if (updateBrandRequest.getSeoTitle() != null) {
            brand.setSeoTitle(updateBrandRequest.getSeoTitle());
        }
        if (updateBrandRequest.getSeoDescription() != null) {
            brand.setSeoDescription(updateBrandRequest.getSeoDescription());
        }
        brand.setUpdatedBy(authContext.getCurrentUser().getEmail());
        brand.setUpdatedAt(new Date());
        brand = brandRepository.save(brand);
        Logger.info("da6c44a1-76ad-4575-b8ef-fabccdcffe67", "Updated brand with id: {}", id);
        return BrandHelper.toUpdateBrandResponseFromBrand(brand);
    }

    @Override
    public BrandStatusUpdateResponse updateBrandStatus(final String id, final boolean status) {
        if (id == null || id.isBlank()) {
            Logger.error("9a112f15-2e28-4ef5-a9d1-63d99cec0bea", "Brand id is mandatory");
            throw new BadRequestException("Invalid brand id");
        }
        Brand brand = this.findBrandById(id);
        if (brand == null || brand.getId().isBlank()) {
            Logger.error("d722e169-1f7d-4fe8-bcea-d0a6225d1a42", "Brand not found with id: {}", id);
            throw new BadRequestException("Brand not found");
        }
        Logger.info("618b2ebf-6932-4952-9351-6c5828e636ed", "Finding brand with id: {}", id);
        brand.setActive(status);
        brand.setUpdatedBy(authContext.getCurrentUser().getEmail());
        brand.setUpdatedAt(new Date());
        brand = brandRepository.save(brand);
        Logger.info("e1e5ca8e-3761-4a7e-a33b-61696006c21a", "Updated brand with id: {}", id);
        return BrandStatusUpdateResponse.builder().message(status ? "Brand Activated" : "Brand Deactivated")
                .status(status).build();
    }

}
