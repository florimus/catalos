package com.commerce.catalos.helpers;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.brands.BrandResponse;
import com.commerce.catalos.models.brands.CreateBrandRequest;
import com.commerce.catalos.models.brands.CreateBrandResponse;
import com.commerce.catalos.models.brands.UpdateBrandResponse;
import com.commerce.catalos.persistence.dtos.Brand;

public class BrandHelper {

    public static Brand toBrandFromCreateBrandRequest(final CreateBrandRequest createBrandRequest) {
        Brand brand = new Brand();
        BeanUtils.copyProperties(createBrandRequest, brand);
        return brand;
    }

    public static CreateBrandResponse toCreateBrandResponseFromBrand(final Brand brand) {
        CreateBrandResponse response = new CreateBrandResponse();
        BeanUtils.copyProperties(brand, response);
        return response;
    }

    public static BrandResponse toBrandResponseFromBrand(final Brand brand) {
        BrandResponse response = new BrandResponse();
        BeanUtils.copyProperties(brand, response);
        return response;
    }

    public static List<BrandResponse> toBrandListResponseFromBrands(final List<Brand> brands) {
        return brands.stream().map(BrandHelper::toBrandResponseFromBrand).toList();
    }

    public static UpdateBrandResponse toUpdateBrandResponseFromBrand(final Brand brand) {
        UpdateBrandResponse response = new UpdateBrandResponse();
        BeanUtils.copyProperties(brand, response);
        return response;
    }
}
