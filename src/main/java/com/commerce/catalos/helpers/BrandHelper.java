package com.commerce.catalos.helpers;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.brands.CreateBrandRequest;
import com.commerce.catalos.models.brands.CreateBrandResponse;
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
}
